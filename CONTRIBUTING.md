# Contributing to Proteus

Thank you for your interest in contributing to Proteus! This guide will help you get started with development, understand our processes, and make meaningful contributions.

## Table of Contents

- [Development Environment Setup](#development-environment-setup)
- [Code Style Guidelines](#code-style-guidelines)
- [Pull Request Process](#pull-request-process)
- [Issue Reporting](#issue-reporting)
- [Testing Requirements](#testing-requirements)
- [Documentation Requirements](#documentation-requirements)
- [Commit Message Guidelines](#commit-message-guidelines)

## Development Environment Setup

### Required Tools

- **Android Studio**: Latest stable version (Flamingo or newer recommended)
- **JDK**: Version 17 (OpenJDK or Oracle JDK)
- **Git**: For version control

### Clone and Build Instructions

1. **Clone the repository:**
   ```bash
   git clone https://github.com/maxim-petlyuk/proteus.git
   cd proteus
   ```

2. **Build the project:**
   ```bash
   ./gradlew build
   ```

3. **Run tests:**
   ```bash
   ./gradlew test
   ```

4. **Run sample app:**
   ```bash
   ./gradlew :sample-firebase:installDebug
   ```

### Project Structure

```
proteus/
├── proteus-core/          # Core library interfaces and implementations
├── proteus-firebase/      # Firebase Remote Config provider
├── proteus-ui/           # Compose UI for configuration override
├── proteus-bom/          # Bill of Materials for dependency management
└── sample-firebase/      # Sample application demonstrating usage
```

## Code Style Guidelines

### EditorConfig

The project uses [EditorConfig](.editorconfig) for consistent formatting. Most IDEs support this automatically.

### Kotlin Coding Conventions

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use `kotlin.code.style=official` (already configured in gradle.properties)
- Maximum line length: 120 characters
- Use 4 spaces for indentation

### Compose Best Practices

- Follow [Compose guidelines](https://developer.android.com/jetpack/compose/guidelines)
- Use `@Preview` functions for UI components
- Prefer stateless composables when possible
- Use descriptive parameter names

### Code Organization

- Group related functionality in the same file
- Use meaningful class and function names
- Add KDoc comments for public APIs
- Keep functions focused and small

## Pull Request Process

### Workflow

1. **Fork the repository** on GitHub
2. **Create a feature branch** from `main`:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. **Make your changes** following the code style guidelines
4. **Write or update tests** for your changes
5. **Update documentation** if needed
6. **Commit your changes** using [conventional commits](#commit-message-guidelines)
7. **Push to your fork** and create a pull request

### Pull Request Checklist

Before submitting your PR, ensure:

- [ ] Code follows project style guidelines
- [ ] All tests pass (`./gradlew test`)
- [ ] New functionality includes tests
- [ ] Documentation is updated (if applicable)
- [ ] Commit messages follow conventional commit format
- [ ] PR description clearly explains the changes
- [ ] No merge conflicts with `main` branch

### Review Process

- All PRs require at least one review from a maintainer
- CI checks must pass before merging
- Address feedback promptly and professionally
- Keep PRs focused on a single feature or fix

## Issue Reporting

### Bug Reports

Use the [bug report template](https://github.com/maxim-petlyuk/proteus/issues/new?template=bug_report.md) and include:

- Clear description of the issue
- Steps to reproduce
- Expected vs actual behavior
- Environment details (Android version, library version)
- Minimal code example
- Crash logs or screenshots (if applicable)

### Feature Requests

Use the [feature request template](https://github.com/maxim-petlyuk/proteus/issues/new?template=feature_request.md) and include:

- Clear description of the proposed feature
- Use case and motivation
- Proposed API or implementation (if any)
- Alternative solutions considered

### Questions

For questions, use [GitHub Discussions](https://github.com/maxim-petlyuk/proteus/discussions) instead of issues.

## Testing Requirements

### Unit Tests

- Write unit tests for all new functionality
- Maintain or improve test coverage
- Use meaningful test names that describe the scenario
- Follow the AAA pattern (Arrange, Act, Assert)

### Integration Tests

- Add integration tests for complex features
- Test multi-module interactions
- Verify UI components with Compose testing

### Running Tests

```bash
# Run all tests
./gradlew test

# Run tests for specific module
./gradlew :proteus-core:test

# Run with coverage
./gradlew testDebugUnitTestCoverage
```

## Documentation Requirements

### Code Documentation

- Add KDoc for all public APIs
- Include usage examples in KDoc
- Document complex algorithms or business logic
- Keep comments up-to-date with code changes

### README Updates

- Update README.md if adding new features
- Include code examples for new functionality
- Update installation instructions if needed

### Module Documentation

- Update module-specific READMEs
- Document breaking changes
- Provide migration guides for major changes

## Commit Message Guidelines

This project uses [Conventional Commits](https://www.conventionalcommits.org/)
to enable automated changelog generation. Please follow these guidelines for
all commits.

### Format
```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

### Types

- `feat`: New features or capabilities (appears in "New" section)
- `fix`: Bug fixes (appears in "Fixed" section)
- `perf`: Performance improvements (appears in "Performance" section)
- `refactor`: Code changes that neither fix bugs nor add features (appears in "Changed" section)
- `docs`: Documentation changes (not included in release notes)
- `test`: Adding or updating tests (not included in release notes)
- `chore`: Maintenance tasks, dependencies (not included in release notes)
- `style`: Code style changes (not included in release notes)
- `ci`: CI/CD configuration changes (not included in release notes)
- `build`: Build system changes (not included in release notes)

### Scopes

Use the module name as the scope when your change affects a specific module:

- `core` - proteus-core module
- `firebase` - proteus-firebase module
- `ui` - proteus-ui module
- `bom` - proteus-bom module

Omit the scope when the change affects multiple modules.

### Breaking Changes

For breaking changes, add `!` after the scope or include a `BREAKING CHANGE:` footer:
```
feat(core)!: redesign Provider interface

BREAKING CHANGE: The Provider interface now requires a CoroutineScope parameter.
```

### Examples
```
feat(firebase): add support for default parameter values
fix(ui): correct theme colors in dark mode
perf(core): cache parsed configuration values
refactor: migrate all modules to Kotlin 2.0
docs: update README with new installation instructions
chore(deps): bump Kotlin to 1.9.22
```

## Multi-Module Changes

When a commit affects multiple modules, **omit the scope**:

```bash
# Good - affects multiple modules
refactor: migrate all modules to Kotlin 2.0
feat: add logging support across all providers
fix: resolve version compatibility issues across modules

# Avoid - this approach creates separate commits
# refactor(core): migrate to Kotlin 2.0
# refactor(firebase): migrate to Kotlin 2.0
# refactor(ui): migrate to Kotlin 2.0
```

Multi-module commits (no scope) will appear in:
- BOM changelog (aggregated view)
- Individual module changelogs (if files in that module were modified)

## Breaking Changes

Always use `!` indicator and provide migration guidance:

```bash
feat(core)!: remove deprecated ConfigProvider interface

BREAKING CHANGE: ConfigProvider has been removed.
Use the new Provider interface instead.
See docs/migration-guide.md for detailed migration steps.
```

## Local Development Setup

### Git Hooks

To enable commit message validation, set up the git hook:

```bash
# Make the hook executable
chmod +x .githooks/commit-msg

# Configure git to use the hooks directory
git config core.hooksPath .githooks
```

This will validate your commit messages locally before they're committed.

### IDE Integration

For **IntelliJ IDEA / Android Studio**:

1. Install the "Conventional Commit" plugin from the Marketplace
2. Use the plugin UI when creating commits for guided type/scope selection
3. The plugin provides dropdowns and validation for proper commit formatting