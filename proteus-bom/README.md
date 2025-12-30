# Proteus BOM

Bill of Materials (BOM) for consistent version management across Proteus modules.

## Overview

The `proteus-bom` module provides a Maven BOM (Bill of Materials) that manages versions for all Proteus modules, ensuring version compatibility and simplifying dependency management.

## What is a BOM?

A BOM is a special kind of POM that is used to control the versions of a project's dependencies. When you import a BOM, you can omit versions when declaring Proteus dependencies, and the BOM ensures all modules use compatible versions.

## Installation

```kotlin
dependencies {
    // Import the BOM
    implementation(platform("io.github.maxim-petlyuk:proteus-bom:1.0.0"))

    // Declare Proteus dependencies without versions
    implementation("io.github.maxim-petlyuk:proteus-core")
    implementation("io.github.maxim-petlyuk:proteus-firebase")
    implementation("io.github.maxim-petlyuk:proteus-ui")
}
```

## Benefits

### 1. Simplified Version Management
No need to specify versions for each Proteus module:

```kotlin
// Without BOM - versions needed everywhere
dependencies {
    implementation("io.github.maxim-petlyuk:proteus-core:1.0.0")
    implementation("io.github.maxim-petlyuk:proteus-firebase:1.0.0")
    implementation("io.github.maxim-petlyuk:proteus-ui:1.0.0")
}

// With BOM - single version declaration
dependencies {
    implementation(platform("io.github.maxim-petlyuk:proteus-bom:1.0.0"))
    implementation("io.github.maxim-petlyuk:proteus-core")
    implementation("io.github.maxim-petlyuk:proteus-firebase")
    implementation("io.github.maxim-petlyuk:proteus-ui")
}
```

### 2. Guaranteed Compatibility
All modules included in a BOM version are tested together and guaranteed to be compatible.

### 3. Easy Updates
Update all Proteus modules by changing a single version number:

```kotlin
// Update everything at once
implementation(platform("io.github.maxim-petlyuk:proteus-bom:1.1.0"))
```

## Version Catalog Integration

For even better dependency management, use with Gradle Version Catalogs:

### libs.versions.toml
```toml
[versions]
proteus-bom = "1.0.0"

[libraries]
proteus-bom = { group = "io.github.maxim-petlyuk", name = "proteus-bom", version.ref = "proteus-bom" }
proteus-core = { group = "io.github.maxim-petlyuk", name = "proteus-core" }
proteus-firebase = { group = "io.github.maxim-petlyuk", name = "proteus-firebase" }
proteus-ui = { group = "io.github.maxim-petlyuk", name = "proteus-ui" }
```

### build.gradle.kts
```kotlin
dependencies {
    implementation(platform(libs.proteus.bom))
    implementation(libs.proteus.core)
    implementation(libs.proteus.firebase)
    implementation(libs.proteus.ui)
}
```

## Included Modules

The BOM manages versions for the following modules:

| Module | Description | Required Dependencies |
|--------|-------------|----------------------|
| `proteus-core` | Core abstractions and interfaces | None |
| `proteus-firebase` | Firebase Remote Config provider | Firebase Config SDK |
| `proteus-ui` | Material Design 3 UI | Jetpack Compose |

## Version Alignment

Each BOM version aligns specific versions of all modules:

| BOM Version | Core | Firebase | UI |
|------------|------|----------|----|
| 1.0.0 | 1.0.0 | 1.0.0 | 1.0.0 |
| 1.1.0 | 1.1.0 | 1.0.1 | 1.1.0 |
| 1.2.0 | 1.2.0 | 1.1.0 | 1.2.0 |

*Note: Individual modules may have different version numbers due to independent development cycles.*

## Overriding Versions

If you need to use a specific version of a module despite the BOM:

```kotlin
dependencies {
    implementation(platform("io.github.maxim-petlyuk:proteus-bom:1.0.0"))

    // Override specific module version
    implementation("io.github.maxim-petlyuk:proteus-core:1.1.0-beta01")

    // These still use BOM versions
    implementation("io.github.maxim-petlyuk:proteus-firebase")
    implementation("io.github.maxim-petlyuk:proteus-ui")
}
```

⚠️ **Warning**: Overriding versions may lead to compatibility issues. Test thoroughly when using different versions than those specified in the BOM.

## Migration Guide

### From Individual Dependencies

If you're currently managing Proteus versions individually:

1. Remove version numbers from all Proteus dependencies
2. Add the BOM import at the top of your dependencies block
3. Verify your build works correctly

Before:
```kotlin
dependencies {
    implementation("io.github.maxim-petlyuk:proteus-core:0.9.0")
    implementation("io.github.maxim-petlyuk:proteus-firebase:0.9.1")
    implementation("io.github.maxim-petlyuk:proteus-ui:0.9.2")
}
```

After:
```kotlin
dependencies {
    implementation(platform("io.github.maxim-petlyuk:proteus-bom:1.0.0"))
    implementation("io.github.maxim-petlyuk:proteus-core")
    implementation("io.github.maxim-petlyuk:proteus-firebase")
    implementation("io.github.maxim-petlyuk:proteus-ui")
}
```

## Checking Current Versions

To see which versions are included in a specific BOM version:

```bash
# Check the POM file
curl https://repo1.maven.org/maven2/io/github/maxim-petlyuk/proteus-bom/1.0.0/proteus-bom-1.0.0.pom
```

Or check the [GitHub Releases](https://github.com/maxim-petlyuk/proteus/releases) page for detailed version information.

## Best Practices

1. **Always use BOM for multiple modules**: If using 2+ Proteus modules, use the BOM
2. **Keep BOM updated**: Regularly update to get bug fixes and new features
3. **Test after updates**: Run your test suite after updating BOM version
4. **Check release notes**: Review breaking changes before major version updates
5. **Use Version Catalogs**: Combine with Gradle Version Catalogs for best experience

## Troubleshooting

### Dependency Resolution Issues
```kotlin
// Force BOM version in case of conflicts
configurations.all {
    resolutionStrategy {
        force("io.github.maxim-petlyuk:proteus-bom:1.0.0")
    }
}
```

### Version Conflicts
If you see version conflict errors, ensure:
1. Only one BOM version is imported
2. No individual Proteus modules have versions specified (unless intentional)
3. Check for transitive dependencies that might bring in different versions

## Support

For issues or questions about the BOM:
- [GitHub Issues](https://github.com/maxim-petlyuk/proteus/issues)
- [GitHub Discussions](https://github.com/maxim-petlyuk/proteus/discussions)

## License

```
Copyright 2025 Maxim Petlyuk

Licensed under the Apache License, Version 2.0
```