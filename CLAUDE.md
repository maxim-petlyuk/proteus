# Proteus - A/B Remote Configuration Library

## 🎯 Project Overview

Multi-module Android library for wrapping remote config providers (Firebase, CleverTap) with runtime override capabilities via local UI
interface.

## 📦 Architecture

- **proteus-core**: Core abstractions and interfaces
- **proteus-firebase**: Firebase Remote Config implementation
- **proteus-ui**: Material Design UI for runtime overrides
- **proteus-bom**: Bill of Materials for dependency management

**Critical**: Each module has independent versioning. Use semantic versioning.

## 🔧 Tech Stack

- Language: Kotlin (target JVM 17)
- Build: Gradle 8.x with Kotlin DSL
- Android: Min SDK 21, Target SDK 34
- UI: Jetpack Compose + Material Design 3
- Publishing: Maven Central via JReleaser 1.20.0

## 📂 Project Structure

```
proteus/
├── proteus-core/          # Core library interfaces
├── proteus-firebase/      # Firebase provider
├── proteus-ui/            # Compose UI components
├── proteus-bom/           # BOM for version management
└── .github/workflows/     # CI/CD automation
```

## 🚨 CRITICAL RULES

- NEVER modify version numbers directly - use workflows or ask first
- ALWAYS test locally before pushing CI/CD changes
- DO NOT commit secrets or API keys
- NEVER bypass the multi-module release orchestration
- ALWAYS maintain independent module versioning
- DO NOT use JReleaser 1.21 (use 1.20.0 for stability)

## 🎨 UI Development Guidelines

- Follow Material Design 3 principles
- Use beige color palette (check Color.kt for theme)
- Typography: Roboto family
- All UI must be Compose-based
- Component structure: Color.kt, Type.kt, Theme.kt, Dimens.kt, Shape.kt

## 🔨 Common Commands

#### Build all modules

./gradlew build

#### Publish locally for testing

./gradlew publishToMavenLocal

#### Test JReleaser config (dry-run)

./gradlew jreleaserConfig --stacktrace

#### Run tests for specific module

./gradlew :proteus-core:test

## 🚀 Release Process

1. Version updates via GitHub Actions workflows
2. JReleaser handles Maven Central publishing
3. Staged deployments: UPLOAD → PUBLISH → FULL
4. PGP keys distributed to ubuntu, openpgp, mit keyservers
5. Wait 5-10 minutes for key propagation before PUBLISH

## 📝 Git Workflow

- Main branch: `main`
- Feature branches: `feature/*`
- Release tags: `{module}-v{version}` (e.g., `proteus-core-v1.0.0` or `v1.0.0` for bom)
- Commit messages: Conventional Commits format

## 🔍 Important Context Files

- For CI/CD debugging: `.github/workflows/*.yml`
- For release config: `gradle.properties` and `publish.properties`
- For UI theming: `proteus-ui/src/main/kotlin/io/github/maximpetlyuk/proteus/ui/theme/`
- For documentation: `docs/*.md`

## ❌ DO NOT

- Edit files in `/build/` directories (generated)
- Modify `gradle-wrapper.jar` manually
- Change JReleaser version from 1.20.0
- Use `cat >` for file updates (use `sed` for targeted edits)
- Create releases without proper keyserver distribution

## 💡 When You Need Help

- For Maven Central issues: Check keyserver propagation first
- For CI/CD failures: Validate YAML syntax and secrets
- For UI design: Reference the Material Design system files
- For versioning conflicts: Each module has its own version