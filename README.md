![proteus](https://raw.githubusercontent.com/maxim-petlyuk/proteus/refs/heads/main/assets/banner.png)

<p align="center">
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License"></a>
    <a href="https://android-arsenal.com/api?level=23"><img src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat" alt="API"></a>
    <a href="https://github.com/maxim-petlyuk/proteus-config/actions"><img src="https://img.shields.io/github/actions/workflow/status/maxim-petlyuk/proteus/release-bom-only.yml?branch=main" alt="Build Status"></a>
    <a href="https://github.com/maxim-petlyuk/proteus/tree/main/docs"><img src="https://img.shields.io/badge/docs-latest-brightgreen.svg" alt="Documentation"></a>
    <a href="https://github.com/maxim-petlyuk"><img src="https://img.shields.io/badge/Profile-maxim--petlyuk-blue" alt="Profile"></a><br>
    <a href="https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-core"><img src="https://img.shields.io/maven-central/v/io.github.maxim-petlyuk/proteus-core.svg?label=proteus-core" alt="Maven Central: proteus-core"></a>
    <a href="https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-firebase"><img src="https://img.shields.io/maven-central/v/io.github.maxim-petlyuk/proteus-firebase.svg?label=proteus-firebase" alt="Maven Central: proteus-firebase"></a>
    <a href="https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-ui"><img src="https://img.shields.io/maven-central/v/io.github.maxim-petlyuk/proteus-ui.svg?label=proteus-ui" alt="Maven Central: proteus-ui"></a>
    <a href="https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-bom"><img src="https://img.shields.io/maven-central/v/io.github.maxim-petlyuk/proteus-bom.svg?label=proteus-bom" alt="Maven Central: proteus-bom"></a>
</p>

<div>
Proteus is an Android library for managing remote configurations with runtime override capabilities. It provides a type-safe abstraction
layer over remote config providers like Firebase, enabling teams to test feature flags and A/B variants instantly without server
deployments. The built-in Material Design 3 UI allows you to modify configurations directly on your device during development and testing.
With multi-module architecture and BOM support, Proteus seamlessly integrates into any Android application.
</div>

## Why Proteus?

**The Problem:** Testing remote configurations requires server deployments, slowing down your development cycle and making A/B testing
tedious.

**The Solution:** Proteus provides a runtime override UI that lets you instantly test any configuration scenario without waiting for backend
changes.

**Unique Value:** Test feature flags, A/B variants, and remote configs in real-time directly on your device - no server round-trips
required.

## How it looks like?

<p align="center">
<img src="https://github.com/maxim-petlyuk/proteus/blob/main/assets/demo_enable_mock.gif?raw=true" width="280" alt="Enable mock config"/>
<img src="https://github.com/maxim-petlyuk/proteus/blob/main/assets/demo_disable_mock.gif?raw=true" width="280" alt="Disable mock config"/>
</p>

## Key Features

- **Runtime Configuration Override** - Modify remote configs instantly through a polished local UI
- **Multi-Provider Support** - Seamlessly integrate with Firebase, CleverTap, or custom providers
- **Material Design 3** - Beautiful beige-themed UI that follows the latest design guidelines
- **Multi-Module Architecture** - Independent versioning with BOM support for simplified dependency management
- **Production-Ready** - Comprehensive error handling, thread-safe operations, and coroutines support

## Packages

| Package | Description |
|---------|-------------|
| [proteus-core](https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-core) | Core abstraction layer for remote configuration providers |
| [proteus-firebase](https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-firebase) | Firebase Remote Config provider implementation |
| [proteus-ui](https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-ui) | Material Design 3 UI for runtime configuration overrides |
| [proteus-bom](https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-bom) | Bill of Materials for consistent version management |

## Installation

### Using BOM (Recommended)

<a href="https://search.maven.org/artifact/io.github.maxim-petlyuk/proteus-bom"><img src="https://img.shields.io/maven-central/v/io.github.maxim-petlyuk/proteus-bom.svg?label=proteus-bom" alt="Maven Central: proteus-bom"></a>

The Bill of Materials (BOM) ensures all Proteus modules use compatible versions:

```toml
[versions]
proteus = "$version"

[libraries]
proteus-bom = { module = "io.github.maxim-petlyuk:proteus-bom", version.ref = "proteus" }
proteus-core = { module = "io.github.maxim-petlyuk:proteus-core" }
proteus-firebase = { module = "io.github.maxim-petlyuk:proteus-firebase" }
proteus-ui = { module = "io.github.maxim-petlyuk:proteus-ui" }

[bundles]
proteus = ["proteus-core", "proteus-firebase", "proteus-ui"]
```

Then in your build.gradle.kts:

```kotlin
dependencies {
    implementation(platform(libs.proteus.bom))
    implementation(libs.bundles.proteus)
}
```

### Requirements

- **Minimum Android SDK**: 23
- **Jetpack Compose**: Required for proteus-ui module only

## Quick Start

Get Proteus up and running in minutes with this minimal example:
#### 1. Initialized first:

```kotlin
class MainApp : Application() {
    
    override fun onCreate() {
        super.onCreate()

        /* IMPORTANT: Initialize Firebase Remote Config BEFORE using FirebaseOnlyProviderFactory */

        // Initialize Proteus with your feature definitions
        Proteus.Builder(this)
            .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
            .registerFeatureBookDataSource(
                // Option A: Load from assets/features.json
                AssetsFeatureBookDataSource(this, "features.json")
                // Option B: Use runtime code
                // StaticFeatureBookDataSource()
            )
            .build()
    }
}
```

#### 2. Build Proteus config provider
```kotlin
val provider = Proteus.getInstance().buildConfigProvider()
```

#### 3. Access configuration values anywhere in your app
```kotlin
val darkModeEnabled = provider.getBoolean("dark_mode_enabled")
val maxItems = provider.getLong("max_items_per_page")
```

#### 4. Show Proteus library UI for overriding remote config
```kotlin
startActivity(Intent(this, FeatureBookActivity::class.java))
```

## Data Source Options

#### Option A: JSON file in assets folder

Create `assets/features.json`:
```json
[
  {
    "feature_key": "dark_mode_enabled",
    "default_value": "false",
    "value_type": "boolean"
  },
  {
    "feature_key": "max_items_per_page",
    "default_value": "20",
    "value_type": "long"
  }
]
```

Then register it during initialization:
```kotlin
Proteus.Builder(this)
    .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
    .registerFeatureBookDataSource(AssetsFeatureBookDataSource(context = this, jsonFilePath = "features.json"))
    .build()
```

#### Option B: Runtime code

Create a custom data source with your feature definitions:

```kotlin
class SampleFeatureBookDataSource : FeatureBookDataSource {

    override suspend fun getFeatureBook(): Result<List<FeatureContext<*>>> {
        return Result.success(
            listOf(
                Feature(
                    key = "dark_mode_enabled",
                    defaultValue = false,
                    valueClass = Boolean::class
                ),
                Feature(
                    key = "max_items_per_page",
                    defaultValue = 20L,
                    valueClass = Long::class
                ),
                Feature(
                    key = "api_timeout_seconds",
                    defaultValue = 30L,
                    valueClass = Long::class
                ),
                Feature(
                    key = "animation_duration_multiplier",
                    defaultValue = 1.0,
                    valueClass = Double::class
                ),
                Feature(
                    key = "primary_server_url",
                    defaultValue = "https://api.production.com",
                    valueClass = String::class
                )
            )
        )
    }
}
```

Then register it during initialization:
```kotlin
Proteus.Builder(this)
    .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
    .registerFeatureBookDataSource(SampleFeatureBookDataSource())
    .build()
```

## Before vs After: Firebase Remote Config Migration

### Before: Direct Firebase Remote Config Usage

```kotlin
class FeatureManager(
    private val remoteConfig : FirebaseRemoteConfig
) {     

    fun isDarkModeEnabled(): Boolean {        
        return remoteConfig.getBoolean("dark_mode_enabled")
    }

    fun getApiUrl(): String {
        return remoteConfig.getString("primary_server_url")
    }
}
```

### After: Using Proteus

```kotlin
class FeatureManager(
    private val configProvider: FeatureConfigProvider
) {

    // Runtime override available via UI
    fun isDarkModeEnabled(): Boolean {
        return configProvider.getBoolean("dark_mode_enabled")
    }

    // Switch between servers instantly for testing
    fun getApiUrl(): String {        
        return configProvider.getString("primary_server_url")
    }
}
```

### Key Benefits

| Aspect | Before (Firebase only) | After (Proteus) |
|--------|------------------------|-----------------|
| **Testing Speed** | Minutes (Firebase Console changes) | Instant (runtime UI) |
| **Override Capability** | None | Full runtime override |
| **A/B Testing** | Requires Firebase Console setup | Test locally first |
| **Debug Features** | Switch between Firebase Console & app | Visual UI directly in app |
| **Development Workflow** | Firebase Console dependent | Independent local testing |

## Core Concepts

### Architecture Overview

Proteus uses a layered architecture with runtime override capabilities:

```mermaid
graph TB
    App[Your App]
    UI[Runtime Override UI]
    Core[Proteus Core<br/>FeatureConfigProvider]
    Mock[MockConfigProvider]
    Storage[(SharedPreferences<br/>Local Overrides)]
    Firebase[Firebase Remote Config]
    CleverTap[CleverTap]
    Custom[Custom Provider]

    App --> Core
    UI --> Core
    Core --> Mock
    Mock --> Storage
    Core -.fallback.-> Firebase
    Core -.fallback.-> CleverTap
    Core -.fallback.-> Custom

    style App fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    style UI fill:#fff3e0,stroke:#e65100,stroke-width:2px
    style Core fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    style Mock fill:#fce4ec,stroke:#880e4f,stroke-width:2px
    style Storage fill:#e8f5e9,stroke:#1b5e20,stroke-width:2px
    style Firebase fill:#fff8e1,stroke:#f57f17,stroke-width:2px
    style CleverTap fill:#fff8e1,stroke:#f57f17,stroke-width:2px
    style Custom fill:#fff8e1,stroke:#f57f17,stroke-width:2px
```

### Key Components

- **Proteus** - Central singleton that coordinates the entire system
- **FeatureConfigProvider** - Type-safe interface for configuration access
- **FeatureBookDataSource** - Defines all available features and their metadata
- **MockConfigProvider** - Enables runtime overrides via local storage
- **ConfigValue** - Type-safe wrapper ensuring correct value types

### Provider Abstraction

All configuration providers implement a simple, type-safe interface:

```kotlin
interface FeatureConfigProvider {
    fun getBoolean(featureKey: String): Boolean
    fun getString(featureKey: String): String
    fun getLong(featureKey: String): Long
    fun getDouble(featureKey: String): Double
}
```

### Runtime Override Mechanism

The override mechanism follows a simple priority system:

1. **Check Local Override**: First checks `MockConfigProvider` for user-overridden values
2. **Fallback to Remote**: If no override exists, fetches from remote provider (Firebase, etc.)
3. **Type Safety**: `ConfigValue` sealed class ensures type-safe value handling
4. **Persistence**: Overrides are persisted in SharedPreferences across app sessions

```kotlin
// How FeatureConfigProviderImpl resolves values
fun getBoolean(featureKey: String): Boolean {
    return try {
        mockConfigProvider.getBoolean(featureKey)  // Check override first
    } catch (e: MockConfigUnavailableException) {
        remoteProvider.getBoolean(featureKey)      // Fallback to remote
    }
}
```

### Feature Definition

Features are defined with strong typing and default values:

```kotlin
data class Feature<DataType : Any>(
    val key: String,                    // Unique identifier
    val defaultValue: DataType,         // Fallback value
    val valueClass: KClass<DataType>    // Type information
)

// Example usage
Feature(
    key = "dark_mode_enabled",
    defaultValue = false,
    valueClass = Boolean::class
)
```

### Configuration Lifecycle

1. **Initialization**: App registers providers and data sources with `Proteus.Builder`
2. **Feature Discovery**: `FeatureBookDataSource` provides all feature definitions
3. **Value Resolution**: Provider checks overrides, then remote sources
4. **Runtime Override**: UI allows instant value changes during development
5. **Persistence**: Changes are saved and restored on next app launch

### Type Safety with ConfigValue

Proteus uses a sealed class to ensure type safety:

```kotlin
sealed class ConfigValue<Value> {
    class Boolean(val value: kotlin.Boolean) : ConfigValue<kotlin.Boolean>()
    class Long(val value: kotlin.Long) : ConfigValue<kotlin.Long>()
    class Double(val value: kotlin.Double) : ConfigValue<kotlin.Double>()
    class Text(val value: String) : ConfigValue<String>()
}
```

### Next Steps

- Check the [comprehensive setup guide](#setup) for detailed configuration
- Learn about [architecture](#architecture) and core concepts
- Explore [usage examples](#usage) for common scenarios



## Define remote config service factory

The idea of **Proteus** that it must be scalable in the future and support multiple config providers. Since there is a chance that at some
day product owner will come to you and ask to integrate Firebase Remote Config & CleverTap parallelly :)

For now the only supported remote config provider is **Firebase**. That's why we have already added factory for you, the only thing which
you need to do is to register it during initialization.

```kotlin  
Proteus.Builder(this)
    ...
    .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
    ...
```

Notice, that **FirebaseOnlyProviderFactory**   is placed inside separate library module

```
dependencies {  
  ...
  implementation("io.github.maxim-petlyuk:proteus-firebase")
  ...
 }
 ```  

## Architecture

Proteus is built with the following architectural components:

- **Proteus**: Central singleton that coordinates the system
- **FeatureConfigProvider**: Interface for retrieving typed configuration values
- **FeatureContext/Feature**: Interface and implementation for defining features
- **MockConfigProvider**: Provides locally-overridden configurations
- **FirebaseFeatureConfigProvider**: Implementation for Firebase Remote Config
- **FeatureBookActivity**: UI for browsing and configuring features

## License

[Include license information here]# Proteus

Proteus is a feature flag and remote configuration management system for Android applications. It provides a flexible framework for
defining, accessing, and overriding feature configurations.
