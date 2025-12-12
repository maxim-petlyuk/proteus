# Create New Module

Create a new Gradle module in the Proteus library following the established pattern.

## Process

1. Create module directory structure
2. Set up build.gradle.kts with:
    - Kotlin Android plugin
    - Maven publish configuration
    - JReleaser setup
3. Create gradle.properties with initial version
4. Add to settings.gradle.kts
5. Update BOM module to include new module
6. Create basic package structure
7. Add README.md for the module

## Ask User

- Module name (e.g., "proteus-clevertap")
- Initial version (default: 0.1.0)
- Module description