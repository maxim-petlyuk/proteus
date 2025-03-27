
# Proteus

Proteus is a feature flag and remote configuration management system for Android applications. It provides a flexible framework for defining, accessing, and overriding feature configurations.

## Overview

Proteus simplifies feature flag management in Android applications by providing:

- Type-safe access to remote configurations
- Local overrides for testing and development
- A UI for browsing and configuring features
- Integration with Firebase Remote Config

## Features

- **Type Safety**: Access boolean, string, long, and double configuration values with type checking
- **Remote Configuration**: Integrate with Firebase Remote Config for cloud-managed feature flags
- **Mock Config System**: Override remote configurations locally for testing
- **Feature Catalog UI**: Browse and modify feature configurations during development
- **JSON-based Feature Definitions**: Define features in a simple JSON format

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

There are 2 ways how you can define information about your feature flags in the application for Proteus library: via json file in assets folder or just simple static collection of objects.

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
**feature_key** -  key in your remote config service (for now only **Firebase Remote Config** is supported)
**default_value** - here could be some default local value which you may propagate to remote config service to use as default one (library is not using it), but to support strong type-safe access - this value should have the same type as **value_type**
**value_type** - describe type of the remote config value and could be one of the next types: *text/long/boolean/double*
**service_owner** - this field describe the name of your remote config service, in case if your application is using 2 providers of the remote config (e.g. CleverTap & Firebase Remote Config), it will help to disctint who should take care about this particular remote config

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
                valueClass = String::class,  
                owner = "firebase"  
            ),  
            Feature(  
                key = "optional_server",  
                defaultValue = "https://test.com",  
                valueClass = String::class,  
                owner = "firebase"  
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

The idea of **Proteus** that it must be scalable in the future and support multiple config providers. Since there is a chance that at some day product owner will come to you and ask to integrate Firebase Remote Config & CleverTap parallelly :)

For now the only supported remote config provider is **Firebase**. That's why we have already added factory for you, the only thing which you need to do is to register it during initialization.

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
class MyViewModel {  
 
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

Proteus is a feature flag and remote configuration management system for Android applications. It provides a flexible framework for defining, accessing, and overriding feature configurations.


## Features

- **Type Safety**: Access boolean, string, long, and double configuration values with type checking
- **Remote Configuration**: Integrate with Firebase Remote Config for cloud-managed feature flags
- **Mock Config System**: Override remote configurations locally for testing
- **Feature Catalog UI**: Browse and modify feature configurations during development
- **JSON-based Feature Definitions**: Define features in a simple JSON format  
  