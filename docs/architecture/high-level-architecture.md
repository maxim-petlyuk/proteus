# High-Level Architecture

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

## Key Components

- **Application Layer**: Your Android app and UI components
- **Proteus Core**: Central coordination and abstraction layer
- **Provider Layer**: Different remote config implementations
- **Data Sources**: Feature definitions and remote configurations
- **Override UI**: Runtime configuration management interface

## Data Flow

1. App initializes Proteus with providers and data sources
2. Configuration requests flow through FeatureConfigProvider
3. MockConfigProvider checks for overrides first
4. Falls back to remote providers (Firebase, Custom, etc.)
5. Override UI allows runtime configuration changes