# Proteus KMP + CMP Migration Plan

> **Version**: 1.0
> **Date**: January 2025
> **Status**: Draft
> **Author**: Proteus Team

---

## Executive Summary

### Vision
Transform Proteus from an Android-only library into a true cross-platform solution using Kotlin Multiplatform (KMP) and Compose Multiplatform (CMP), enabling iOS, Android, and potentially desktop teams to test remote configurations without admin access.

### Strategic Goals
1. **Expand market reach**: First cross-platform remote config override library
2. **Unify codebase**: Single source of truth for business logic
3. **Consistent UX**: Identical Material Design 3 experience across platforms
4. **Reduce maintenance**: One codebase instead of platform-specific implementations
5. **Enable new platforms**: Foundation for desktop and web support

### Expected Impact
- **iOS teams** gain same testing capabilities as Android teams
- **50-70% code sharing** between platforms
- **Single UI codebase** with Compose Multiplatform
- **Broader adoption** in cross-platform development teams
- **Pioneer position** in cross-platform config testing tools

---

## Technical Architecture

### Current Architecture (Android-only)
```
proteus/
├── proteus-core/        # Core abstractions (Android)
├── proteus-firebase/    # Firebase provider (Android)
├── proteus-ui/          # Compose UI (Android)
└── proteus-bom/         # BOM module
```

### Target KMP Architecture (In-Place Migration)
```
proteus/                          # Keep existing structure
├── proteus-core/                 # Convert to KMP module
│   ├── src/
│   │   ├── commonMain/           # Shared interfaces & logic
│   │   │   ├── kotlin/
│   │   │   │   └── io/proteus/core/
│   │   │   │       ├── domain/   # ConfigValue, Feature, FeatureContext
│   │   │   │       ├── provider/ # FeatureConfigProvider interfaces
│   │   │   │       └── data/     # MockConfigRepository, MockConfigStorage
│   │   ├── androidMain/          # Android-specific (minimal changes)
│   │   │   └── kotlin/
│   │   │       └── io/proteus/core/
│   │   │           └── data/     # DataStore Android implementation
│   │   └── iosMain/              # iOS-specific implementations
│   │       └── kotlin/
│   │           └── io/proteus/core/
│   │               └── data/     # DataStore iOS implementation
├── proteus-firebase/             # Convert to KMP module
│   ├── src/
│   │   ├── commonMain/           # Firebase provider interface
│   │   ├── androidMain/          # Android Firebase SDK
│   │   └── iosMain/              # iOS Firebase SDK
├── proteus-ui/                   # Convert to CMP module
│   └── src/
│       └── commonMain/           # Compose Multiplatform UI
└── proteus-bom/                  # Remains as-is
```

### Platform-Specific Components

#### Why DataStore Instead of SharedPreferences/NSUserDefaults

**Benefits of DataStore for KMP:**
1. **Single implementation** - Write once in commonMain, works everywhere
2. **Type safety** - Strongly typed with Preferences API
3. **Async/Flow-first** - Built for coroutines, no blocking I/O
4. **Data consistency** - Transactional updates with atomic reads/writes
5. **Migration support** - Automatic migration from SharedPreferences on Android
6. **Error handling** - Better exception handling than SharedPreferences
7. **Cross-platform** - Official Kotlin Multiplatform support

#### Storage Layer with DataStore (Cross-Platform)
```kotlin
// commonMain - Keep existing interface unchanged
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

// commonMain - DataStore implementation (shared across platforms!)
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class DataStoreMockConfigStorage(
    private val dataStore: DataStore<Preferences>
) : MockConfigStorage {

    override suspend fun contains(featureKey: String): Boolean {
        return dataStore.data.map { prefs ->
            prefs.contains(stringPreferencesKey(featureKey.asPreferenceKey()))
        }.first()
    }

    override suspend fun getString(featureKey: String): String {
        return dataStore.data.map { prefs ->
            prefs[stringPreferencesKey(featureKey.asPreferenceKey())] ?: ""
        }.first()
    }

    override suspend fun save(featureKey: String, value: String) {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(featureKey.asPreferenceKey())] = value
        }
    }

    override suspend fun getBoolean(featureKey: String): Boolean {
        return dataStore.data.map { prefs ->
            prefs[booleanPreferencesKey(featureKey.asPreferenceKey())] ?: false
        }.first()
    }

    override suspend fun save(featureKey: String, value: Boolean) {
        dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(featureKey.asPreferenceKey())] = value
        }
    }

    override suspend fun getLong(featureKey: String): Long {
        return dataStore.data.map { prefs ->
            prefs[longPreferencesKey(featureKey.asPreferenceKey())] ?: 0L
        }.first()
    }

    override suspend fun save(featureKey: String, value: Long) {
        dataStore.edit { prefs ->
            prefs[longPreferencesKey(featureKey.asPreferenceKey())] = value
        }
    }

    override suspend fun getDouble(featureKey: String): Double {
        return dataStore.data.map { prefs ->
            prefs[doublePreferencesKey(featureKey.asPreferenceKey())] ?: 0.0
        }.first()
    }

    override suspend fun save(featureKey: String, value: Double) {
        dataStore.edit { prefs ->
            prefs[doublePreferencesKey(featureKey.asPreferenceKey())] = value
        }
    }

    override suspend fun remove(featureKey: String) {
        dataStore.edit { prefs ->
            prefs.remove(stringPreferencesKey(featureKey.asPreferenceKey()))
            prefs.remove(booleanPreferencesKey(featureKey.asPreferenceKey()))
            prefs.remove(longPreferencesKey(featureKey.asPreferenceKey()))
            prefs.remove(doublePreferencesKey(featureKey.asPreferenceKey()))
        }
    }

    override suspend fun clear() {
        dataStore.edit { prefs ->
            prefs.clear()
        }
    }

    private fun String.asPreferenceKey(): String {
        return "proteus_${this.sha1()}"
    }
}

// androidMain - Platform-specific DataStore creation
expect fun createDataStore(context: Any): DataStore<Preferences>

actual fun createDataStore(context: Any): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        produceFile = {
            (context as Context).filesDir.resolve("proteus_config.preferences_pb")
        }
    )
}

// iosMain - Platform-specific DataStore creation
actual fun createDataStore(context: Any): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        produceFile = {
            val documentDirectory = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory,
                NSUserDomainMask,
                true
            ).first() as String
            File(documentDirectory, "proteus_config.preferences_pb")
        }
    )
}
```

#### Platform Context
```kotlin
// commonMain
expect class PlatformContext

// androidMain
actual typealias PlatformContext = android.content.Context

// iosMain
actual class PlatformContext(val viewController: UIViewController)
```

---

## Migration Strategy

### Phase 1: Core Module Migration (4-6 weeks)
**Goal**: Migrate core abstractions to KMP while maintaining Android compatibility

#### Tasks:
1. **Convert existing modules to KMP**
   - Update `proteus-core/build.gradle.kts` for multiplatform
   - Create source sets (commonMain, androidMain, iosMain)
   - Move existing code from `src/main/java` to `src/androidMain/kotlin`

2. **Migrate domain models to commonMain**
   - Move `ConfigValue` sealed class to commonMain
   - Move `Feature` and `FeatureContext` interfaces
   - Ensure kotlinx.serialization works cross-platform

3. **Implement DataStore for cross-platform storage**
   - Move `MockConfigStorage` interface to commonMain
   - Replace `PreferenceMockConfigStorage` with `DataStoreMockConfigStorage`
   - Single implementation in commonMain using DataStore
   - Platform-specific DataStore creation only
   - Automatic migration from SharedPreferences on Android

4. **Migrate providers**
   - Move provider interfaces to commonMain
   - Create platform-specific factory patterns
   - Ensure suspend functions work on both platforms

5. **Testing**
   - Setup shared tests in commonTest
   - Platform-specific tests in androidTest/iosTest
   - Verify backward compatibility

#### Deliverables:
- [ ] KMP project structure
- [ ] Migrated domain models
- [ ] Platform storage implementations
- [ ] Shared unit tests
- [ ] Documentation updates

### Phase 2: UI Migration with CMP (6-8 weeks)
**Goal**: Create unified UI using Compose Multiplatform

#### Tasks:
1. **Setup Compose Multiplatform**
   - Add CMP dependencies
   - Configure for iOS target
   - Setup resource management

2. **Migrate Material Design 3 theme**
   - Move Color.kt, Type.kt, Theme.kt to commonMain
   - Ensure beige theme works on iOS
   - Test dark mode support

3. **Migrate UI components**
   - FeatureCatalogScreen
   - FeatureConfiguratorScreen
   - SearchBar component
   - Override indicators

4. **Platform-specific UI adjustments**
   - iOS navigation patterns
   - Safe area handling
   - Keyboard management

5. **Testing**
   - UI tests with Compose Testing
   - Screenshot tests for both platforms
   - Accessibility verification

#### Deliverables:
- [ ] CMP UI module
- [ ] Migrated screens and components
- [ ] Platform-specific adjustments
- [ ] UI test suite
- [ ] Design documentation

### Phase 3: Firebase Provider Migration (3-4 weeks)
**Goal**: Support Firebase Remote Config on both platforms

#### Tasks:
1. **Abstract Firebase provider**
   - Create common interface
   - Handle platform differences

2. **Android implementation**
   - Wrap existing Firebase Android SDK
   - Maintain current functionality

3. **iOS implementation**
   - Integrate Firebase iOS SDK
   - Handle Swift interop
   - Manage CocoaPods dependencies

4. **Testing**
   - Integration tests with real Firebase
   - Mock Firebase for unit tests
   - Verify feature parity

#### Deliverables:
- [ ] Cross-platform Firebase provider
- [ ] Platform implementations
- [ ] Integration tests
- [ ] Setup documentation

### Phase 4: Distribution & Publishing (2-3 weeks)
**Goal**: Publish to both Maven Central and Apple ecosystems

#### Tasks:
1. **Android/JVM distribution**
   - Maintain Maven Central publishing
   - Update BOM module
   - Preserve version strategy

2. **iOS distribution**
   - Setup XCFramework generation
   - Configure Swift Package Manager
   - Create CocoaPods podspec
   - Setup Carthage support

3. **Documentation**
   - Installation guides per platform
   - Migration guide from Android-only
   - API documentation with Dokka
   - Sample apps for both platforms

4. **CI/CD updates**
   - GitHub Actions for iOS builds
   - XCFramework publishing workflow
   - Cross-platform testing matrix

#### Deliverables:
- [ ] Published Android artifacts
- [ ] Published iOS frameworks
- [ ] Updated documentation
- [ ] CI/CD pipelines

### Phase 5: Beta Testing & Refinement (4-6 weeks)
**Goal**: Validate with real users and refine

#### Tasks:
1. **Internal testing**
   - Dogfood in real projects
   - Gather team feedback
   - Performance profiling

2. **External beta**
   - Recruit beta testers
   - Setup feedback channels
   - Monitor crash reports

3. **Refinements**
   - Address feedback
   - Optimize performance
   - Fix platform-specific issues

4. **Documentation polish**
   - Update based on feedback
   - Add troubleshooting guide
   - Create video tutorials

#### Deliverables:
- [ ] Beta feedback report
- [ ] Performance improvements
- [ ] Bug fixes
- [ ] Polished documentation

---

## Platform-Specific Implementation Details

### Android Specifics
```kotlin
// Initialization in Application class (minimal changes)
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // DataStore is created internally with automatic migration
        Proteus.Builder(this)
            .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
            .registerFeatureBookDataSource(
                AssetsFeatureBookDataSource(this, "features.json")
            )
            .build()

        // Optional: Custom DataStore configuration
        val customDataStore = createDataStore(this)
        Proteus.Builder(this)
            .setMockConfigStorage(DataStoreMockConfigStorage(customDataStore))
            .registerConfigProviderFactory(FirebaseOnlyProviderFactory())
            .build()
    }
}
```

### iOS Specifics
```swift
// Initialization in AppDelegate
class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication,
                    didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {

        // Initialize Proteus with iOS storage implementation
        let storage = NSDefaultsMockConfigStorage()
        let dataSource = BundleFeatureBookDataSource(fileName: "features.json")

        Proteus.shared.initialize(
            storage: storage,
            providerFactory: FirebaseProviderFactory(),
            featureDataSource: dataSource
        )
        return true
    }
}

// SwiftUI Integration
struct ProteusView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        return FeatureBookViewControllerKt.createFeatureBookViewController()
    }
}
```

### Desktop Support (Future)
```kotlin
// Desktop initialization
fun main() = application {
    ProteusDesktop.initialize(
        storage = DesktopStorage(path = "~/.proteus"),
        providers = listOf(CustomProvider())
    )

    Window(onCloseRequest = ::exitApplication) {
        ProteusDesktopUI()
    }
}
```

---

## Distribution Strategy

### Maven Central (Android/JVM)
```kotlin
// Current (unchanged)
implementation(platform("io.github.maxim-petlyuk:proteus-bom:3.0.0"))
implementation("io.github.maxim-petlyuk:proteus-core")
implementation("io.github.maxim-petlyuk:proteus-firebase")
implementation("io.github.maxim-petlyuk:proteus-ui")

// New KMP artifacts
implementation("io.github.maxim-petlyuk:proteus-core-kmp:3.0.0")
implementation("io.github.maxim-petlyuk:proteus-ui-cmp:3.0.0")
```

### Swift Package Manager
```swift
// Package.swift
dependencies: [
    .package(url: "https://github.com/maxim-petlyuk/proteus-ios.git", from: "3.0.0")
]

// In Xcode
// Add package dependency: https://github.com/maxim-petlyuk/proteus-ios
```

### CocoaPods
```ruby
# Podfile
pod 'Proteus', '~> 3.0.0'
pod 'Proteus/Firebase', '~> 3.0.0'  # Optional Firebase support
```

### XCFramework Structure
```
Proteus.xcframework/
├── ios-arm64/
│   └── Proteus.framework
├── ios-arm64_x86_64-simulator/
│   └── Proteus.framework
└── Info.plist
```

---

## Risk Assessment & Mitigation

### Technical Risks

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| CMP iOS stability | High | Medium | Use stable CMP version, extensive testing |
| Firebase SDK differences | Medium | High | Abstract differences in common layer |
| Performance regression | Medium | Low | Profile critical paths, optimize hot spots |
| Binary size increase | Low | Medium | Use ProGuard/R8, tree shaking |
| iOS memory management | Medium | Medium | Careful Kotlin/Native memory model usage |

### Business Risks

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| Adoption resistance | Medium | Medium | Provide migration guide, maintain Android version |
| Increased complexity | Medium | High | Comprehensive docs, sample apps |
| Support burden | High | Medium | Phased rollout, beta testing |
| Version fragmentation | Medium | Low | Clear versioning strategy |

---

## Timeline & Milestones

### Overall Timeline: 5-6 months

```mermaid
gantt
    title Proteus KMP Migration Timeline
    dateFormat  YYYY-MM-DD
    section Phase 1
    Core Migration           :2025-02-01, 6w
    section Phase 2
    UI Migration (CMP)       :2025-03-15, 8w
    section Phase 3
    Firebase Provider        :2025-05-10, 4w
    section Phase 4
    Distribution Setup       :2025-06-07, 3w
    section Phase 5
    Beta Testing            :2025-06-28, 6w
    section Release
    v3.0.0 Launch           :milestone, 2025-08-09, 0d
```

### Key Milestones

| Milestone | Date | Description |
|-----------|------|-------------|
| M1: KMP Structure | Feb 15, 2025 | Basic KMP project compiling |
| M2: Core Complete | Mar 15, 2025 | Core module fully migrated |
| M3: UI Working | May 10, 2025 | CMP UI running on iOS |
| M4: Firebase Ready | Jun 7, 2025 | Firebase provider cross-platform |
| M5: Beta Release | Jun 28, 2025 | Public beta available |
| M6: GA Release | Aug 9, 2025 | Version 3.0.0 launch |

---

## Success Metrics

### Technical Metrics
- **Code sharing**: Target 60-70% shared code
- **Binary size**: iOS framework < 5MB
- **Performance**: Override UI loads < 100ms
- **Test coverage**: >80% for shared code
- **Crash rate**: <0.1% on both platforms

### Adoption Metrics
- **iOS downloads**: 500+ in first month
- **GitHub stars**: Additional 200+ stars
- **Active projects**: 20+ iOS apps using Proteus
- **Community PRs**: 5+ contributions
- **Documentation views**: 10,000+ page views

### Quality Metrics
- **Bug reports**: <10 critical issues in first month
- **Response time**: <24 hours for critical issues
- **User satisfaction**: >4.5/5 rating
- **Migration success**: 90% successful migrations
- **API stability**: No breaking changes for 6 months

---

## Resource Requirements

### Team Structure
- **Lead Developer**: 1 person (full-time)
- **iOS Developer**: 1 person (50% time)
- **UI/UX Designer**: 1 person (25% time)
- **QA Engineer**: 1 person (50% time)
- **Technical Writer**: 1 person (25% time)

### Infrastructure
- **CI/CD**: GitHub Actions (expanded for iOS)
- **Testing**: Physical iOS devices + simulators
- **Distribution**: Maven Central + GitHub releases
- **Documentation**: GitHub Pages + Dokka
- **Analytics**: Firebase Analytics for usage metrics

### Budget Estimate
- **Developer time**: ~800 hours
- **iOS testing devices**: $3,000
- **Apple Developer Program**: $99/year
- **CI/CD infrastructure**: $200/month
- **Total estimate**: ~$85,000

---

## Migration Guide for Existing Users

### Android Users
```kotlin
// Old (Android-only)
import io.proteus.core.Proteus

// New (KMP)
import io.proteus.kmp.core.Proteus

// API remains largely the same
Proteus.Builder(context)
    .registerConfigProviderFactory(factory)
    .build()
```

### New iOS Users
```swift
// Swift integration
import Proteus

class ViewController: UIViewController {
    override func viewDidLoad() {
        super.viewDidLoad()

        // Initialize Proteus
        Proteus.shared.initialize(providers: [FirebaseProvider()])

        // Show override UI
        let proteusVC = FeatureBookViewController()
        present(proteusVC, animated: true)
    }
}
```

---

## Competitive Analysis

### Current Landscape
| Library | Platform | Override UI | Open Source | Active |
|---------|----------|------------|-------------|--------|
| Proteus (current) | Android | ✅ | ✅ | ✅ |
| Proteus KMP | Android + iOS | ✅ | ✅ | Planned |
| Firebase Console | Web | ❌ | ❌ | ✅ |
| LaunchDarkly | Multi | ❌ | ❌ | ✅ |
| Optimizely | Multi | ❌ | ❌ | ✅ |

### Unique Value Proposition
- **First** cross-platform library with built-in override UI
- **Only** open-source solution with Material Design 3
- **Simplest** integration (single dependency)
- **Fastest** testing workflow (no external tools)
- **Most secure** (no production admin access needed)

---

## Future Opportunities

### Post-KMP Roadmap
1. **Desktop Support** (Windows, macOS, Linux)
2. **Web Support** via Kotlin/JS
3. **Flutter Plugin** via platform channels
4. **React Native Module**
5. **Cloud Sync** for team overrides
6. **Analytics Dashboard**
7. **A/B Test Results Viewer**
8. **Config History & Rollback**

### Potential Partnerships
- **Firebase team**: Official integration
- **JetBrains**: Featured KMP showcase
- **Google**: Android Dev Summit presentation
- **Major apps**: Case studies and testimonials

---

## Conclusion

The migration to KMP + CMP represents a strategic evolution of Proteus from an Android-specific tool to a comprehensive cross-platform solution. This positions Proteus as the pioneer in cross-platform remote config testing, opening new markets and solving the same critical problem for iOS teams.

The investment in KMP will:
- Double the addressable market
- Reduce long-term maintenance costs
- Establish Proteus as an industry leader
- Create foundation for future platform expansion

With careful execution of this plan, Proteus 3.0 will become the de facto standard for remote config testing across mobile platforms.

---

## Appendix A: Technical Dependencies

### KMP Dependencies
```kotlin
// proteus-core/build.gradle.kts
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
}

kotlin {
    android()
    ios()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
                // DataStore for cross-platform storage
                implementation("androidx.datastore:datastore-preferences-core:1.1.0")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.12.0")
                // DataStore Android
                implementation("androidx.datastore:datastore-preferences:1.1.0")
            }
        }
        val iosMain by getting {
            dependencies {
                // DataStore automatically works on iOS via KMP
            }
        }
    }
}

// Migration from SharedPreferences (Android only)
android {
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}
```

### CMP Dependencies
```kotlin
// Compose Multiplatform
compose {
    dependencies {
        implementation(compose.runtime)
        implementation(compose.foundation)
        implementation(compose.material3)
        implementation(compose.ui)
        implementation(compose.animation)
    }
}
```

---

## Appendix B: API Compatibility Matrix

| Feature | Android | iOS | Desktop | Web |
|---------|---------|-----|---------|-----|
| Core Provider | ✅ | ✅ | 🔄 | ❌ |
| Firebase Provider | ✅ | ✅ | ❌ | ❌ |
| CleverTap Provider | ✅ | ❓ | ❌ | ❌ |
| Custom Provider | ✅ | ✅ | ✅ | ✅ |
| Override UI | ✅ | ✅ | 🔄 | ❌ |
| Persistence | ✅ | ✅ | ✅ | ✅ |
| Encryption | ✅ | ✅ | ✅ | ✅ |
| Multi-module | ✅ | ✅ | ✅ | ✅ |

Legend: ✅ Supported | 🔄 Planned | ❓ Investigation needed | ❌ Not planned

---

**Document Version**: 1.0
**Last Updated**: January 2025
**Next Review**: February 2025
**Status**: Ready for Review