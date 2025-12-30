# Module Dependency Graph

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

## Dependency Rules

### Required Dependencies
- **proteus-core**: Always required, no external dependencies beyond Android SDK
- **proteus-firebase**: Requires proteus-core + Firebase Config SDK
- **proteus-ui**: Requires proteus-core + Jetpack Compose

### Optional Dependencies
- Use **proteus-bom** for version management (recommended)
- Choose specific provider modules based on your needs

### Minimum Requirements
- Android SDK 23+
- Kotlin 1.8+
- Target SDK 34+