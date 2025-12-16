# Conventional Commits Reference for Proteus

This document provides a quick reference for writing commit messages that work with Proteus' automated changelog generation.

## Format

```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

## Types

| Type | Release Notes Section | Use For |
|------|----------------------|---------|
| `feat` | **New** | New features, capabilities, API additions |
| `fix` | **Fixed** | Bug fixes, corrections to existing behavior |
| `perf` | **Performance** | Performance improvements without changing functionality |
| `refactor` | **Changed** | Code restructuring without changing external behavior |
| `docs` | *hidden* | Documentation changes only |
| `test` | *hidden* | Adding or modifying tests |
| `chore` | *hidden* | Build scripts, CI, tooling, dependencies |
| `style` | *hidden* | Formatting, whitespace, code style |
| `ci` | *hidden* | CI/CD configuration changes |
| `build` | *hidden* | Build system changes |

## Scopes

Use the module name as scope when your change affects a specific module:

| Scope | Module | Example |
|-------|--------|---------|
| `core` | proteus-core | `feat(core): add suspend function variants` |
| `firebase` | proteus-firebase | `fix(firebase): handle null Remote Config values` |
| `ui` | proteus-ui | `feat(ui): implement search in feature list` |
| `bom` | proteus-bom | `chore(bom): update dependency versions` |
| `deps` | Dependencies | `chore(deps): bump Compose BOM to 2024.06.00` |
| *(omit)* | Multiple modules | `refactor: migrate all modules to Kotlin 2.0` |

## Breaking Changes

Use `!` after the scope or include a `BREAKING CHANGE:` footer:

```bash
feat(core)!: redesign Provider interface for coroutines

BREAKING CHANGE: Provider now uses suspend functions instead of callbacks.
Migration guide available in docs/migration-v2.md
```

## Examples

### New Features

```bash
feat(firebase): add real-time config update listener
feat(ui): implement search functionality in feature catalog
feat(core): add suspend function variants for all providers
feat: add support for custom config validation rules
```

### Bug Fixes

```bash
fix(core): resolve race condition during parallel config fetches
fix(ui): correct dark mode colors in feature editor
fix(firebase): handle null values from Remote Config properly
fix: address memory leak in config caching
```

### Performance Improvements

```bash
perf(ui): optimize feature list rendering with LazyColumn keys
perf(core): improve config parsing speed by 40%
perf(firebase): reduce network calls with smart caching
```

### Code Changes

```bash
refactor(core): simplify Provider interface implementation
refactor: migrate from callbacks to coroutines across all modules
refactor(ui): extract reusable Compose components
```

### Breaking Changes

```bash
feat(core)!: remove deprecated ConfigProvider interface

BREAKING CHANGE: ConfigProvider has been removed.
Use the new Provider interface instead.
See docs/migration.md for migration steps.
```

### Multi-Module Changes

When a commit affects multiple modules, **omit the scope** entirely:

```bash
# Good - affects multiple modules
refactor: migrate all modules to Kotlin 2.0
feat: add logging support across all providers
fix: resolve version compatibility issues across modules

# Avoid - this creates unnecessary separate commits
# refactor(core): migrate to Kotlin 2.0
# refactor(firebase): migrate to Kotlin 2.0
# refactor(ui): migrate to Kotlin 2.0
```

**Multi-module commits (no scope) will appear in:**
- BOM changelog (aggregated view of all changes)
- Individual module changelogs (if files in that module were modified)

**When to omit scope:**
- Dependency updates affecting multiple modules
- Build system changes across modules
- Code style/formatting changes across codebase
- Migration to new language features or libraries
- Cross-cutting feature implementations

### Hidden Changes (Won't Appear in Release Notes)

```bash
docs: update README with new installation instructions
docs(firebase): add setup guide for Remote Config integration
test(core): add unit tests for ConfigCache implementation
test: increase test coverage to 90% across all modules
chore(deps): update Kotlin to 2.0.0
chore: configure dependabot for automated dependency updates
style: apply ktlint formatting across codebase
ci: add workflow for automated changelog generation
build: optimize Gradle build performance
```

## Tips for Great Commit Messages

### Writing Guidelines

1. **Keep descriptions concise**: First line should be under 72 characters
2. **Use imperative mood**: "add feature" not "added feature" or "adding feature"
3. **Be specific**: "fix login button alignment" not "fix UI bug"
4. **Scope appropriately**: Use module scope when change is module-specific

### Good Examples

```bash
✅ feat(firebase): add retry mechanism for failed config fetches
✅ fix(ui): prevent crash when feature list is empty
✅ perf(core): cache parsed configuration to avoid re-parsing
✅ refactor(firebase): simplify Firebase initialization logic
```

### Bad Examples

```bash
❌ fix stuff
❌ update code
❌ firebase changes
❌ WIP: working on new feature
❌ Fixed bug in the UI component that was causing crashes
```

### Breaking Change Guidelines

1. **Always use `!` or footer**: Make breaking changes explicit
2. **Provide migration guidance**: Include steps or documentation references
3. **Be clear about impact**: Explain what changes for users
4. **Link to documentation**: Reference migration guides or API docs

```bash
# Good breaking change
feat(core)!: replace callback-based API with coroutines

BREAKING CHANGE: All Provider methods now use suspend functions.
Replace callback-based calls with coroutine calls.
See docs/migration-v2.md for detailed migration steps.

# Bad breaking change
feat(core): update API
```

## Integration with Workflows

### Local Development

```bash
# Before committing, preview the impact
git cliff --unreleased --strip header

# Check specific module impact
git cliff --include-path "proteus-core/**" --unreleased --strip header
```

### Commit Hook Validation

The project includes commit message validation. If your commit is rejected:

1. Check the format: `type(scope): description`
2. Ensure type is valid: `feat`, `fix`, `docs`, `style`, `refactor`, `perf`, `test`, `chore`, `ci`, `build`
3. Verify scope is lowercase: `core`, not `Core` or `CORE`
4. Confirm description starts with lowercase letter

## Module-Specific Guidelines

### proteus-core
Focus on core functionality, provider interfaces, configuration handling:
```bash
feat(core): add new provider interface method
fix(core): resolve config parsing edge case
perf(core): optimize memory usage in config cache
```

### proteus-firebase
Firebase Remote Config integration, Firebase-specific features:
```bash
feat(firebase): add support for Firebase A/B testing
fix(firebase): handle Firebase SDK initialization errors
perf(firebase): reduce Firebase API call frequency
```

### proteus-ui
Compose UI components, Material Design implementation:
```bash
feat(ui): add dark theme support to all components
fix(ui): correct button ripple effect in Material theme
perf(ui): optimize recomposition in feature list
```

This reference helps ensure your commits generate meaningful, well-organized release notes that clearly communicate changes to users and maintainers.