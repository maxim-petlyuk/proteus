import org.jreleaser.model.Active
import java.io.FileInputStream
import java.util.Properties
import kotlin.apply
import kotlin.toString

plugins {
    `java-platform`
    id("org.jreleaser")
    id("maven-publish")
    id("signing")
}

val publishProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "publish.properties")))
}

group = publishProperties["proteus.group"].toString()
version = publishProperties["proteus.bom.version"].toString()

val coreVersion = publishProperties["proteus.core.version"].toString()
val uiVersion = publishProperties["proteus.ui.version"].toString()
val firebaseVersion = publishProperties["proteus.firebase.version"].toString()

dependencies {
    constraints {
        api("io.github.maxim-petlyuk:proteus-core:${coreVersion}")
        api("io.github.maxim-petlyuk:proteus-firebase:${firebaseVersion}")
        api("io.github.maxim-petlyuk:proteus-ui:${uiVersion}")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["javaPlatform"])

            groupId = publishProperties["proteus.group"].toString()
            artifactId = publishProperties["proteus.bom"].toString()
            version = publishProperties["proteus.bom.version"].toString()

            pom {
                name.set("Proteus BOM")
                description.set(publishProperties["proteus.bom.description"].toString())
                url.set("https://github.com/maxim-petlyuk/proteus")

                issueManagement {
                    url.set("https://github.com/maxim-petlyuk/proteus/issues")
                }

                scm {
                    url.set("https://github.com/maxim-petlyuk/proteus")
                    connection.set("scm:git://github.com/maxim-petlyuk/proteus.git")
                    developerConnection.set("scm:git://github.com/maxim-petlyuk/proteus.git")
                }

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                developers {
                    developer {
                        id.set("maxim-petlyuk")
                        name.set("Maxim Petlyuk")
                        email.set("mpetlyuk@gmail.com")
                        url.set("https://www.linkedin.com/in/maxim-petlyuk-1464a6121/")
                    }
                }
            }
        }
    }

    repositories {
        maven {
            name = "staging"
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}


val artifactName = publishProperties["proteus.bom"].toString()
val artifactVersion = publishProperties["proteus.bom.version"].toString()

jreleaser {
    project {
        name = artifactName
        version = artifactVersion
        description = publishProperties["proteus.bom.description"].toString()
        longDescription = publishProperties["proteus.bom.description.long"].toString()
        author("@maxim-petlyuk")
        license.set("Apache-2.0")
        inceptionYear.set("2025")
    }

    gitRootSearch = true

    signing {
        active = Active.ALWAYS
        armored = true
        verify = true
    }

    release {
        github {
            enabled = true
            tagName = "v${artifactVersion}"
            releaseName = "v${artifactVersion}"
            overwrite.set(true)
            update {
                enabled.set(true)
            }
            changelog {
                enabled = true
                external = File(rootProject.rootDir, "CHANGELOG.md")
                append.enabled = false
            }
        }
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().toString())
                    setAuthorization("Basic")
                    applyMavenCentralRules = false // Wait for fix: https://github.com/kordamp/pomchecker/issues/21
                    sign = true
                    checksums = true
                    sourceJar = true
                    javadocJar = true
                    retryDelay = 60
                }
            }
        }
    }
}

// NEW: Make JReleaser tasks depend on publishing to staging
tasks.withType<org.jreleaser.gradle.plugin.tasks.JReleaserFullReleaseTask> {
    dependsOn("publishAllPublicationsToStagingRepository")
}

tasks.withType<org.jreleaser.gradle.plugin.tasks.JReleaserPublishTask> {
    dependsOn("publishAllPublicationsToStagingRepository")
}