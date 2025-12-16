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

### 4.1 JReleaser Gradle Plugin Configuration

Proteus uses JReleaser Gradle plugin (not external config files) with direct CHANGELOG.md file references. This approach is more efficient as it integrates directly with the build system.

**BOM Module Configuration (`proteus-bom/build.gradle.kts`):**

```kotlin
jreleaser {
    project {
        name = artifactName
        version = artifactVersion
        // ... other project config
    }

    release {
        github {
            enabled = true
            tagName = "${artifactName}-v${artifactVersion}"
            releaseName = "v${artifactVersion}"
            overwrite.set(true)

            changelog {
                enabled = true
                external = File(rootProject.rootDir, "CHANGELOG.md")
                append.enabled = false
            }
        }
    }
    // ... maven deployment config
}
```

**Individual Module Configuration (e.g., `proteus-core/build.gradle.kts`):**

```kotlin
jreleaser {
    project {
        name = artifactName
        version = artifactVersion
        // ... other project config
    }

    release {
        github {
            enabled = true
            tagName = "${artifactName}-v${artifactVersion}"
            releaseName = "${artifactName} v${artifactVersion}"
            overwrite.set(true)

            changelog {
                enabled = true
                external = File(rootProject.rootDir, "proteus-core/CHANGELOG.md")
                append.enabled = false
            }
        }
    }
    // ... maven deployment config
}
```

### 4.2 Changelog File Strategy

**File Locations:**
- BOM releases: Read from `CHANGELOG.md` (root level, aggregated changelog)
- Module releases: Read from `{module}/CHANGELOG.md` (module-specific changelog)

**Workflow:**
1. GitHub Actions generates/updates CHANGELOG files using git-cliff
2. JReleaser reads from these files during release
3. GitHub Release is created with the changelog content

### 4.3 Build Directory Exclusion

Ensure build directories are excluded from git tracking:

**.gitignore update:**
```diff
+*/build
```

### 4.4 Tasks

- [x] **Task 4.4.1**: JReleaser Gradle plugin already configured in all modules
- [x] **Task 4.4.2**: External changelog configuration already set up
- [x] **Task 4.4.3**: Verify build directories are excluded from git

### Checkpoint 4

- [x] JReleaser config uses Gradle DSL with `changelog.external` pointing to CHANGELOG files
- [x] Build directories are excluded from git
- [x] JReleaser can read from the external CHANGELOG files
- [x] Each module has appropriate tag and release name configuration

---

## Phase 5: GitHub Actions Workflows

### 5.1 Existing Workflow Integration

Proteus uses a sophisticated workflow system with manual control and staged deployments. The existing workflows have been enhanced with git-cliff integration while preserving the original architecture.

**Existing Workflows:**
- `release-specific-module.yml` - Individual module releases with flexible staging
- `release-bom-only.yml` - BOM-only releases for coordinated versions
- `release-all-with-bom.yml` - Complete release of all modules + BOM (triggered by `v*` tags)
- `release-all-dry-run.yml` - Testing and validation

### 5.2 Git-Cliff Integration Strategy

**Integration Points:**
1. **git-cliff installation** added to all release workflows
2. **Changelog generation** before JReleaser execution
3. **Module-specific filtering** using path and tag patterns
4. **Fallback handling** for cases where git-cliff produces no output

**Key Integration Steps Added:**

```yaml
# Install git-cliff in all workflows
- name: Install git-cliff
  uses: taiki-e/install-action@v2
  with:
    tool: git-cliff

# For individual modules (release-specific-module.yml)
- name: Generate proteus-core changelog
  if: inputs.module == 'proteus-core' || inputs.module == 'all'
  run: |
    git cliff \
      --config cliff.toml \
      --include-path "proteus-core/**" \
      --tag-pattern "proteus-core-v.*" \
      --output proteus-core/CHANGELOG.md
    echo "✅ Generated proteus-core CHANGELOG.md"

# For BOM releases (release-bom-only.yml)
- name: Generate BOM changelog
  run: |
    git cliff \
      --config cliff.toml \
      --tag-pattern "^v[0-9]" \
      --output CHANGELOG.md

    if [ -f CHANGELOG.md ]; then
      echo "✅ BOM changelog generated successfully"
    else
      echo "⚠️ Creating minimal changelog"
      echo "# Changelog" > CHANGELOG.md
      echo "## [${{ inputs.bom_version }}] - $(date '+%Y-%m-%d')" >> CHANGELOG.md
    fi

# For complete releases (release-all-with-bom.yml)
- name: Generate all module changelogs
  run: |
    # Generate individual module changelogs
    git cliff --config cliff.toml --include-path "proteus-core/**" --tag-pattern "proteus-core-v.*" --output proteus-core/CHANGELOG.md
    git cliff --config cliff.toml --include-path "proteus-firebase/**" --tag-pattern "proteus-firebase-v.*" --output proteus-firebase/CHANGELOG.md
    git cliff --config cliff.toml --include-path "proteus-ui/**" --tag-pattern "proteus-ui-v.*" --output proteus-ui/CHANGELOG.md

    # Generate BOM changelog (aggregated)
    git cliff --config cliff.toml --tag-pattern "^v[0-9]" --output CHANGELOG.md
```

### 5.3 Workflow Capabilities Preserved

**Manual Control Features:**
- ✅ **Staged Deployments**: UPLOAD → PUBLISH → FULL stages maintained
- ✅ **Module Selection**: Individual or bulk module releases
- ✅ **Version Control**: Manual version input through `publish.properties`
- ✅ **Deployment ID Management**: For Maven Central staged releases
- ✅ **Dry Run Testing**: Validation workflow remains unchanged

**Enhanced Features:**
- ✅ **Automatic Changelogs**: Generated before each release
- ✅ **Module-Specific Notes**: Filtered by path and tag patterns
- ✅ **Conventional Commits**: Properly categorized release notes
- ✅ **Fallback Handling**: Graceful degradation if git-cliff fails

### 5.4 Release Patterns

**Individual Module Release:**
```bash
# Workflow: release-specific-module.yml
# Trigger: Manual with module selection
# Generates: {module}/CHANGELOG.md
# JReleaser reads: File(rootProject.rootDir, "proteus-{module}/CHANGELOG.md")
```

**BOM Release:**
```bash
# Workflow: release-bom-only.yml
# Trigger: Manual with version coordination
# Generates: CHANGELOG.md (aggregated)
# JReleaser reads: File(rootProject.rootDir, "CHANGELOG.md")
```

**Complete Release:**
```bash
# Workflow: release-all-with-bom.yml
# Trigger: v* tag push or manual
# Generates: All CHANGELOG files
# JReleaser reads: Respective files for each module
```

### 5.5 Tasks

- [x] **Task 5.5.1**: Integrate git-cliff into `release-specific-module.yml`
- [x] **Task 5.5.2**: Integrate git-cliff into `release-bom-only.yml`
- [x] **Task 5.5.3**: Integrate git-cliff into `release-all-with-bom.yml`
- [x] **Task 5.5.4**: Preserve existing workflow capabilities and staging

### Checkpoint 5

- [x] Module release workflow generates module-specific changelogs
- [x] BOM release workflow generates aggregated changelog
- [x] Complete release workflow generates all changelogs
- [x] CHANGELOG files are generated before JReleaser execution
- [x] Existing staging and manual control features preserved
- [x] Fallback handling for git-cliff failures implemented

---

## Phase 6: Documentation Generation

### 6.1 Documentation Strategy

Generate comprehensive documentation that reflects the actual Proteus implementation:
- **Gradle DSL JReleaser configuration** (not external config files)
- **Manual workflow system** with flexible staging support
- **Module-specific approach** with path filtering
- **Direct CHANGELOG.md integration** (not temporary build files)

### 6.2 Documentation Files

**User Documentation:**
- `docs/RELEASE_NOTES_GUIDE.md` - Complete guide to Proteus release workflows
- `docs/CONVENTIONAL_COMMITS.md` - Commit message reference with examples

**Technical Documentation:**
- `docs/CHANGELOG_AUTOMATION.md` - Implementation details and troubleshooting

### 6.3 Key Documentation Features

**Accurate Implementation Reflection:**
- ✅ Documents actual Gradle-based JReleaser setup
- ✅ Covers manual workflow dispatch system
- ✅ Explains staging support (UPLOAD/PUBLISH/FULL)
- ✅ Includes troubleshooting for common issues

**Practical Examples:**
- ✅ Real workflow commands and parameters
- ✅ Module-specific filtering examples
- ✅ Local development and testing procedures

**Documentation Contents:**

**`docs/RELEASE_NOTES_GUIDE.md`** covers:
- Manual workflow system (release-specific-module.yml, release-bom-only.yml, release-all-with-bom.yml)
- Staging support (UPLOAD → PUBLISH → FULL)
- Module selection flexibility
- Local preview commands
- Troubleshooting workflow failures

**`docs/CONVENTIONAL_COMMITS.md`** covers:
- Complete type and scope reference
- Breaking change guidelines
- Module-specific examples
- Integration with git-cliff
- Commit hook validation

**`docs/CHANGELOG_AUTOMATION.md`** covers:
- Technical architecture overview
- cliff.toml and Gradle DSL configuration
- Workflow integration points
- Path filtering strategy
- Tag pattern matching
- Performance and security considerations

### 6.4 Tasks

- [x] **Task 6.4.1**: Create `docs/RELEASE_NOTES_GUIDE.md`
- [x] **Task 6.4.2**: Create `docs/CONVENTIONAL_COMMITS.md`
- [x] **Task 6.4.3**: Create `docs/CHANGELOG_AUTOMATION.md`

### Checkpoint 6

- [x] All three documentation files exist in `docs/`
- [x] Documentation accurately matches Gradle-based implementation
- [x] Examples reflect actual workflow patterns and commands
- [x] Technical details match current cliff.toml and JReleaser setup

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

- [x] **Task 7.4.1**: Decide on migration strategy (Option A: clean break chosen)
- [x] **Task 7.4.2**: Document multi-module commit policy (Option A: omit scope chosen)
- [x] **Task 7.4.3**: Create initial CHANGELOG files with clean break approach

### Checkpoint 7

- [x] Migration strategy is decided and implemented (clean break)
- [x] Multi-module commit handling documented (omit scope approach)
- [x] Initial CHANGELOG files created for all modules
- [x] CONTRIBUTING.md and docs/CONVENTIONAL_COMMITS.md updated with guidelines

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

- [x] **Task 8.3.1**: Create `.githooks/commit-msg` hook
- [x] **Task 8.3.2**: Add hook setup instructions to `CONTRIBUTING.md`
- [x] **Task 8.3.3**: Document IDE plugin recommendation

### Checkpoint 8

- [x] Commit hook validates message format
- [x] Hook setup is documented
- [x] IDE integration is documented

---

## Implementation Summary

### Files Created

| File | Purpose | Status |
|------|---------|--------|
| `cliff.toml` | git-cliff configuration | ✅ Created |
| `.githooks/commit-msg` | Commit validation hook | ✅ Created |
| `docs/RELEASE_NOTES_GUIDE.md` | User documentation | ✅ Created |
| `docs/CHANGELOG_AUTOMATION.md` | Technical documentation | ✅ Created |
| `CHANGELOG.md` | Initial BOM changelog (clean break) | ✅ Created |
| `proteus-core/CHANGELOG.md` | Initial core module changelog | ✅ Created |
| `proteus-firebase/CHANGELOG.md` | Initial firebase module changelog | ✅ Created |
| `proteus-ui/CHANGELOG.md` | Initial UI module changelog | ✅ Created |

### Files Modified

| File | Changes | Status |
|------|---------|--------|
| `CONTRIBUTING.md` | Added commit message guidelines and local development setup | ✅ Updated |
| `docs/CONVENTIONAL_COMMITS.md` | Enhanced with multi-module guidelines | ✅ Updated |
| `build.gradle.kts` (root) | Added changelog generation tasks | ✅ Updated |
| `.gitignore` | Added `*/build` exclusion | ✅ Updated |
| `.github/workflows/release-specific-module.yml` | Integrated git-cliff for module releases | ✅ Updated |
| `.github/workflows/release-bom-only.yml` | Integrated git-cliff for BOM releases | ✅ Updated |
| `.github/workflows/release-all-with-bom.yml` | Integrated git-cliff for complete releases | ✅ Updated |

### Files Generated by Automation (Future)

| File | Generated By |
|------|--------------|
| `CHANGELOG.md` | BOM releases (updated via git-cliff) |
| `proteus-core/CHANGELOG.md` | Core releases (updated via git-cliff) |
| `proteus-firebase/CHANGELOG.md` | Firebase releases (updated via git-cliff) |
| `proteus-ui/CHANGELOG.md` | UI releases (updated via git-cliff) |

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