# Changelog Automation Technical Documentation

This document describes the technical implementation of automated changelog generation in the Proteus project.

## Architecture Overview

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Conventional   │────▶│    git-cliff    │────▶│ JReleaser       │────▶│ GitHub Release  │
│    Commits      │     │   (changelog)   │     │ (Gradle DSL)    │     │ + CHANGELOG.md  │
└─────────────────┘     └─────────────────┘     └─────────────────┘     └─────────────────┘
```

## Configuration Files

### cliff.toml

**Location**: Repository root
**Purpose**: Configures git-cliff changelog generation

**Key Sections:**
- `changelog.body`: Jinja2 template for rendering release notes
- `git.commit_parsers`: Maps commit types to display groups
- `git.tag_pattern`: Regex for identifying version tags

**Tag Pattern Logic:**
```toml
tag_pattern = "(proteus-(core|firebase|ui)-)?v[0-9]+\\.[0-9]+\\.[0-9]+"
```

**Matches:**
- Module tags: `proteus-core-v1.0.0`, `proteus-firebase-v1.0.0`, `proteus-ui-v1.0.0`
- BOM tags: `v1.0.0`

### JReleaser Configuration (Gradle DSL)

**Location**: Each module's `build.gradle.kts`
**Purpose**: Configures GitHub releases and Maven Central publishing

**BOM Configuration** (`proteus-bom/build.gradle.kts`):
```kotlin
changelog {
    enabled = true
    external = File(rootProject.rootDir, "CHANGELOG.md")
    append.enabled = false
}
```

**Module Configuration** (e.g., `proteus-core/build.gradle.kts`):
```kotlin
changelog {
    enabled = true
    external = File(rootProject.rootDir, "proteus-core/CHANGELOG.md")
    append.enabled = false
}
```

## Workflow Integration

### Workflow Architecture

**Manual Control System:**
- ✅ **Staged Deployments**: UPLOAD → PUBLISH → FULL
- ✅ **Module Selection**: Individual or bulk releases
- ✅ **Version Coordination**: Through `publish.properties`
- ✅ **Flexible Triggering**: Manual dispatch + tag triggers

### Workflow Files

| Workflow | Trigger | Purpose | Changelog Strategy |
|----------|---------|---------|-------------------|
| `release-specific-module.yml` | Manual dispatch | Individual module releases | Module-specific with path filter |
| `release-bom-only.yml` | Manual dispatch | BOM coordination releases | Aggregated across all modules |
| `release-all-with-bom.yml` | `v*` tag or manual | Complete project release | All changelogs generated |
| `release-all-dry-run.yml` | PR/manual | Testing and validation | No changelog generation |

### Integration Points

**1. git-cliff Installation:**
```yaml
- name: Install git-cliff
  uses: taiki-e/install-action@v2
  with:
    tool: git-cliff
```

**2. Module-Specific Generation:**
```yaml
- name: Generate proteus-core changelog
  if: inputs.module == 'proteus-core' || inputs.module == 'all'
  run: |
    git cliff \
      --config cliff.toml \
      --include-path "proteus-core/**" \
      --tag-pattern "proteus-core-v.*" \
      --output proteus-core/CHANGELOG.md
```

**3. BOM Generation:**
```yaml
- name: Generate BOM changelog
  run: |
    git cliff \
      --config cliff.toml \
      --tag-pattern "^v[0-9]" \
      --output CHANGELOG.md
```

## Path Filtering Strategy

### Module-Specific Filters

| Module | Path Filter | Tag Pattern | Output |
|--------|-------------|-------------|--------|
| proteus-core | `proteus-core/**` | `proteus-core-v.*` | `proteus-core/CHANGELOG.md` |
| proteus-firebase | `proteus-firebase/**` | `proteus-firebase-v.*` | `proteus-firebase/CHANGELOG.md` |
| proteus-ui | `proteus-ui/**` | `proteus-ui-v.*` | `proteus-ui/CHANGELOG.md` |
| BOM (aggregated) | *(no filter)* | `^v[0-9]` | `CHANGELOG.md` |

### Path Filter Logic

**Include Path**: `--include-path "proteus-core/**"`
- ✅ Includes: `proteus-core/src/main/kotlin/File.kt`
- ✅ Includes: `proteus-core/build.gradle.kts`
- ❌ Excludes: `proteus-firebase/src/main/kotlin/File.kt`
- ❌ Excludes: `README.md` (root level)

## Tag Pattern Matching

### Pattern Breakdown

**Full Pattern**: `(proteus-(core|firebase|ui)-)?v[0-9]+\\.[0-9]+\\.[0-9]+`

**Components:**
- `(proteus-(core|firebase|ui)-)?`: Optional module prefix
- `v`: Required version prefix
- `[0-9]+\\.[0-9]+\\.[0-9]+`: Semantic version pattern

**Examples:**
```bash
✅ proteus-core-v1.0.0      # Module release
✅ proteus-firebase-v2.1.3  # Module release
✅ v1.0.0                   # BOM release
❌ 1.0.0                    # Missing 'v' prefix
❌ core-v1.0.0              # Missing 'proteus-' prefix
❌ v1.0                     # Incomplete semantic version
```

### Tag-Specific Filtering

**Module Releases:**
```bash
# Only proteus-core tags
git cliff --tag-pattern "proteus-core-v.*"

# Only proteus-firebase tags
git cliff --tag-pattern "proteus-firebase-v.*"
```

**BOM Releases:**
```bash
# Only BOM tags (v1.0.0 format)
git cliff --tag-pattern "^v[0-9]"
```

## Local Development Commands

### Preview Commands

```bash
# Preview unreleased changes (all modules)
git cliff --unreleased --strip header

# Preview module-specific unreleased changes
git cliff --include-path "proteus-core/**" --tag-pattern "proteus-core-v.*" --unreleased --strip header

# Preview BOM changes (aggregated)
git cliff --tag-pattern "^v[0-9]" --unreleased --strip header

# Generate changelog between specific versions
git cliff proteus-core-v1.0.0..proteus-core-v1.1.0 --strip header
```

### Gradle Tasks

**Generate Single Module:**
```bash
./gradlew generateChangelog -Pmodule=core
./gradlew generateChangelog -Pmodule=firebase
./gradlew generateChangelog -Pmodule=ui
./gradlew generateChangelog -Pmodule=bom
```

**Generate All Modules:**
```bash
./gradlew generateAllChangelogs
```

**Task Implementation** (`build.gradle.kts`):
```kotlin
val modulePathMap = mapOf(
    "core" to "proteus-core",
    "firebase" to "proteus-firebase",
    "ui" to "proteus-ui"
)

val moduleTagPatternMap = mapOf(
    "core" to "proteus-core-v.*",
    "firebase" to "proteus-firebase-v.*",
    "ui" to "proteus-ui-v.*"
)
```

## Commit Processing Pipeline

### 1. Commit Parsing

**Conventional Commits Format:**
```
feat(core): add new provider interface method
```

**Parsed Components:**
- **Type**: `feat` → **Group**: "New"
- **Scope**: `core` → **Module**: proteus-core
- **Description**: "add new provider interface method"

### 2. Type Mapping

**Visible Types** (appear in changelog):
```toml
{ message = "^feat", group = "New" }
{ message = "^fix", group = "Fixed" }
{ message = "^perf", group = "Performance" }
{ message = "^refactor", group = "Changed" }
```

**Hidden Types** (filtered out):
```toml
{ message = "^docs", skip = true }
{ message = "^test", skip = true }
{ message = "^chore", skip = true }
{ message = "^ci", skip = true }
{ message = "^build", skip = true }
{ message = "^style", skip = true }
```

### 3. Breaking Change Detection

**Pattern Matching:**
```toml
{ message = "^.*!:", group = "⚠️ Breaking Changes" }
{ body = "BREAKING CHANGE", group = "⚠️ Breaking Changes" }
```

**Examples:**
```bash
feat(core)!: remove deprecated interface    # Detected by !:
feat(core): add new API

BREAKING CHANGE: Old API removed.          # Detected by footer
```

## Fallback Handling

### Workflow Resilience

**BOM Changelog Fallback:**
```yaml
- name: Generate BOM changelog
  run: |
    git cliff --config cliff.toml --tag-pattern "^v[0-9]" --output CHANGELOG.md

    if [ -f CHANGELOG.md ]; then
      echo "✅ BOM changelog generated successfully"
    else
      echo "⚠️ Creating minimal changelog"
      echo "# Changelog" > CHANGELOG.md
      echo "## [${{ inputs.bom_version }}] - $(date '+%Y-%m-%d')" >> CHANGELOG.md
      echo "Release notes will be generated automatically in future releases." >> CHANGELOG.md
    fi
```

**Module Changelog Handling:**
- ✅ **Success**: Generated changelog used by JReleaser
- ⚠️ **No commits**: Empty changelog created, release proceeds
- ❌ **git-cliff error**: Workflow reports error but doesn't fail

## Troubleshooting Guide

### Issue: Empty Changelog Output

**Cause**: No commits matching conventional format since last tag

**Diagnosis:**
```bash
# Check commit history since last tag
git log $(git describe --tags --abbrev=0)..HEAD --oneline

# Check if commits match conventional format
git log $(git describe --tags --abbrev=0)..HEAD --pretty=format:"%s" | grep -E "^(feat|fix|perf|refactor)"
```

**Solution**: Ensure commit messages follow `type(scope): description` format

### Issue: Wrong Commits in Module Changelog

**Cause**: Path filter not matching or commits affecting multiple modules

**Diagnosis:**
```bash
# Check which files were modified in commits
git log $(git describe --tags --abbrev=0)..HEAD --name-only

# Test path filter
git log $(git describe --tags --abbrev=0)..HEAD --oneline -- proteus-core/**
```

**Solution**: Verify path filter matches module directory structure

### Issue: Tag Not Recognized

**Cause**: Tag doesn't match the `tag_pattern` regex

**Diagnosis:**
```bash
# List all tags
git tag -l

# Test tag pattern
git cliff --tag-pattern "proteus-core-v.*" --list-tags
```

**Solution**: Ensure tags follow pattern: `proteus-{module}-v{semver}` or `v{semver}`

### Issue: Workflow Permissions

**Cause**: GitHub token lacks necessary permissions

**Required Permissions:**
- `contents: write` - For creating releases
- `packages: write` - For Maven Central publishing

**Configuration:**
```yaml
permissions:
  contents: write
  packages: write
```

## Performance Considerations

### git-cliff Performance

**Repository Size**: Performance scales linearly with commit history
**Optimization**: Use specific tag ranges for large repositories
**Caching**: GitHub Actions caches git-cliff installation

**Example Optimized Command:**
```bash
# Limit scope for better performance
git cliff --tag-pattern "proteus-core-v.*" proteus-core-v1.0.0..HEAD
```

### Workflow Performance

**Parallel Execution**: Module changelog generation runs sequentially
**Build Cache**: Gradle build cache improves performance
**Network Optimization**: Maven Central staging reduces upload time

## Security Considerations

### Secrets Management

**Required Secrets:**
- `REPOSITORY_TOKEN`: GitHub token for releases
- `GPG_SECRET_KEY`: For artifact signing
- `GPG_PASSPHRASE`: GPG key passphrase
- `SONATYPE_USERNAME`: Maven Central username
- `SONATYPE_PASSWORD`: Maven Central token

**Secret Scoping:**
- Repository-level secrets for all workflows
- Environment-specific secrets not used (manual workflows)

### Access Control

**Workflow Triggers:**
- Manual dispatch: Requires repository write access
- Tag push: Automatic, controlled by branch protection

**Permission Model:**
- Workflows run with restricted permissions
- No direct repository modification outside designated paths