# Proteus Coroutines Migration Plan

## Overview

This plan outlines the migration of Proteus FeatureConfigProvider to use suspend functions, making the architecture coroutines-friendly while maintaining the current SharedPreferences-based storage implementation. This migration prepares the codebase for future DataStore integration.

## Current Architecture Analysis

### FeatureConfigProvider (Current)
```kotlin
interface FeatureConfigProvider {
    fun getBoolean(featureKey: String): Boolean
    fun getString(featureKey: String): String
    fun getLong(featureKey: String): Long
    fun getDouble(featureKey: String): Double
}
```

### MockConfigRepository (Current)
```kotlin
interface MockConfigRepository {
    fun getMockedConfigValue(featureKey: String, typeClass: KClass<*>): ConfigValue<*>?
    fun save(featureKey: String, typeClass: KClass<*>, configValue: ConfigValue<*>)
    fun remove(featureKey: String)
}
```

### MockConfigStorage (Current)
```kotlin
interface MockConfigStorage {
    fun contains(featureKey: String): Boolean
    fun getLong(featureKey: String): Long
    fun getDouble(featureKey: String): Double
    fun getString(featureKey: String): String
    fun getBoolean(featureKey: String): Boolean
    fun save(featureKey: String, value: Long)
    fun save(featureKey: String, value: Double)
    fun save(featureKey: String, value: String)
    fun save(featureKey: String, value: Boolean)
    fun remove(featureKey: String)
    fun clear()
}
```

## Target Architecture

### FeatureConfigProvider (Target)
```kotlin
interface FeatureConfigProvider {
    suspend fun getBoolean(featureKey: String): Boolean
    suspend fun getString(featureKey: String): String
    suspend fun getLong(featureKey: String): Long
    suspend fun getDouble(featureKey: String): Double
}
```

### MockConfigRepository (Target)
```kotlin
interface MockConfigRepository {
    suspend fun getMockedConfigValue(featureKey: String, typeClass: KClass<*>): ConfigValue<*>?
    suspend fun save(featureKey: String, typeClass: KClass<*>, configValue: ConfigValue<*>)
    suspend fun remove(featureKey: String)
}
```

### MockConfigStorage (Target)
```kotlin
interface MockConfigStorage {
    suspend fun contains(featureKey: String): Boolean
    suspend fun getLong(featureKey: String): Long
    suspend fun getDouble(featureKey: String): Double
    suspend fun getString(featureKey: String): String
    suspend fun getBoolean(featureKey: String): Boolean
    suspend fun save(featureKey: String, value: Long)
    suspend fun save(featureKey: String, value: Double)
    suspend fun save(featureKey: String, value: String)
    suspend fun save(featureKey: String, value: Boolean)
    suspend fun remove(featureKey: String)
    suspend fun clear()
}
```

## Migration Steps

### Phase 1: Core Interface Migration (Day 1)

#### Step 1.1: Update FeatureConfigProvider Interface
**File**: `proteus-core/src/main/java/io/proteus/core/provider/FeatureConfigProvider.kt`

**Changes**:
```kotlin
interface FeatureConfigProvider {
    suspend fun getBoolean(featureKey: String): Boolean
    suspend fun getString(featureKey: String): String
    suspend fun getLong(featureKey: String): Long
    suspend fun getDouble(featureKey: String): Double
}
```

**Impact**: This is a breaking change that will require updates to all implementations.

#### Step 1.2: Update MockConfigRepository Interface
**File**: `proteus-core/src/main/java/io/proteus/core/data/MockConfigRepository.kt`

**Changes**:
```kotlin
interface MockConfigRepository {
    @Throws(IllegalConfigDataTypeException::class)
    suspend fun getMockedConfigValue(featureKey: String, typeClass: KClass<*>): ConfigValue<*>?

    suspend fun save(featureKey: String, typeClass: KClass<*>, configValue: ConfigValue<*>)

    suspend fun remove(featureKey: String)
}
```

#### Step 1.3: Update MockConfigStorage Interface
**File**: `proteus-core/src/main/java/io/proteus/core/data/MockConfigStorage.kt`

**Changes**:
```kotlin
interface MockConfigStorage {
    suspend fun contains(featureKey: String): Boolean
    suspend fun getLong(featureKey: String): Long
    suspend fun getDouble(featureKey: String): Double
    suspend fun getString(featureKey: String): String
    suspend fun getBoolean(featureKey: String): Boolean
    suspend fun save(featureKey: String, value: Long)
    suspend fun save(featureKey: String, value: Double)
    suspend fun save(featureKey: String, value: String)
    suspend fun save(featureKey: String, value: Boolean)
    suspend fun remove(featureKey: String)
    suspend fun clear()
}
```

### Phase 2: Implementation Updates (Day 1-2)

#### Step 2.1: Update MockConfigRepositoryImpl
**File**: `proteus-core/src/main/java/io/proteus/core/data/MockConfigRepositoryImpl.kt`

**Changes**:
- Add suspend modifiers to all functions
- Wrap SharedPreferences calls with `withContext(Dispatchers.IO)`
- Update exception handling for coroutines

**Example**:
```kotlin
internal class MockConfigRepositoryImpl(
    private val mockConfigStorage: MockConfigStorage
) : MockConfigRepository {

    @Throws(IllegalConfigDataTypeException::class)
    override suspend fun getMockedConfigValue(featureKey: String, typeClass: KClass<*>): ConfigValue<*>? {
        if (!mockConfigStorage.contains(featureKey)) {
            return null
        }

        return when (typeClass) {
            Int::class,
            Long::class -> {
                val localValue = mockConfigStorage.getLong(featureKey)
                ConfigValue.Long(localValue)
            }
            // ... rest of implementation with suspend calls
        }
    }

    override suspend fun save(featureKey: String, typeClass: KClass<*>, configValue: ConfigValue<*>) {
        // Implementation with suspend calls
    }

    override suspend fun remove(featureKey: String) {
        mockConfigStorage.remove(featureKey)
    }
}
```

#### Step 2.2: Update PreferenceMockConfigStorage
**File**: `proteus-core/src/main/java/io/proteus/core/data/PreferenceMockConfigStorage.kt`

**Changes**:
- Add suspend modifiers to all functions
- Wrap SharedPreferences operations with `withContext(Dispatchers.IO)`
- Ensure thread-safe operations

**Example**:
```kotlin
internal class PreferenceMockConfigStorage(
    private val context: Context
) : MockConfigStorage {

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    override suspend fun contains(featureKey: String): Boolean = withContext(Dispatchers.IO) {
        preferences.contains(featureKey)
    }

    override suspend fun getLong(featureKey: String): Long = withContext(Dispatchers.IO) {
        preferences.getLong(featureKey, 0L)
    }

    override suspend fun save(featureKey: String, value: Long) = withContext(Dispatchers.IO) {
        preferences.edit().putLong(featureKey, value).apply()
    }

    // ... rest of implementation
}
```

### Phase 3: Provider Implementations (Day 2)

#### Step 3.1: Update FeatureConfigProviderImpl
**File**: `proteus-core/src/main/java/io/proteus/core/provider/FeatureConfigProviderImpl.kt`

**Changes**:
- Add suspend modifiers
- Update mock provider calls
- Maintain fallback logic with suspend functions

#### Step 3.2: Update MockConfigProvider
**File**: `proteus-core/src/main/java/io/proteus/core/provider/MockConfigProvider.kt`

**Changes**:
- Add suspend modifiers to all methods
- Update repository calls

### Phase 4: Firebase Provider Updates (Day 2)

#### Step 4.1: Update FirebaseFeatureConfigProvider
**File**: `proteus-firebase/src/main/java/io/proteus/firebase/FirebaseFeatureConfigProvider.kt`

**Changes**:
- Add suspend modifiers
- Wrap Firebase calls with appropriate coroutine context
- Use Firebase async APIs where available

**Example**:
```kotlin
internal class FirebaseFeatureConfigProvider : FeatureConfigProvider {

    override suspend fun getBoolean(featureKey: String): Boolean = withContext(Dispatchers.IO) {
        FirebaseRemoteConfig.getInstance().getBoolean(featureKey)
    }

    override suspend fun getString(featureKey: String): String = withContext(Dispatchers.IO) {
        FirebaseRemoteConfig.getInstance().getString(featureKey)
    }

    // ... rest of implementation
}
```

### Phase 5: UI Layer Updates (Day 3)

#### Step 5.1: Update FeatureCatalogViewModel
**File**: `proteus-ui/src/main/java/io/proteus/ui/presentation/catalog/FeatureCatalogViewModel.kt`

**Changes**:
- Use ViewModelScope for coroutines
- Update config provider calls to use suspend functions
- Handle loading states properly

#### Step 5.2: Update UI Components
**Files**: All Compose screens and components using FeatureConfigProvider

**Changes**:
- Use LaunchedEffect for config loading
- Handle suspend function calls properly
- Update state management for async operations

### Phase 6: Test Updates (Day 3-4)

#### Step 6.1: Update Unit Tests
**Files**:
- `proteus-core/src/testDebug/java/io/proteus/core/data/FeatureMetadataMapperTest.kt`
- `proteus-core/src/testDebug/java/io/proteus/core/domain/FeatureMetadataTest.kt`
- `proteus-core/src/testDebug/java/io/proteus/core/data/StaticFeatureBookDataSourceTest.kt`
- `proteus-core/src/testDebug/java/io/proteus/core/domain/FeatureTest.kt`
- `proteus-core/src/testDebug/java/io/proteus/core/domain/ConfigValueTest.kt`

**Changes**:
- Add `kotlinx-coroutines-test` dependency
- Use `runTest` for suspend function testing
- Update test assertions for async operations

**Example**:
```kotlin
@Test
fun `test getBoolean returns correct value`() = runTest {
    // Given
    val provider = mockProvider

    // When
    val result = provider.getBoolean("test_key")

    // Then
    assertEquals(true, result)
}
```

#### Step 6.2: Update Integration Tests
**File**: `proteus-core/src/androidTest/java/io/proteus/core/data/AssetsFeatureBookDataSourceTest.kt`

**Changes**:
- Update for async operations
- Use proper coroutine test context

### Phase 7: Synchronous Compatibility Layer (Day 4)

#### Step 7.1: Create Synchronous Wrapper
**File**: `proteus-core/src/main/java/io/proteus/core/provider/SynchronousFeatureConfigProvider.kt`

**Implementation**:
```kotlin
class SynchronousFeatureConfigProvider(
    private val suspendProvider: FeatureConfigProvider,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
    private val timeout: Duration = 30.seconds
) {

    fun getBoolean(featureKey: String): Boolean {
        return runBlocking(context = coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getBoolean(featureKey)
            }
        }
    }

    fun getString(featureKey: String): String {
        return runBlocking(context = coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getString(featureKey)
            }
        }
    }

    fun getLong(featureKey: String): Long {
        return runBlocking(context = coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getLong(featureKey)
            }
        }
    }

    fun getDouble(featureKey: String): Double {
        return runBlocking(context = coroutineContext) {
            withTimeout(timeout) {
                suspendProvider.getDouble(featureKey)
            }
        }
    }
}
```

#### Step 7.2: Update Proteus Factory
**File**: Update main Proteus factory class

**Add Method**:
```kotlin
fun buildSynchronousConfigProvider(): SynchronousFeatureConfigProvider {
    val suspendProvider = buildConfigProvider()
    return SynchronousFeatureConfigProvider(suspendProvider)
}
```

#### Step 7.3: Create Tests
**File**: `proteus-core/src/testDebug/java/io/proteus/core/provider/SynchronousFeatureConfigProviderTest.kt`

**Changes**:
- Test all synchronous methods
- Test timeout handling
- Test custom coroutine context
- Verify proper delegation to suspend provider

### Phase 8: Documentation Updates (Day 4)

#### Step 8.1: Update Module READMEs
**Files**:
- `proteus-core/README.md`
- `proteus-firebase/README.md`
- `proteus-ui/README.md`

**Changes**:
- Update code examples to show both suspend and synchronous usage
- Add coroutine usage patterns
- Update best practices section

**Example**:
```kotlin
// Async usage (recommended for UI/coroutine contexts)
class MyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val provider = Proteus.getInstance().buildConfigProvider()
            val isEnabled = provider.getBoolean("feature_flag")
            // Use configuration value
        }
    }
}

// Synchronous usage (for non-coroutine contexts)
val syncProvider = Proteus.getInstance().buildSynchronousConfigProvider()
val isEnabled = syncProvider.getBoolean("feature_flag")
```

#### Step 8.2: Update Architecture Documentation
**File**: `docs/ARCHITECTURE.md`

**Changes**:
- Update sequence diagrams for both async and sync operations
- Add coroutine best practices
- Document both API patterns
- Update configuration lifecycle documentation

### Phase 9: Dependency Updates (Day 4)

#### Step 9.1: Update Build Dependencies
**Files**:
- `proteus-core/build.gradle.kts`
- `proteus-firebase/build.gradle.kts`
- `proteus-ui/build.gradle.kts`

**Changes**:
- Add `kotlinx-coroutines-android` if not already present
- Add `kotlinx-coroutines-test` for testing
- Update Kotlin coroutines to latest stable version

**Example**:
```kotlin
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}
```

## Breaking Changes and Migration Guide

### Breaking Changes
1. **FeatureConfigProvider**: All methods now require suspend context
2. **MockConfigRepository**: All methods are now suspend functions
3. **MockConfigStorage**: All methods are now suspend functions

### Migration Guide for Users

#### Before (Blocking)
```kotlin
val provider = Proteus.getInstance().buildConfigProvider()
val isEnabled = provider.getBoolean("feature_flag")
```

#### After (Suspend)
```kotlin
val provider = Proteus.getInstance().buildConfigProvider()
val isEnabled = provider.getBoolean("feature_flag") // Must be called from suspend context
```

#### After (Synchronous Compatibility)
```kotlin
val syncProvider = Proteus.getInstance().buildSynchronousConfigProvider()
val isEnabled = syncProvider.getBoolean("feature_flag") // Can be called from any context
```

#### In Activity/Fragment
```kotlin
// Option 1: Using lifecycleScope
lifecycleScope.launch {
    val isEnabled = provider.getBoolean("feature_flag")
}

// Option 2: Using viewModel
class MyViewModel : ViewModel() {
    fun loadConfig() {
        viewModelScope.launch {
            val isEnabled = provider.getBoolean("feature_flag")
        }
    }
}

// Option 3: Using synchronous API (legacy compatibility)
val syncProvider = Proteus.getInstance().buildSynchronousConfigProvider()
val isEnabled = syncProvider.getBoolean("feature_flag")
```

#### In Compose
```kotlin
@Composable
fun MyScreen(provider: FeatureConfigProvider) {
    var isEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isEnabled = provider.getBoolean("feature_flag")
    }

    if (isEnabled) {
        // Show feature
    }
}
```

## Testing Strategy

### Unit Tests
- Use `runTest` for all suspend function tests
- Mock coroutine contexts appropriately
- Test error handling in async context

### Integration Tests
- Test end-to-end async flow
- Verify proper coroutine cancellation
- Test concurrent access scenarios

### Performance Tests
- Measure async operation performance
- Verify no blocking on main thread
- Test under high concurrency

## Risk Assessment and Mitigation

### High Risk
1. **Breaking Changes**: All existing code using FeatureConfigProvider will break
   - **Mitigation**: Provide synchronous compatibility layer and detailed migration guide
   - **Timeline**: Announce in advance, provide migration period with both APIs available

2. **Thread Safety**: SharedPreferences operations need proper synchronization
   - **Mitigation**: Use appropriate Dispatchers and synchronization
   - **Testing**: Comprehensive concurrent access tests

### Medium Risk
1. **Performance Impact**: Adding coroutine overhead
   - **Mitigation**: Benchmark before/after performance
   - **Monitoring**: Add performance tests

2. **Firebase Integration**: Firebase SDK async compatibility
   - **Mitigation**: Test Firebase async patterns thoroughly
   - **Fallback**: Keep synchronous wrappers if needed

### Low Risk
1. **UI Updates**: Compose integration complexity
   - **Mitigation**: Comprehensive UI testing
   - **Documentation**: Clear Compose usage patterns

## Success Criteria

### Functional
- [ ] All FeatureConfigProvider methods are suspend functions
- [ ] All existing functionality works with async operations
- [ ] No blocking operations on main thread
- [ ] Firebase provider works correctly with async calls

### Performance
- [ ] No significant performance degradation
- [ ] Improved UI responsiveness
- [ ] Proper resource cleanup

### Code Quality
- [ ] All tests pass with async operations
- [ ] Documentation updated for async patterns
- [ ] Code follows Kotlin coroutine best practices

## Timeline

| Phase | Duration | Dependencies |
|-------|----------|--------------|
| **Phase 1**: Core Interfaces | 4 hours | None |
| **Phase 2**: Implementation Updates | 1 day | Phase 1 |
| **Phase 3**: Provider Updates | 6 hours | Phase 2 |
| **Phase 4**: Firebase Updates | 4 hours | Phase 3 |
| **Phase 5**: UI Updates | 1 day | Phase 4 |
| **Phase 6**: Test Updates | 1.5 days | Phase 5 |
| **Phase 7**: Synchronous Compatibility Layer | 4 hours | Phase 6 |
| **Phase 8**: Documentation | 4 hours | Phase 7 |
| **Phase 9**: Dependencies | 2 hours | Phase 8 |

**Total Estimated Time**: 4-5 days

## Post-Migration Benefits

### Immediate Benefits
- Non-blocking configuration access
- Better UI responsiveness
- Consistent async patterns

### Future Benefits
- Easy DataStore integration
- Better testing capabilities
- Improved scalability
- More efficient resource usage

## Future Considerations

### DataStore Migration (Future Phase)
Once coroutines are in place, migrating to DataStore becomes straightforward:
1. Replace `PreferenceMockConfigStorage` implementation
2. Use DataStore's suspend APIs directly
3. Minimal changes to calling code due to suspend functions already in place

### Reactive Programming
Coroutines foundation enables future reactive features:
- Flow-based configuration updates
- Real-time configuration changes
- Reactive UI updates

---

**Document Version**: 1.0
**Created**: December 30, 2024
**Status**: Ready for Implementation