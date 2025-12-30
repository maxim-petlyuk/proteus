# Proteus Firebase

Firebase Remote Config provider implementation for Proteus.

## Overview

The `proteus-firebase` module seamlessly integrates Firebase Remote Config with Proteus, providing automatic value fetching, type mapping, and fallback mechanisms.

## Prerequisites

⚠️ **Important**: Firebase must be initialized in your application before using this module.

```kotlin
// In your Application class or before Proteus initialization
FirebaseApp.initializeApp(this)
```

## Installation

```kotlin
dependencies {
    implementation("io.github.maxim-petlyuk:proteus-firebase:$version")

    // Firebase Remote Config dependency
    implementation("com.google.firebase:firebase-config:$firebaseVersion")
}
```

Or using the BOM (recommended):

```kotlin
dependencies {
    implementation(platform("io.github.maxim-petlyuk:proteus-bom:$version"))
    implementation("io.github.maxim-petlyuk:proteus-core")
    implementation("io.github.maxim-petlyuk:proteus-firebase")

    // Firebase Remote Config still needs to be added separately
    implementation("com.google.firebase:firebase-config:$firebaseVersion")
}
```

## Setup

### 1. Add Firebase to Your Project

Follow the [official Firebase setup guide](https://firebase.google.com/docs/android/setup):
1. Add `google-services.json` to your app module
2. Apply the Google Services plugin
3. Initialize Firebase in your Application class

### 2. Register Firebase Provider

```kotlin
class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase first
        FirebaseApp.initializeApp(this)

        // Setup Proteus with Firebase provider
        Proteus.Builder(this)
            .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
            .registerFeatureBookDataSource(
                AssetsFeatureBookDataSource(this, "features.json")
            )
            .build()
    }
}
```

## How It Works

### FirebaseOnlyProviderFactory

Creates a Firebase-backed configuration provider that:
- Automatically fetches values from Firebase Remote Config
- Maps Firebase values to Proteus `ConfigValue` types
- Provides type-safe access through `FeatureConfigProvider` interface

### FirebaseFeatureConfigProvider

The internal implementation that:
- Retrieves values from `FirebaseRemoteConfig.getInstance()`
- Handles type conversion (boolean, long, double, string)
- Falls back to default values when keys don't exist
- Integrates with MockConfigProvider for testing overrides

## Configuration in Firebase Console

1. Open [Firebase Console](https://console.firebase.google.com)
2. Navigate to Remote Config
3. Create parameters matching your feature keys:

| Parameter Key | Value Type | Example Value |
|--------------|------------|---------------|
| `show_tutorial` | Boolean | `true` |
| `max_retries` | Number | `3` |
| `api_endpoint` | String | `"https://api.example.com"` |
| `threshold` | Number | `0.75` |

## Value Type Mapping

| Proteus Type | Firebase Type | Notes |
|--------------|---------------|-------|
| `ConfigValue.Boolean` | Boolean | Direct mapping |
| `ConfigValue.Long` | Number | Converted to Long |
| `ConfigValue.Double` | Number | Converted to Double |
| `ConfigValue.Text` | String | Direct mapping |

## Usage Example

```kotlin
// After initialization, use the same API as any Proteus provider
val provider = Proteus.getInstance().buildConfigProvider()

// Values are fetched from Firebase Remote Config
val isEnabled = provider.getBoolean("feature_flag")
val timeout = provider.getLong("request_timeout")
val endpoint = provider.getString("api_endpoint")
val rate = provider.getDouble("sampling_rate")
```

## Migration from Direct Firebase Usage

If you're currently using Firebase Remote Config directly:

### Before (Direct Firebase)
```kotlin
val remoteConfig = FirebaseRemoteConfig.getInstance()
val isEnabled = remoteConfig.getBoolean("feature_flag")
```

### After (With Proteus)
```kotlin
val provider = Proteus.getInstance().buildConfigProvider()
val isEnabled = provider.getBoolean("feature_flag")
```

Benefits of migration:
- Runtime override capability for testing
- Type-safe configuration access
- Consistent API across different providers
- Built-in UI for configuration management

## Troubleshooting

### Common Issues

1. **"Firebase not initialized" error**
   - Ensure `FirebaseApp.initializeApp(context)` is called before Proteus initialization
   - Check that `google-services.json` is in the correct location

2. **Values not updating**
   - Check Firebase Console for the correct parameter keys
   - Verify fetch is succeeding (check logcat)
   - Ensure minimum fetch interval hasn't been exceeded

3. **Type mismatch errors**
   - Verify Firebase parameter types match expected Proteus types
   - Use the correct getter method for each value type

### Debug Logging

Enable Firebase debug logging:
```kotlin
FirebaseRemoteConfig.getInstance().setConfigSettingsAsync(
    remoteConfigSettings {
        minimumFetchIntervalInSeconds = 0 // For development only
    }
)
```

## Best Practices

1. **Initialize Firebase first**: Always before Proteus initialization
2. **Use consistent keys**: Match Firebase parameter keys with feature definitions
3. **Set reasonable defaults**: Define fallback values in feature definitions
4. **Test with overrides**: Use Proteus UI to test different configurations
5. **Monitor fetch failures**: Add logging for fetch/activation failures

## License

```
Copyright 2025 Maxim Petlyuk

Licensed under the Apache License, Version 2.0
```