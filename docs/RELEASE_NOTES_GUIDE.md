# Proteus Release Notes Guide

This guide explains how release notes are automatically generated for Proteus releases using your sophisticated workflow system.

## Overview

Proteus uses [git-cliff](https://git-cliff.org/) integrated with GitHub Actions workflows to automatically generate release notes from conventional commit messages. The system supports flexible staging, individual module releases, and coordinated BOM releases.

## How It Works

1. **Commit Messages**: Contributors write commit messages following the [Conventional Commits](https://www.conventionalcommits.org/) specification
2. **Workflow Trigger**: Manual workflow dispatch or tag push triggers release workflows
3. **Changelog Generation**: git-cliff generates module-specific or aggregated changelogs
4. **Staging Support**: Releases can be staged (UPLOAD → PUBLISH → FULL) through Maven Central
5. **GitHub Release**: JReleaser creates GitHub Releases with generated changelog content

## Release Workflows

### Individual Module Release

**Workflow**: `release-specific-module.yml`
**Trigger**: Manual dispatch with module selection
**Supports**: proteus-core, proteus-firebase, proteus-ui, or all modules

```bash
# Manual release of a specific module
# GitHub Actions → release-specific-module.yml
# Select module: proteus-core
# Select stage: UPLOAD/PUBLISH/FULL
# Enter version: 1.0.0
```

**Generated Files:**
- `proteus-{module}/CHANGELOG.md` - Module-specific changes
- GitHub Release with module-filtered changelog

### BOM Release

**Workflow**: `release-bom-only.yml`
**Trigger**: Manual dispatch with coordinated versioning
**Purpose**: Coordinate versions across all modules

```bash
# Manual BOM release
# GitHub Actions → release-bom-only.yml
# BOM version: 1.0.0
# Core version: 1.0.0
# Firebase version: 1.0.0
# UI version: 1.0.0
```

**Generated Files:**
- `CHANGELOG.md` - Aggregated changes across all modules
- GitHub Release with coordinated version information

### Complete Release

**Workflow**: `release-all-with-bom.yml`
**Trigger**: `v*` tag push or manual dispatch
**Purpose**: Release all modules + BOM together

```bash
# Tag-triggered complete release
git tag v1.0.0
git push origin v1.0.0

# Or manual dispatch with version input
```

**Generated Files:**
- `CHANGELOG.md` - BOM changelog (aggregated)
- `proteus-core/CHANGELOG.md` - Core-specific changes
- `proteus-firebase/CHANGELOG.md` - Firebase-specific changes
- `proteus-ui/CHANGELOG.md` - UI-specific changes
- GitHub Releases for all modules

## Staging Support

All workflows support Maven Central staged deployments:

1. **UPLOAD Stage**: Uploads artifacts to staging repository
2. **PUBLISH Stage**: Publishes from staging (requires deployment_id)
3. **FULL Stage**: Complete upload + publish in one step

## Previewing Release Notes

Before creating a release, preview what the release notes will look like:

```bash
# Preview unreleased changes for all modules
git cliff --unreleased --strip header

# Preview changes for a specific module
git cliff --include-path "proteus-core/**" --tag-pattern "proteus-core-v.*" --unreleased --strip header

# Preview BOM changes (aggregated)
git cliff --tag-pattern "^v[0-9]" --unreleased --strip header
```

## Local Changelog Generation

Use the Gradle tasks for local development:

```bash
# Generate changelog for specific module
./gradlew generateChangelog -Pmodule=core

# Generate changelogs for all modules
./gradlew generateAllChangelogs
```

## Troubleshooting

### Empty Release Notes

If release notes are empty:
1. Ensure commits follow Conventional Commits format (`type(scope): description`)
2. Check that commits use visible types: `feat`, `fix`, `perf`, `refactor`
3. Verify commits exist since the last release tag
4. Hidden types won't appear: `docs`, `chore`, `test`, `ci`, `build`, `style`

### Module-Specific Issues

If wrong commits appear in module changelogs:
1. Verify path filtering: `--include-path "proteus-{module}/**"`
2. Check tag pattern: `proteus-{module}-v.*`
3. Ensure commits modify files in the correct module directory

### Workflow Failures

Common workflow issues:
1. **Deployment ID required**: PUBLISH stage needs deployment_id from previous UPLOAD
2. **Version conflicts**: Ensure versions in `publish.properties` are consistent
3. **Secrets missing**: Verify GPG and Maven Central secrets are configured

## Version Coordination

**Individual Modules**: Each module has independent versioning
**BOM Releases**: Coordinates and locks versions across all modules
**Tag Patterns**:
- Module tags: `proteus-core-v1.0.0`, `proteus-firebase-v1.0.0`, `proteus-ui-v1.0.0`
- BOM tags: `v1.0.0`

## Best Practices

1. **Use conventional commits**: Ensures proper categorization
2. **Test with dry run**: Use `release-all-dry-run.yml` for validation
3. **Stage carefully**: Use UPLOAD → PUBLISH for production releases
4. **Coordinate versions**: Use BOM releases for synchronized deployments
5. **Preview first**: Check changelog output before releasing