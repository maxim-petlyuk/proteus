# Proteus Architecture

This document provides a comprehensive overview of the Proteus architecture, including high-level design, module dependencies, configuration lifecycle, and runtime override mechanisms.

## Overview

Proteus is a multi-module Android library that provides A/B testing and remote configuration capabilities with runtime override functionality. The architecture is designed to be:

- **Modular**: Independent modules with clear responsibilities
- **Extensible**: Support for custom providers and data sources
- **Type-safe**: Strongly-typed configuration access
- **Testable**: Runtime override capabilities for testing

## High-Level Architecture

The system consists of four main layers:

```mermaid
graph TB
    subgraph "Application Layer"
        APP[Android App]
        UI[User Interface]
    end

    subgraph "Proteus Core"
        PROTEUS[Proteus Singleton]
        PROVIDER[FeatureConfigProvider]
        MOCK[MockConfigProvider]
        STORAGE[SharedPreferences]
    end

    subgraph "Provider Layer"
        FIREBASE[Firebase Provider]
        CUSTOM[Custom Provider]
        CLEVERTAP[CleverTap Provider]
    end

    subgraph "Data Sources"
        ASSETS[Assets JSON]
        CODE[Code Definition]
        REMOTE[Remote Config]
    end

    subgraph "Override UI"
        ACTIVITY[FeatureBookActivity]
        CATALOG[FeatureCatalogScreen]
        CONFIG[FeatureConfiguratorScreen]
    end

    %% Data Flow
    APP --> PROTEUS
    UI --> PROVIDER
    PROTEUS --> PROVIDER
    PROVIDER --> MOCK
    MOCK --> STORAGE
    PROVIDER --> FIREBASE
    PROVIDER --> CUSTOM
    PROVIDER --> CLEVERTAP

    %% Data Sources
    ASSETS --> PROTEUS
    CODE --> PROTEUS
    REMOTE --> FIREBASE
    REMOTE --> CLEVERTAP

    %% Override UI
    APP --> ACTIVITY
    ACTIVITY --> CATALOG
    CATALOG --> CONFIG
    CONFIG --> MOCK

    %% Styling
    classDef appLayer fill:#B59D82,stroke:#7A6650,color:#fff
    classDef coreLayer fill:#F5EFE6,stroke:#B59D82,color:#5C4D3D
    classDef providerLayer fill:#EBE0D0,stroke:#9D8468,color:#5C4D3D
    classDef dataLayer fill:#D9C8B0,stroke:#7A6650,color:#5C4D3D
    classDef uiLayer fill:#C7B299,stroke:#7A6650,color:#fff

    class APP,UI appLayer
    class PROTEUS,PROVIDER,MOCK,STORAGE coreLayer
    class FIREBASE,CUSTOM,CLEVERTAP providerLayer
    class ASSETS,CODE,REMOTE dataLayer
    class ACTIVITY,CATALOG,CONFIG uiLayer
```

### Key Components

1. **Application Layer**: Your Android app and UI components
2. **Proteus Core**: Central coordination and abstraction layer
3. **Provider Layer**: Different remote config implementations
4. **Data Sources**: Feature definitions and remote configurations
5. **Override UI**: Runtime configuration management interface

## Module Dependencies

```mermaid
graph TD
    subgraph "Application"
        APP[Your Android App]
    end

    subgraph "Proteus Modules"
        BOM[proteus-bom]
        CORE[proteus-core]
        FIREBASE[proteus-firebase]
        UI[proteus-ui]
    end

    subgraph "External Dependencies"
        ANDROID[Android SDK 23+]
        COMPOSE[Jetpack Compose]
        FIREBASE_SDK[Firebase Config SDK]
        KOTLIN[Kotlin Stdlib]
        COROUTINES[Kotlin Coroutines]
    end

    %% Dependencies
    APP --> BOM
    BOM -.-> CORE
    BOM -.-> FIREBASE
    BOM -.-> UI

    APP --> CORE
    APP --> FIREBASE
    APP --> UI

    FIREBASE --> CORE
    UI --> CORE

    %% External Dependencies
    CORE --> ANDROID
    CORE --> KOTLIN
    CORE --> COROUTINES

    FIREBASE --> FIREBASE_SDK
    FIREBASE --> ANDROID

    UI --> COMPOSE
    UI --> ANDROID

    %% Styling
    classDef appModule fill:#B59D82,stroke:#7A6650,color:#fff
    classDef proteusModule fill:#F5EFE6,stroke:#B59D82,color:#5C4D3D
    classDef externalDep fill:#EBE0D0,stroke:#9D8468,color:#5C4D3D

    class APP appModule
    class BOM,CORE,FIREBASE,UI proteusModule
    class ANDROID,COMPOSE,FIREBASE_SDK,KOTLIN,COROUTINES externalDep
```

### Dependency Rules

- **proteus-core**: Always required, minimal dependencies
- **proteus-firebase**: Requires proteus-core + Firebase Config SDK
- **proteus-ui**: Requires proteus-core + Jetpack Compose
- **proteus-bom**: Optional but recommended for version management

## Configuration Lifecycle

The configuration system follows a well-defined lifecycle:

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

### Lifecycle Phases

1. **Initialization**: App sets up Proteus with providers and data sources
2. **Feature Discovery**: Load feature definitions from assets or code
3. **Provider Registration**: Register remote config providers
4. **Override Loading**: Load existing overrides from SharedPreferences
5. **Runtime Access**: App requests configuration values
6. **Value Resolution**: Check overrides first, fallback to remote
7. **Override Management**: Runtime changes via UI or programmatically

## Runtime Override Flow

The override mechanism provides priority-based value resolution:

```mermaid
flowchart TD
    START([App Requests Config Value])
    PROVIDER{FeatureConfigProvider}
    MOCK_CHECK{MockConfigProvider<br/>has override?}
    OVERRIDE_VALUE[Return Override Value]
    REMOTE_CHECK{Remote Provider<br/>has value?}
    REMOTE_VALUE[Return Remote Value]
    DEFAULT_VALUE[Return Default Value]
    END_SUCCESS([Value Returned])
    END_DEFAULT([Default Returned])

    subgraph "Override Management"
        UI_OVERRIDE[Override UI Action]
        SET_OVERRIDE[Set Override in Mock]
        PERSIST[Persist to SharedPreferences]
        REMOVE_OVERRIDE[Remove Override]
        DELETE_PERSIST[Delete from SharedPreferences]
    end

    %% Main Flow
    START --> PROVIDER
    PROVIDER --> MOCK_CHECK
    MOCK_CHECK -->|Yes| OVERRIDE_VALUE
    OVERRIDE_VALUE --> END_SUCCESS

    MOCK_CHECK -->|No| REMOTE_CHECK
    REMOTE_CHECK -->|Yes| REMOTE_VALUE
    REMOTE_VALUE --> END_SUCCESS

    REMOTE_CHECK -->|No| DEFAULT_VALUE
    DEFAULT_VALUE --> END_DEFAULT

    %% Override Management Flow
    UI_OVERRIDE --> SET_OVERRIDE
    SET_OVERRIDE --> PERSIST
    PERSIST --> MOCK_CHECK

    UI_OVERRIDE --> REMOVE_OVERRIDE
    REMOVE_OVERRIDE --> DELETE_PERSIST
    DELETE_PERSIST --> MOCK_CHECK

    %% Styling
    classDef startEnd fill:#B59D82,stroke:#7A6650,color:#fff
    classDef process fill:#F5EFE6,stroke:#B59D82,color:#5C4D3D
    classDef decision fill:#EBE0D0,stroke:#9D8468,color:#5C4D3D
    classDef override fill:#C7B299,stroke:#7A6650,color:#fff

    class START,END_SUCCESS,END_DEFAULT startEnd
    class PROVIDER,OVERRIDE_VALUE,REMOTE_VALUE,DEFAULT_VALUE process
    class MOCK_CHECK,REMOTE_CHECK decision
    class UI_OVERRIDE,SET_OVERRIDE,PERSIST,REMOVE_OVERRIDE,DELETE_PERSIST override
```

### Override Priority

1. **Mock Override** (Highest Priority) - Set via UI or programmatically
2. **Remote Provider Value** (Medium Priority) - Firebase, Custom providers
3. **Default Value** (Lowest Priority) - Defined in feature definition

## Design Principles

### Modularity
Each module has a single, well-defined responsibility:
- **proteus-core**: Core abstractions and interfaces
- **proteus-firebase**: Firebase Remote Config integration
- **proteus-ui**: Material Design override interface

### Extensibility
The system supports custom implementations:
- Custom provider factories
- Custom data sources
- Custom configuration sources

### Type Safety
Strong typing prevents configuration errors:
- `ConfigValue` sealed class for type safety
- Compile-time type checking
- Runtime type validation

### Testing
Built-in testing capabilities:
- Mock provider for overrides
- Runtime configuration changes
- UI for manual testing

## Performance Considerations

### Memory Usage
- Singleton pattern minimizes instance overhead
- Lazy loading of providers
- Efficient caching of configuration values

### Storage
- SharedPreferences for lightweight persistence
- JSON serialization for complex values
- Automatic cleanup of unused overrides

### Network
- Provider-specific optimization
- Caching strategies vary by provider
- Background refresh capabilities

## Security Considerations

### Production Safety
- Override UI should be disabled in release builds
- No sensitive data in feature definitions
- Secure provider implementations

### Data Protection
- Local storage encryption (provider-dependent)
- Network security follows provider standards
- No logging of sensitive configuration values

## Migration & Compatibility

### Version Compatibility
- Semantic versioning across all modules
- BOM ensures compatible versions
- Clear migration guides for breaking changes

### Provider Migration
- Gradual migration between providers
- Fallback mechanisms during transition
- Data export/import capabilities

---

## Further Reading

- [High-Level Architecture](architecture/high-level-architecture.md)
- [Module Dependencies](architecture/module-dependency-graph.md)
- [Configuration Lifecycle](architecture/configuration-lifecycle.md)
- [Runtime Override Flow](architecture/runtime-override-flow.md)

## Contributing

For architecture-related discussions or proposals, please:
1. Review existing architecture documents
2. Open a GitHub Discussion for design questions
3. Submit PRs with architecture updates when needed

---

*Last updated: December 2024*
*Version: 1.0*