![proteus](https://raw.githubusercontent.com/maxim-petlyuk/proteus/refs/heads/main/assets/banner.png)

<p align="center">
    <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg" alt="License"></a>
    <a href="https://android-arsenal.com/api?level=23"><img src="https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat" alt="API"></a>
    <a href="https://github.com/maxim-petlyuk/proteus-config/actions"><img src="https://img.shields.io/github/actions/workflow/status/maxim-petlyuk/proteus/release-bom-only.yml?branch=main" alt="Build Status"></a>
    <a href="https://maxim-petlyuk.github.io/proteus-config"><img src="https://img.shields.io/badge/docs-latest-brightgreen.svg" alt="Documentation"></a>
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

## Installation

Add the Proteus dependencies to your project:

```kotlin  
// In your root build.gradle.kts  
dependencies {
    implementation("io.proteus:proteus-core:1.0.0")   // Core functionality  
    implementation("io.proteus:proteus-firebase:1.0.0")   // Firebase Remote Config integration  
    implementation("io.proteus:proteus-ui:1.0.0")    // UI components for feature discovery and configuration
}  
```  

## Setup

### 1. Initialize Proteus

Initialize Proteus in your Application class:

```kotlin  
class MainApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase SDK before initializing Proteus  
        FirebaseApp.initializeApp(this)

        val config = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(60)
            .build()
        config.setConfigSettingsAsync(configSettings)
        config.fetchAndActivate()

        Proteus.Builder(this)
            .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
            .registerFeatureBookDataSource(
                AssetsFeatureBookDataSource(
                    context = this,
                    jsonFilePath = "featurebook.json"
                )
            )
            .build()
    }
}  
```  

### 2. Define Your Features

There are 2 ways how you can define information about your feature flags in the application for Proteus library: via json file in assets
folder or just simple static collection of objects.

- #### json file in assets folder

```
    Proteus.Builder(this)
         ...  
        .registerFeatureBookDataSource(  
            AssetsFeatureBookDataSource(  
                context = this,  
                jsonFilePath = "featurebook.json"  
            )  
        )  
        ...
```

```json  
[
  {
    "feature_key": "primary_server",
    "default_value": "https://api.example.com",
    "value_type": "text",
    "service_owner": "firebase"
  },
  {
    "feature_key": "max_items_per_page",
    "default_value": "25",
    "value_type": "long",
    "service_owner": "firebase"
  },
  {
    "feature_key": "animation_threshold",
    "default_value": "0.5",
    "value_type": "double",
    "service_owner": "firebase"
  },
  {
    "feature_key": "dark_mode_enabled",
    "default_value": "true",
    "value_type": "boolean",
    "service_owner": "firebase"
  }
]
``` 

**feature_key** - key in your remote config service (for now only **Firebase Remote Config** is supported)
**default_value** - here could be some default local value which you may propagate to remote config service to use as default one (library
is not using it), but to support strong type-safe access - this value should have the same type as **value_type**
**value_type** - describe type of the remote config value and could be one of the next types: *text/long/boolean/double*

- #### static collection through kotlin code

 ```
   Proteus.Builder(this)
      ...  
      registerFeatureBookDataSource(provideStaticFeatureDataSource())
      ...
```

 ```kotlin 
 private fun provideStaticFeatureDataSource(): FeatureBookDataSource {
    return StaticFeatureBookDataSource(
        listOf(
            Feature(
                key = "primary_server",
                defaultValue = "https://google.com",
                valueClass = String::class
            ),
            Feature(
                key = "optional_server",
                defaultValue = "https://test.com",
                valueClass = String::class
            )
        )
    )
}

private class StaticFeatureBookDataSource(private val featureBook: List<FeatureContext<*>>) : FeatureBookDataSource {

    override suspend fun getFeatureBook(): Result<List<FeatureContext<*>>> {
        return Result.success(featureBook)
    }
}
 ```

### 3. Define remote config service factory

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
  implementation("io.proteus:proteus-firebase:1.0.0")
  ...
 }
 ```  

## Usage

### Reading Feature Values

Access feature values through the `FeatureConfigProvider`:

```kotlin  
class YourViewModel(private val featureConfigProvider: FeatureConfigProvider) : ViewModel() {

    fun getFeatureValues() {
        val primaryServer = featureConfigProvider.getString("primary_server")
        val maxItemsPerPage = featureConfigProvider.getLong("max_items_per_page")
        val animationThreshold = featureConfigProvider.getDouble("animation_threshold")
        val darkModeEnabled = featureConfigProvider.getBoolean("dark_mode_enabled")
    }
}  
```  

### Launching the Feature Catalog UI

Launch the feature catalog UI to view and modify feature flags during development:

```kotlin  
class MainActivity : AppCompatActivity() {

    private fun openFeatureCatalog() {
        startActivity(Intent(this, FeatureBookActivity::class.java))
    }
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

## Modules

- **proteus-core**: Core functionality and interfaces
- **proteus-firebase**: Firebase Remote Config integration
- **proteus-ui**: UI components for feature discovery and configuration

## License

[Include license information here]# Proteus

Proteus is a feature flag and remote configuration management system for Android applications. It provides a flexible framework for
defining, accessing, and overriding feature configurations.
