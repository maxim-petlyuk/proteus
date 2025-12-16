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