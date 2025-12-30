# Proteus Core

Core abstraction layer for A/B testing and remote configuration in Android applications.

## Overview

The `proteus-core` module provides the foundational architecture for configuration management, including provider abstractions, type-safe value wrappers, and runtime override capabilities through mock providers.

## Installation

```kotlin
dependencies {
    implementation("io.github.maxim-petlyuk:proteus-core:$version")
}
```

Or using the BOM (recommended):

```kotlin
dependencies {
    implementation(platform("io.github.maxim-petlyuk:proteus-bom:$version"))
    implementation("io.github.maxim-petlyuk:proteus-core")
}
```

## Key Components

### Proteus
The main singleton coordinator that manages the entire configuration system.

```kotlin
// Initialize Proteus
Proteus.Builder(context)
    .registerConfigProviderFactory(factory)
    .registerFeatureBookDataSource(dataSource)
    .build()

// Get the singleton instance
val proteus = Proteus.getInstance()
```

### FeatureConfigProvider
The main interface for accessing configuration values with type safety.

```kotlin
interface FeatureConfigProvider {
    suspend fun getBoolean(featureKey: String): Boolean
    suspend fun getString(featureKey: String): String
    suspend fun getLong(featureKey: String): Long
    suspend fun getDouble(featureKey: String): Double
}
```

### ConfigValue
Type-safe value wrapper supporting multiple data types:

```kotlin
sealed class ConfigValue {
    data class Boolean(val value: kotlin.Boolean) : ConfigValue()
    data class Long(val value: kotlin.Long) : ConfigValue()
    data class Double(val value: kotlin.Double) : ConfigValue()
    data class Text(val value: String) : ConfigValue()
}
```

### Feature & FeatureContext
Define your features with metadata and context:

```kotlin
data class Feature<DataType : Any>(
    override val key: String,
    override val defaultValue: DataType,
    override val valueClass: KClass<DataType>,
) : FeatureContext<DataType>

interface FeatureContext<DataType : Any> {
    val key: String
    val defaultValue: DataType
    val valueClass: KClass<DataType>
}
```

### MockConfigProvider
Enables runtime configuration overrides for testing:

```kotlin
// Values are automatically persisted in SharedPreferences
// and take precedence over remote providers
val mockProvider = MockConfigProvider(repository)
```

### FeatureBookDataSource
Sources for feature definitions - you can load features from JSON files or define them programmatically:

#### Option 1: Load from Assets (JSON File)

Create `features.json` in your `assets` folder:

```json
[
  {
    "feature_key": "primary_server",
    "default_value": "https://google.com",
    "value_type": "text"
  },
  {
    "feature_key": "optional_server",
    "default_value": "https://test.com",
    "value_type": "text"
  },
  {
    "feature_key": "max_group_chat_size",
    "default_value": "25",
    "value_type": "long"
  }
]
```

Then load it using `AssetsFeatureBookDataSource`:

```kotlin
val dataSource = AssetsFeatureBookDataSource(context, "features.json")
```

#### Option 2: Define in Code

For more control or dynamic feature definitions:

```kotlin
val dataSource = object : FeatureBookDataSource {
    override fun getFeatureBook(): Result<List<FeatureContext<*>>> {
        return Result.success(
            listOf(
                Feature(
                    key = "primary_server",
                    defaultValue = "https://google.com",
                    valueClass = String::class
                ),
                Feature(
                    key = "max_group_chat_size",
                    defaultValue = 25L,
                    valueClass = Long::class
                )
            )
        )
    }
}
```

## Usage Examples

### Basic Configuration Access (Async - Recommended)

```kotlin
// In Activity/Fragment using lifecycleScope
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val provider = Proteus.getInstance().buildConfigProvider()

            // All config access must be in suspend context
            val isFeatureEnabled = provider.getBoolean("new_feature_flag")
            val maxRetries = provider.getLong("max_retry_count")
            val apiEndpoint = provider.getString("api_endpoint")
            val threshold = provider.getDouble("confidence_threshold")

            // Use configuration values...
        }
    }
}
```

```kotlin
// In ViewModel using viewModelScope
class MyViewModel : ViewModel() {
    private val _config = MutableLiveData<AppConfig>()
    val config: LiveData<AppConfig> = _config

    fun loadConfiguration() {
        viewModelScope.launch {
            val provider = Proteus.getInstance().buildConfigProvider()
            val appConfig = AppConfig(
                isEnabled = provider.getBoolean("feature_enabled"),
                timeout = provider.getLong("request_timeout"),
                endpoint = provider.getString("api_endpoint")
            )
            _config.value = appConfig
        }
    }
}
```

### Synchronous Usage (Legacy Compatibility)

```kotlin
// For non-coroutine contexts or legacy compatibility
val syncProvider = Proteus.getInstance().buildSynchronousConfigProvider()

// Can be called from any context (blocks calling thread)
val isFeatureEnabled = syncProvider.getBoolean("new_feature_flag")
val maxRetries = syncProvider.getLong("max_retry_count")
val apiEndpoint = syncProvider.getString("api_endpoint")
val threshold = syncProvider.getDouble("confidence_threshold")
```

### Compose Usage

```kotlin
@Composable
fun FeatureScreen() {
    var isEnabled by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            val provider = Proteus.getInstance().buildConfigProvider()
            isEnabled = provider.getBoolean("new_feature_enabled")
        } catch (e: Exception) {
            // Handle error
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> CircularProgressIndicator()
        isEnabled -> NewFeatureContent()
        else -> LegacyContent()
    }
}
```

### Runtime Override Flow

1. **FeatureConfigProviderImpl** checks MockConfigProvider first
2. If no override exists, falls back to the remote provider
3. Overrides persist across app sessions via SharedPreferences
4. Clear overrides by removing them through the UI or programmatically

## Error Handling

The module provides custom exceptions for better error handling:

- `IllegalConfigDataTypeException` - Thrown when config value type doesn't match expected type
- `IllegalConfigOwnerException` - Thrown when accessing config from wrong owner/context
- `MockConfigUnavailableException` - Thrown when mock provider is not available

```kotlin
try {
    val value = provider.getBoolean("feature_key")
} catch (e: IllegalConfigDataTypeException) {
    // Handle type mismatch
    Log.e("Config", "Expected boolean but got ${e.actualType}")
}
```

## Dependencies

- Android SDK 23+
- Kotlin Coroutines
- Kotlin Serialization
- AndroidX Core

## ProGuard / R8

ProGuard rules are automatically included with the library. No additional configuration needed.

## Integration with Other Modules

- **proteus-firebase**: Provides Firebase Remote Config implementation
- **proteus-ui**: Provides Material Design UI for runtime overrides
- **proteus-bom**: Manages versions across all modules

## Best Practices

1. **Initialize early**: Set up Proteus in your Application class
2. **Define features upfront**: Use JSON files or code-based definitions
3. **Type safety**: Always use the appropriate getter for your value type
4. **Use coroutines**: Prefer suspend functions with proper lifecycle scopes
5. **Handle errors**: Wrap config calls in try-catch for robust error handling
6. **Testing**: Leverage MockConfigProvider for testing different configurations
7. **Sync API sparingly**: Use synchronous API only for legacy compatibility
8. **Production**: Consider disabling override UI in release builds
9. **Performance**: Use viewModelScope and lifecycleScope for automatic cancellation

## License

```
Copyright 2025 Maxim Petlyuk

Licensed under the Apache License, Version 2.0
```