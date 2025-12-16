# Proteus Release Notes Automation Plan

## Implementation Guide for git-cliff + JReleaser Integration

This document provides a complete, incremental implementation plan for automating release notes generation in the Proteus project. Follow each phase sequentially, verifying checkpoints before proceeding to the next phase.

---

## Project Context

Proteus is an open-source Android library for A/B remote configuration published to Maven Central. The project uses a multi-module Gradle architecture with independent versioning.

**Modules:**
- `proteus-core` — Core abstractions and interfaces
- `proteus-firebase` — Firebase Remote Config provider
- `proteus-ui` — Jetpack Compose debug UI
- `proteus-bom` — Bill of Materials aggregating all modules

**Tag Patterns:**
- Module releases: `proteus-{module}-v{version}` (e.g., `proteus-core-v0.0.5`, `proteus-firebase-v0.0.5`, `proteus-ui-v0.0.5`)
- BOM releases: `v{version}` (e.g., `v0.0.5`)

**Current Infrastructure:**
- JReleaser Gradle plugin for Maven Central publishing
- GitHub Actions for CI/CD automation
- GPG signing with staged deployment

---

## Phase 1: Conventional Commits Specification

### 1.1 Commit Message Format

The commit message structure follows this pattern:

```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

**Types and their mapping to release notes sections:**

| Type | Release Notes Section | When to Use |
|------|----------------------|-------------|
| `feat` | **New** | New features, capabilities, or public API additions |
| `fix` | **Fixed** | Bug fixes, corrections to existing behavior |
| `perf` | **Performance** | Performance improvements without changing functionality |
| `refactor` | **Changed** | Code restructuring without changing external behavior |
| `docs` | *hidden* | Documentation only changes |
| `test` | *hidden* | Adding or modifying tests |
| `chore` | *hidden* | Build scripts, CI, tooling, dependencies |
| `style` | *hidden* | Formatting, whitespace, code style |

**Scopes for module identification:**

| Scope | Module | Tag Pattern | Path Filter |
|-------|--------|-------------|-------------|
| `core` | proteus-core | `proteus-core-v.*` | `proteus-core/**` |
| `firebase` | proteus-firebase | `proteus-firebase-v.*` | `proteus-firebase/**` |
| `ui` | proteus-ui | `proteus-ui-v.*` | `proteus-ui/**` |
| `bom` | proteus-bom | `v.*` | N/A (all modules) |
| *omitted* | Multiple modules | — | No path filter |

**Breaking changes** use exclamation mark or footer:

```
feat(core)!: redesign Provider interface for coroutines support

BREAKING CHANGE: Provider now returns Flow<Config> instead of Config.
```

### 1.2 Example Commits

```bash
# New feature in Firebase module
feat(firebase): add real-time config update listener

# Bug fix in core module
fix(core): resolve race condition during parallel config fetches

# Performance improvement in UI
perf(ui): optimize feature list rendering with LazyColumn keys

# Change affecting multiple modules (no scope)
refactor: migrate from callbacks to coroutines across all providers

# Documentation update (hidden from release notes)
docs(firebase): add setup instructions for Firebase configuration

# Dependency update (hidden from release notes)
chore(deps): bump Compose BOM to 2024.02.00

# Breaking change
feat(core)!: remove deprecated ConfigProvider interface
```

### 1.3 Tasks

- [x] **Task 1.3.1**: Update `CONTRIBUTING.md` with commit message guidelines section
- [ ] **Task 1.3.2**: Communicate new commit format to all contributors

### Checkpoint 1

- [x] `CONTRIBUTING.md` contains commit message guidelines
- [x] Team understands the commit format
- [x] You can identify which scope applies to any given change

---

## Phase 2: git-cliff Configuration

### 2.1 Create Base Configuration File

Create `cliff.toml` in the repository root with the following content:

```toml
# =============================================================================
# git-cliff configuration for Proteus
# Generates Retrofit-style release notes with categorized sections
# =============================================================================

[changelog]
# Header for full CHANGELOG.md file
header = """
# Changelog

All notable changes to Proteus will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/),
and this project adheres to [Semantic Versioning](https://semver.org/).

"""

# Template for changelog body - produces Retrofit-style output
body = """
{% if version %}\
    ## [{{ version | trim_start_matches(pat="v") | trim_start_matches(pat="proteus-core-") | trim_start_matches(pat="proteus-firebase-") | trim_start_matches(pat="proteus-ui-") }}] - {{ timestamp | date(format="%Y-%m-%d") }}
{% else %}\
    ## [Unreleased]
{% endif %}\
{% for group, commits in commits | group_by(attribute="group") %}

**{{ group }}**
{% for commit in commits %}
- {% if commit.scope %}**{{ commit.scope }}:** {% endif %}\
{{ commit.message | upper_first }}{% if commit.breaking %} ⚠️{% endif %}\
{% endfor %}\
{% endfor %}

"""

trim = true

footer = """
---
*Generated by [git-cliff](https://git-cliff.org/)*
"""

[git]
# Parse commits according to Conventional Commits spec
conventional_commits = true
filter_unconventional = true
split_commits = false

# Commit parsers - maps types to display groups
commit_parsers = [
    # Breaking changes get special treatment
    { message = "^.*!:", group = "⚠️ Breaking Changes" },
    { body = "BREAKING CHANGE", group = "⚠️ Breaking Changes" },
    
    # Standard type mappings
    { message = "^feat", group = "New" },
    { message = "^fix", group = "Fixed" },
    { message = "^perf", group = "Performance" },
    { message = "^refactor", group = "Changed" },
    
    # Hidden types
    { message = "^doc", skip = true },
    { message = "^docs", skip = true },
    { message = "^test", skip = true },
    { message = "^style", skip = true },
    { message = "^chore", skip = true },
    { message = "^ci", skip = true },
    { message = "^build", skip = true },
]

protect_breaking_commits = true

# Tag pattern matches both module tags and BOM tags
# Module: proteus-core-v1.0.0, proteus-firebase-v1.0.0, proteus-ui-v1.0.0
# BOM: v1.0.0
tag_pattern = "(proteus-(core|firebase|ui)-)?v[0-9]+\\.[0-9]+\\.[0-9]+"

sort_commits = "newest"
filter_commits = true

# Clean up commit messages
commit_preprocessors = [
    { pattern = "\\s*\\(#\\d+\\)$", replace = "" },
    { pattern = "\\s+$", replace = "" },
]
```

### 2.2 Verify Configuration Locally

Install git-cliff and test the configuration:

```bash
# Install git-cliff (macOS)
brew install git-cliff

# Or using cargo
cargo install git-cliff

# Test: Generate changelog for entire project
git cliff --config cliff.toml

# Test: Generate latest release notes only
git cliff --config cliff.toml --latest --strip header

# Test: Generate changelog for core module only
git cliff --config cliff.toml --include-path "proteus-core/**"

# Test: Preview unreleased changes
git cliff --config cliff.toml --unreleased
```

### 2.3 Tasks

- [x] **Task 2.3.1**: Create `cliff.toml` in repository root
- [x] **Task 2.3.2**: Install git-cliff locally
- [x] **Task 2.3.3**: Verify configuration with test commands

### Checkpoint 2

- [x] `cliff.toml` exists in repository root
- [x] `git cliff` runs without errors
- [x] Output groups commits into correct sections
- [x] Hidden types (docs, chore, test) are excluded
- [x] Breaking changes show ⚠️ indicator

---

## Phase 3: Directory Structure and Per-Module Strategy

### 3.1 Target Directory Structure

```
proteus/
├── cliff.toml                    # git-cliff configuration
├── CHANGELOG.md                  # Aggregated changelog (BOM releases)
├── docs/
│   ├── RELEASE_NOTES_GUIDE.md    # Documentation for release process
│   ├── CONVENTIONAL_COMMITS.md   # Commit message reference
│   └── CHANGELOG_AUTOMATION.md   # Technical documentation
├── proteus-core/
│   ├── CHANGELOG.md              # Core-only changes
│   └── build.gradle.kts
├── proteus-firebase/
│   ├── CHANGELOG.md              # Firebase-only changes
│   └── build.gradle.kts
├── proteus-ui/
│   ├── CHANGELOG.md              # UI-only changes
│   └── build.gradle.kts
└── proteus-bom/
    └── build.gradle.kts
```

### 3.2 Module-Specific Commands

Each module uses path filtering and tag pattern matching:

| Module | git-cliff Command |
|--------|-------------------|
| proteus-core | `git cliff --include-path "proteus-core/**" --tag-pattern "proteus-core-v.*"` |
| proteus-firebase | `git cliff --include-path "proteus-firebase/**" --tag-pattern "proteus-firebase-v.*"` |
| proteus-ui | `git cliff --include-path "proteus-ui/**" --tag-pattern "proteus-ui-v.*"` |
| BOM (aggregated) | `git cliff --tag-pattern "^v[0-9]"` |

### 3.3 Optional Gradle Tasks

Add to root `build.gradle.kts` for convenient local changelog generation:

```kotlin
// =============================================================================
// Changelog Generation Tasks
// Requires git-cliff: brew install git-cliff
// =============================================================================

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

// Generate changelog for specific module: ./gradlew generateChangelog -Pmodule=core
tasks.register<Exec>("generateChangelog") {
    group = "documentation"
    description = "Generate CHANGELOG.md for a specific module or BOM"
    
    val moduleName = project.findProperty("module")?.toString() ?: "bom"
    
    doFirst {
        println("Generating changelog for: $moduleName")
    }
    
    commandLine = if (moduleName == "bom") {
        listOf(
            "git", "cliff",
            "--config", "cliff.toml",
            "--tag-pattern", "^v[0-9]",
            "--output", "CHANGELOG.md"
        )
    } else {
        val modulePath = modulePathMap[moduleName] 
            ?: throw GradleException("Unknown module: $moduleName. Valid: ${modulePathMap.keys}")
        val tagPattern = moduleTagPatternMap[moduleName]!!
        listOf(
            "git", "cliff",
            "--config", "cliff.toml",
            "--include-path", "$modulePath/**",
            "--tag-pattern", tagPattern,
            "--output", "$modulePath/CHANGELOG.md"
        )
    }
}

// Generate all changelogs: ./gradlew generateAllChangelogs
tasks.register("generateAllChangelogs") {
    group = "documentation"
    description = "Generate CHANGELOG.md for all modules and BOM"
    
    doLast {
        // Generate BOM changelog
        exec {
            commandLine("git", "cliff",
                "--config", "cliff.toml",
                "--tag-pattern", "^v[0-9]",
                "--output", "CHANGELOG.md"
            )
        }
        println("Generated: CHANGELOG.md (BOM)")
        
        // Generate per-module changelogs
        modulePathMap.forEach { (name, path) ->
            val tagPattern = moduleTagPatternMap[name]!!
            exec {
                commandLine("git", "cliff",
                    "--config", "cliff.toml",
                    "--include-path", "$path/**",
                    "--tag-pattern", tagPattern,
                    "--output", "$path/CHANGELOG.md"
                )
            }
            println("Generated: $path/CHANGELOG.md")
        }
    }
}
```

### 3.4 Tasks

- [x] **Task 3.4.1**: Create `docs/` directory in project root
- [x] **Task 3.4.2**: Add Gradle tasks for changelog generation (optional)
- [x] **Task 3.4.3**: Test module-specific changelog generation

### Checkpoint 3

- [x] `docs/` directory exists
- [x] Each module can generate its own changelog with path filter
- [x] Tag patterns correctly identify module-specific tags
- [x] BOM changelog aggregates all modules

---

## Phase 4: JReleaser Configuration

### 4.1 Configure External Release Notes

Modify JReleaser configuration to read release notes from a file generated by git-cliff.

**For TOML format (`jreleaser.toml`):**

```toml
[release]
[release.github]
owner = "anthropics"
name = "proteus"

# Use external changelog file generated by git-cliff
changelog.enabled = true
changelog.external = "build/release-notes.md"

tagName = "{{ projectVersion }}"
releaseName = "{{ projectVersion }}"
overwrite = false
draft = false
```

**For YAML format (`jreleaser.yml`):**

```yaml
release:
  github:
    owner: anthropics
    name: proteus
    
    changelog:
      enabled: true
      external: build/release-notes.md
    
    tagName: "{{ projectVersion }}"
    releaseName: "{{ projectVersion }}"
    overwrite: false
```

### 4.2 Alternative: Environment Variable Approach

If you prefer not to use intermediate files:

```toml
[release.github]
changelog.enabled = true
changelog.content = "{{ Env.RELEASE_NOTES }}"
```

Then in GitHub Actions:

```yaml
- name: Generate release notes
  run: |
    NOTES=$(git cliff --latest --strip header)
    echo "RELEASE_NOTES<<EOF" >> $GITHUB_ENV
    echo "$NOTES" >> $GITHUB_ENV
    echo "EOF" >> $GITHUB_ENV
```

### 4.3 Tasks

- [ ] **Task 4.3.1**: Add `changelog.external` to JReleaser configuration
- [ ] **Task 4.3.2**: Verify file path `build/release-notes.md` is in `.gitignore`

### Checkpoint 4

- [ ] JReleaser config has `changelog.external` pointing to `build/release-notes.md`
- [ ] The file path is excluded from git
- [ ] JReleaser can read from the external file

---

## Phase 5: GitHub Actions Workflows

### 5.1 Module Release Workflow

Create `.github/workflows/release-module.yml`:

```yaml
name: Release Module

on:
  push:
    tags:
      - 'proteus-core-v*'
      - 'proteus-firebase-v*'
      - 'proteus-ui-v*'

permissions:
  contents: write
  packages: write

env:
  JAVA_VERSION: '17'
  JAVA_DISTRIBUTION: 'temurin'

jobs:
  release:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout repository
        uses: actions/checkout@v4
        with:
          fetch-depth: 0
      
      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: ${{ env.JAVA_DISTRIBUTION }}
      
      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3
      
      # Parse tag: proteus-{module}-v{version}
      - name: Parse release tag
        id: parse_tag
        run: |
          TAG=${GITHUB_REF#refs/tags/}
          echo "tag=$TAG" >> $GITHUB_OUTPUT
          
          # Extract module: proteus-core-v1.0.0 -> core
          MODULE=$(echo $TAG | sed 's/proteus-//' | sed 's/-v.*//')
          echo "module=$MODULE" >> $GITHUB_OUTPUT
          
          # Extract version: proteus-core-v1.0.0 -> 1.0.0
          VERSION=$(echo $TAG | sed 's/.*-v//')
          echo "version=$VERSION" >> $GITHUB_OUTPUT
          
          # Map to directory path
          MODULE_PATH="proteus-$MODULE"
          echo "module_path=$MODULE_PATH" >> $GITHUB_OUTPUT
          
          # Tag pattern for this module
          TAG_PATTERN="proteus-$MODULE-v.*"
          echo "tag_pattern=$TAG_PATTERN" >> $GITHUB_OUTPUT
          
          echo "Releasing $MODULE v$VERSION from $MODULE_PATH"
      
      - name: Install git-cliff
        uses: taiki-e/install-action@v2
        with:
          tool: git-cliff
      
      - name: Generate release notes
        run: |
          mkdir -p build
          
          git cliff \
            --config cliff.toml \
            --include-path "${{ steps.parse_tag.outputs.module_path }}/**" \
            --tag-pattern "${{ steps.parse_tag.outputs.tag_pattern }}" \
            --latest \
            --strip header \
            --output build/release-notes.md
          
          echo "Generated release notes:"
          cat build/release-notes.md
      
      - name: Verify release notes
        run: |
          if [ ! -s build/release-notes.md ]; then
            echo "No conventional commits found for this release." > build/release-notes.md
          fi
      
      - name: Update module changelog
        run: |
          git cliff \
            --config cliff.toml \
            --include-path "${{ steps.parse_tag.outputs.module_path }}/**" \
            --tag-pattern "${{ steps.parse_tag.outputs.tag_pattern }}" \
            --output "${{ steps.parse_tag.outputs.module_path }}/CHANGELOG.md"
      
      - name: Commit updated changelog
        run: |
          git config user.name 'github-actions[bot]'
          git config user.email 'github-actions[bot]@users.noreply.github.com'
          
          git add "${{ steps.parse_tag.outputs.module_path }}/CHANGELOG.md"
          
          if git diff --staged --quiet; then
            echo "No changelog changes to commit"
          else
            git commit -m "docs(${{ steps.parse_tag.outputs.module }}): update CHANGELOG for v${{ steps.parse_tag.outputs.version }}"
            git push origin HEAD:main
          fi
      
      - name: Build module
        run: ./gradlew :${{ steps.parse_tag.outputs.module_path }}:build
      
      - name: Run JReleaser
        env:
          JRELEASER_GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          JRELEASER_GPG_SECRET_KEY: ${{ secrets.GPG_SECRET_KEY }}
          JRELEASER_GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
          JRELEASER_MAVENCENTRAL_USERNAME: ${{ secrets.MAVEN_USERNAME }}
          JRELEASER_MAVENCENTRAL_TOKEN: ${{ secrets.MAVEN_TOKEN }}
        run: ./gradlew :${{ steps.parse_tag.outputs.module_path }}:jreleaserFullRelease
```

### 5.2 BOM Release Workflow

Create `.github/workflows/release-bom.yml`:

```yaml
name: Release BOM

on:
  push:
    tags:
      - 'v[0-9]*'

permissions:
  contents: write
  packages: write

env:
  JAVA_VERSION: '17'
  JAVA_DISTRIBUTION: 'temurin'

jobs:
  release:
    runs-on: ubuntu-latest
    
    steps:
      - name: Checkout repository
        uses: actions/checkout@v4
        with:
          fetch-depth: 0
      
      - name: Set up JDK
        uses: actions/setup-java@v4
        with:
          java-version: ${{ env.JAVA_VERSION }}
          distribution: ${{ env.JAVA_DISTRIBUTION }}
      
      - name: Setup Gradle
        uses: gradle/actions/setup-gradle@v3
      
      - name: Parse release tag
        id: parse_tag
        run: |
          TAG=${GITHUB_REF#refs/tags/}
          VERSION=$(echo $TAG | sed 's/^v//')
          echo "tag=$TAG" >> $GITHUB_OUTPUT
          echo "version=$VERSION" >> $GITHUB_OUTPUT
          echo "Releasing BOM v$VERSION"
      
      - name: Install git-cliff
        uses: taiki-e/install-action@v2
        with:
          tool: git-cliff
      
      - name: Generate release notes
        run: |
          mkdir -p build
          
          # BOM uses simple v* pattern and includes all modules
          git cliff \
            --config cliff.toml \
            --tag-pattern "^v[0-9]" \
            --latest \
            --strip header \
            --output build/release-notes.md
          
          echo "Generated release notes:"
          cat build/release-notes.md
      
      - name: Add version table to release notes
        run: |
          cat << 'EOF' > build/release-notes-header.md
          ## Included Versions
          
          | Module | Version |
          |--------|---------|
          EOF
          
          for module in proteus-core proteus-firebase proteus-ui; do
            if [ -f "$module/build.gradle.kts" ]; then
              version=$(grep 'version\s*=' "$module/build.gradle.kts" | head -1 | sed 's/.*"\(.*\)".*/\1/')
              echo "| $module | $version |" >> build/release-notes-header.md
            fi
          done
          
          echo "" >> build/release-notes-header.md
          echo "## Changes" >> build/release-notes-header.md
          echo "" >> build/release-notes-header.md
          
          cat build/release-notes-header.md build/release-notes.md > build/release-notes-combined.md
          mv build/release-notes-combined.md build/release-notes.md
      
      - name: Update root changelog
        run: |
          git cliff \
            --config cliff.toml \
            --tag-pattern "^v[0-9]" \
            --output CHANGELOG.md
      
      - name: Commit updated changelog
        run: |
          git config user.name 'github-actions[bot]'
          git config user.email 'github-actions[bot]@users.noreply.github.com'
          
          git add CHANGELOG.md
          
          if git diff --staged --quiet; then
            echo "No changelog changes to commit"
          else
            git commit -m "docs(bom): update CHANGELOG for v${{ steps.parse_tag.outputs.version }}"
            git push origin HEAD:main
          fi
      
      - name: Build BOM
        run: ./gradlew :proteus-bom:build
      
      - name: Run JReleaser
        env:
          JRELEASER_GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
          JRELEASER_GPG_SECRET_KEY: ${{ secrets.GPG_SECRET_KEY }}
          JRELEASER_GPG_PASSPHRASE: ${{ secrets.GPG_PASSPHRASE }}
          JRELEASER_MAVENCENTRAL_USERNAME: ${{ secrets.MAVEN_USERNAME }}
          JRELEASER_MAVENCENTRAL_TOKEN: ${{ secrets.MAVEN_TOKEN }}
        run: ./gradlew :proteus-bom:jreleaserFullRelease
```

### 5.3 Tasks

- [ ] **Task 5.3.1**: Create `.github/workflows/release-module.yml`
- [ ] **Task 5.3.2**: Create `.github/workflows/release-bom.yml`
- [ ] **Task 5.3.3**: Test workflow with a minor release

### Checkpoint 5

- [ ] Module release workflow handles `proteus-{module}-v*` tags correctly
- [ ] BOM release workflow handles `v*` tags correctly
- [ ] Release notes are generated before JReleaser runs
- [ ] CHANGELOG.md files are updated and committed

---

## Phase 6: Documentation Generation

### 6.1 Create Documentation Files

Generate comprehensive documentation in `docs/` directory.

**File: `docs/RELEASE_NOTES_GUIDE.md`**

```markdown
# Proteus Release Notes Guide

This guide explains how release notes are automatically generated for Proteus releases.

## Overview

Proteus uses [git-cliff](https://git-cliff.org/) to automatically generate release notes from commit messages. The release notes follow the style used by popular libraries like [Retrofit](https://github.com/square/retrofit/releases).

## How It Works

1. **Commit Messages**: Contributors write commit messages following the [Conventional Commits](https://www.conventionalcommits.org/) specification
2. **Tag Creation**: When a release tag is pushed, GitHub Actions triggers the release workflow
3. **Changelog Generation**: git-cliff parses commits since the last tag and generates formatted release notes
4. **GitHub Release**: JReleaser creates a GitHub Release with the generated notes
5. **CHANGELOG Update**: The module's CHANGELOG.md file is updated and committed

## Release Types

### Module Releases

Individual modules can be released independently using tags like:
- `proteus-core-v1.0.0`
- `proteus-firebase-v1.0.0`
- `proteus-ui-v1.0.0`

Module releases include only changes affecting that specific module.

### BOM Releases

The Bill of Materials (BOM) represents a coordinated release of all modules. BOM releases use tags like:
- `v1.0.0`

BOM release notes aggregate all changes across all modules since the last BOM release.

## Previewing Release Notes

Before creating a release, you can preview what the release notes will look like:

```bash
# Preview unreleased changes
git cliff --unreleased --strip header

# Preview changes for a specific module
git cliff --include-path "proteus-core/**" --unreleased --strip header

# Preview changes between two tags
git cliff proteus-core-v1.0.0..proteus-core-v1.1.0 --strip header
```

## Troubleshooting

### Empty Release Notes

If release notes are empty, check that:
1. Commits follow the Conventional Commits format
2. Commits include types that are not hidden (feat, fix, perf, refactor)
3. The tag pattern matches your release tag

### Wrong Commits Included

If wrong commits appear in release notes:
1. Verify the tag pattern is correct for your module
2. Check the path filter includes the right directories
3. Ensure previous release tags exist and are reachable
```

**File: `docs/CONVENTIONAL_COMMITS.md`**

```markdown
# Conventional Commits Reference for Proteus

This document provides a quick reference for writing commit messages that work with our automated changelog generation.

## Format

```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

## Types

| Type | Section | Use For |
|------|---------|---------|
| `feat` | **New** | New features, capabilities, API additions |
| `fix` | **Fixed** | Bug fixes, corrections |
| `perf` | **Performance** | Performance improvements |
| `refactor` | **Changed** | Code restructuring |
| `docs` | *hidden* | Documentation changes |
| `test` | *hidden* | Test additions/changes |
| `chore` | *hidden* | Maintenance, dependencies |
| `style` | *hidden* | Formatting, whitespace |
| `ci` | *hidden* | CI/CD changes |
| `build` | *hidden* | Build system changes |

## Scopes

| Scope | Module |
|-------|--------|
| `core` | proteus-core |
| `firebase` | proteus-firebase |
| `ui` | proteus-ui |
| `bom` | proteus-bom |
| `deps` | Dependencies |
| *(omit)* | Multiple modules |

## Examples

### Features

```bash
feat(firebase): add real-time config update listener
feat(ui): implement search in feature list
feat(core): add suspend function variants for all providers
```

### Bug Fixes

```bash
fix(core): resolve race condition in config refresh
fix(ui): correct dark mode colors in feature editor
fix(firebase): handle null values from Remote Config
```

### Breaking Changes

```bash
feat(core)!: redesign Provider interface for coroutines

BREAKING CHANGE: Provider now uses suspend functions.
See migration guide in docs/migration-v2.md
```

### Multi-Module Changes

```bash
refactor: migrate all modules to Kotlin 2.0
```

### Hidden Changes (Won't Appear in Release Notes)

```bash
docs: update README installation instructions
test(core): add unit tests for ConfigCache
chore(deps): bump Compose BOM to 2024.06.00
ci: add release automation workflow
```

## Tips

1. **Keep descriptions concise**: The first line should be under 72 characters
2. **Use imperative mood**: "add feature" not "added feature"
3. **Scope appropriately**: Use module scope when change is module-specific
4. **Document breaking changes**: Always include migration guidance in the body
```

**File: `docs/CHANGELOG_AUTOMATION.md`**

```markdown
# Changelog Automation Technical Documentation

This document describes the technical implementation of automated changelog generation in Proteus.

## Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Conventional   │────▶│    git-cliff    │────▶│   JReleaser     │
│    Commits      │     │   (changelog)   │     │ (GitHub Release)│
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

## Configuration Files

### cliff.toml

Located at repository root, this file configures git-cliff:

- **changelog.body**: Jinja2 template for rendering release notes
- **git.commit_parsers**: Rules for categorizing commits
- **git.tag_pattern**: Regex for identifying version tags

### JReleaser Configuration

The `changelog.external` property tells JReleaser to read release notes from a file:

```toml
[release.github]
changelog.external = "build/release-notes.md"
```

## Tag Patterns

| Release Type | Tag Pattern | Example | Regex |
|--------------|-------------|---------|-------|
| Core Module | `proteus-core-v*` | `proteus-core-v1.0.0` | `proteus-core-v[0-9]+\\.[0-9]+\\.[0-9]+` |
| Firebase Module | `proteus-firebase-v*` | `proteus-firebase-v1.0.0` | `proteus-firebase-v[0-9]+\\.[0-9]+\\.[0-9]+` |
| UI Module | `proteus-ui-v*` | `proteus-ui-v1.0.0` | `proteus-ui-v[0-9]+\\.[0-9]+\\.[0-9]+` |
| BOM | `v*` | `v1.0.0` | `^v[0-9]+\\.[0-9]+\\.[0-9]+` |

## Path Filters

Per-module changelogs use `--include-path` to filter commits:

| Module | Filter |
|--------|--------|
| proteus-core | `proteus-core/**` |
| proteus-firebase | `proteus-firebase/**` |
| proteus-ui | `proteus-ui/**` |

## GitHub Actions Workflows

### release-module.yml

Triggered by: `proteus-{module}-v*` tags

Steps:
1. Parse tag to extract module name and version
2. Generate module-specific release notes with path filter
3. Update module's CHANGELOG.md
4. Commit changelog changes
5. Build module
6. Run JReleaser

### release-bom.yml

Triggered by: `v*` tags

Steps:
1. Parse tag to extract version
2. Generate aggregated release notes (all modules)
3. Add version table header
4. Update root CHANGELOG.md
5. Commit changelog changes
6. Build BOM
7. Run JReleaser

## Local Development

### Preview Commands

```bash
# All unreleased changes
git cliff --unreleased

# Module-specific unreleased changes
git cliff --include-path "proteus-core/**" --unreleased

# Changes between versions
git cliff proteus-core-v1.0.0..proteus-core-v1.1.0

# Generate full changelog
git cliff --output CHANGELOG.md
```

### Gradle Tasks

```bash
# Generate changelog for specific module
./gradlew generateChangelog -Pmodule=core

# Generate all changelogs
./gradlew generateAllChangelogs
```

## Troubleshooting

### Issue: Empty changelog output

**Cause**: No commits matching conventional format since last tag

**Solution**: Check commit messages follow `type(scope): description` format

### Issue: Wrong commits in module changelog

**Cause**: Path filter not matching or commits touching multiple modules

**Solution**: Verify `--include-path` argument matches module directory structure

### Issue: Tag not recognized

**Cause**: Tag doesn't match the `tag_pattern` regex

**Solution**: Ensure tag follows exact pattern: `proteus-{module}-v{semver}` or `v{semver}`
```

### 6.2 Tasks

- [ ] **Task 6.2.1**: Create `docs/RELEASE_NOTES_GUIDE.md`
- [ ] **Task 6.2.2**: Create `docs/CONVENTIONAL_COMMITS.md`
- [ ] **Task 6.2.3**: Create `docs/CHANGELOG_AUTOMATION.md`

### Checkpoint 6

- [ ] All three documentation files exist in `docs/`
- [ ] Documentation is accurate and matches implementation
- [ ] Links and examples are correct

---

## Phase 7: Migration and Edge Cases

### 7.1 Migration Strategy for Existing Commits

Choose one of these approaches:

**Option A: Clean Break (Recommended)**

Start fresh from a specific tag. Create an initial manual changelog entry for all previous releases:

```markdown
# Changelog

## [0.0.4] - 2024-XX-XX

Initial tracked release. See [GitHub Releases](link) for earlier history.

---
*Automated changelog generation begins with this release.*
```

**Option B: Retroactive Parsing**

Add preprocessors to `cliff.toml` that convert old commit styles:

```toml
commit_preprocessors = [
    { pattern = "^\\[CORE\\]\\s*", replace = "feat(core): " },
    { pattern = "^\\[FIX\\]\\s*", replace = "fix: " },
]
```

### 7.2 Handling Multi-Module Commits

When a commit affects multiple modules:

**Approach A: Omit Scope**

```bash
git commit -m "refactor: migrate all providers to coroutines"
```

This appears in BOM changelog and in each module's changelog if files in that module were modified.

**Approach B: Separate Commits**

```bash
git commit -m "refactor(core): add coroutine support to Provider"
git commit -m "refactor(firebase): migrate to coroutine-based Provider"
git commit -m "refactor(ui): update UI to use suspend functions"
```

### 7.3 Breaking Changes

Use the `!` indicator and include migration guidance:

```bash
git commit -m "feat(core)!: remove deprecated ConfigProvider

BREAKING CHANGE: ConfigProvider has been removed.
Use Provider interface instead.
See docs/migration.md for migration steps."
```

### 7.4 Tasks

- [ ] **Task 7.4.1**: Decide on migration strategy (clean break or retroactive)
- [ ] **Task 7.4.2**: Document multi-module commit policy for team
- [ ] **Task 7.4.3**: Test breaking change formatting

### Checkpoint 7

- [ ] Migration strategy is decided and documented
- [ ] Team understands multi-module commit handling
- [ ] Breaking change format is tested

---

## Phase 8: Local Development Setup

### 8.1 Commit Message Validation Hook

Create `.githooks/commit-msg`:

```bash
#!/bin/bash

commit_msg_file=$1
commit_msg=$(cat "$commit_msg_file")

# Conventional Commits pattern
pattern="^(feat|fix|docs|style|refactor|perf|test|chore|ci|build)(\([a-z]+\))?(!)?: .{1,}"

if ! echo "$commit_msg" | grep -qE "$pattern"; then
    echo "❌ Commit message does not follow Conventional Commits format"
    echo ""
    echo "Expected: <type>(<scope>): <description>"
    echo ""
    echo "Types: feat, fix, docs, style, refactor, perf, test, chore, ci, build"
    echo "Scopes: core, firebase, ui, bom, deps (optional)"
    echo ""
    echo "Examples:"
    echo "  feat(firebase): add real-time config updates"
    echo "  fix(core): resolve race condition"
    echo ""
    echo "Your message: $commit_msg"
    exit 1
fi
```

Setup:

```bash
chmod +x .githooks/commit-msg
git config core.hooksPath .githooks
```

### 8.2 IDE Setup

For IntelliJ IDEA / Android Studio:

1. Install "Conventional Commit" plugin from Marketplace
2. Use plugin UI when creating commits
3. Plugin provides type/scope dropdowns and validation

### 8.3 Tasks

- [ ] **Task 8.3.1**: Create `.githooks/commit-msg` hook
- [ ] **Task 8.3.2**: Add hook setup instructions to `CONTRIBUTING.md`
- [ ] **Task 8.3.3**: Document IDE plugin recommendation

### Checkpoint 8

- [ ] Commit hook validates message format
- [ ] Hook setup is documented
- [ ] IDE integration is documented

---

## Implementation Summary

### Files to Create

| File | Purpose |
|------|---------|
| `cliff.toml` | git-cliff configuration |
| `.github/workflows/release-module.yml` | Module release workflow |
| `.github/workflows/release-bom.yml` | BOM release workflow |
| `.githooks/commit-msg` | Commit validation hook |
| `docs/RELEASE_NOTES_GUIDE.md` | User documentation |
| `docs/CONVENTIONAL_COMMITS.md` | Commit reference |
| `docs/CHANGELOG_AUTOMATION.md` | Technical documentation |

### Files to Modify

| File | Changes |
|------|---------|
| `CONTRIBUTING.md` | Add commit message guidelines |
| `jreleaser.toml` or `.yml` | Add `changelog.external` |
| `build.gradle.kts` | Add changelog tasks (optional) |
| `.gitignore` | Ensure `build/` is ignored |

### Files Generated by Automation

| File | Generated By |
|------|--------------|
| `CHANGELOG.md` | BOM releases |
| `proteus-core/CHANGELOG.md` | Core releases |
| `proteus-firebase/CHANGELOG.md` | Firebase releases |
| `proteus-ui/CHANGELOG.md` | UI releases |
| `build/release-notes.md` | Each release (temporary) |

---

## Quick Reference Commands

```bash
# Install git-cliff
brew install git-cliff

# Preview all unreleased changes
git cliff --unreleased --strip header

# Preview module-specific changes
git cliff --include-path "proteus-core/**" --unreleased --strip header

# Generate full changelog
git cliff --output CHANGELOG.md

# Generate module changelog
git cliff --include-path "proteus-core/**" --tag-pattern "proteus-core-v.*" --output proteus-core/CHANGELOG.md

# Setup commit hooks
git config core.hooksPath .githooks
chmod +x .githooks/commit-msg

# Create module release tag
git tag proteus-core-v1.0.0
git push origin proteus-core-v1.0.0

# Create BOM release tag
git tag v1.0.0
git push origin v1.0.0
```

---

## Execution Order

Follow these phases in order, completing each checkpoint before proceeding:

1. **Phase 1**: Establish Conventional Commits specification
2. **Phase 2**: Create and test git-cliff configuration
3. **Phase 3**: Set up directory structure and per-module strategy
4. **Phase 4**: Configure JReleaser for external release notes
5. **Phase 5**: Create GitHub Actions workflows
6. **Phase 6**: Generate documentation
7. **Phase 7**: Handle migration and edge cases
8. **Phase 8**: Set up local development tooling

After completing all phases, test with a minor patch release of one module before doing a full BOM release.