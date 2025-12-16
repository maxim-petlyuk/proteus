// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    alias(libs.plugins.jreleaser) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

// =============================================================================
// Changelog Generation Tasks
// Requires git-cliff to be installed: brew install git-cliff
// =============================================================================

// Map of module names to their directory paths
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

// Task to generate changelog for a specific module
// Usage: ./gradlew generateChangelog -Pmodule=core
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

// Task to generate changelogs for all modules at once
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