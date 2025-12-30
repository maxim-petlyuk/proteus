# Configuration Lifecycle

```mermaid
sequenceDiagram
    participant App as Android App
    participant Proteus as Proteus Core
    participant DataSource as FeatureBookDataSource
    participant Provider as ConfigProvider
    participant Mock as MockConfigProvider
    participant Remote as Remote Provider
    participant Storage as SharedPreferences

    Note over App,Storage: 1. Initialization Phase
    App->>Proteus: Builder().build()
    Proteus->>DataSource: getFeatureBook()
    DataSource-->>Proteus: List<FeatureContext>
    Proteus->>Provider: registerProvider()
    Proteus->>Mock: initialize()
    Mock->>Storage: load existing overrides

    Note over App,Storage: 2. Configuration Access
    App->>Provider: getBoolean("feature_key")
    Provider->>Mock: hasOverride("feature_key")

    alt Override exists
        Mock-->>Provider: override value
        Provider-->>App: override value
    else No override
        Provider->>Remote: getBoolean("feature_key")
        Remote-->>Provider: remote value
        Provider-->>App: remote value
    end

    Note over App,Storage: 3. Runtime Override
    App->>Mock: setOverride("feature_key", value)
    Mock->>Storage: persist override
    Storage-->>Mock: confirmation
    Mock-->>App: success

    Note over App,Storage: 4. Override Removal
    App->>Mock: removeOverride("feature_key")
    Mock->>Storage: delete override
    Storage-->>Mock: confirmation
    Mock-->>App: success
```

## Lifecycle Phases

1. **Initialization**: App sets up Proteus with providers and data sources
2. **Feature Discovery**: Load feature definitions from assets or code
3. **Provider Registration**: Register remote config providers
4. **Override Loading**: Load existing overrides from SharedPreferences
5. **Runtime Access**: App requests configuration values
6. **Value Resolution**: Check overrides first, fallback to remote
7. **Override Management**: Runtime changes via UI or programmatically