import org.jreleaser.model.Active
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    id("org.jreleaser")
    id("maven-publish")
    id("signing")
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
        publishLibraryVariants("release")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ProteusCore"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.serialization)
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.kotlinx.coroutines.android)
            }
        }

        val androidUnitTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.robolectric)
                implementation(libs.androidx.test.core)
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.kotlinx.test)
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting

        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting

        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "io.proteus.core"
    compileSdk = 36

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

val publishProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "publish.properties")))
}

version = publishProperties["proteus.core.version"].toString()
description = publishProperties["proteus.core.description"].toString()

publishing {
    publications {
        withType<MavenPublication> {
            groupId = publishProperties["proteus.group"].toString()
            artifactId = when (name) {
                "kotlinMultiplatform" -> publishProperties["proteus.core.artifact"].toString() + "-kmp"
                else -> publishProperties["proteus.core.artifact"].toString() + "-$name"
            }

            pom {
                name.set(publishProperties["proteus.core.version"].toString())
                description.set(publishProperties["proteus.core.description"].toString())
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
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
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
            setUrl(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

val artifactName = publishProperties["proteus.core.artifact"].toString()
val artifactVersion = publishProperties["proteus.core.version"].toString()

jreleaser {
    project {
        name = artifactName
        version = artifactVersion
        author("@maxim-petlyuk")
        license.set("Apache-2.0")
        inceptionYear = "2025"
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
            tagName = "${artifactName}-v${artifactVersion}"
            releaseName = "${artifactName} v${artifactVersion}"
            overwrite.set(true)
            update {
                enabled.set(true)
            }
            changelog {
                enabled = true
                external = File(rootProject.rootDir, "proteus-core/CHANGELOG.md")
                append.enabled = false
            }
        }
    }

    deploy {
        maven {
            mavenCentral.create("sonatype") {
                active = Active.ALWAYS
                url = "https://central.sonatype.com/api/v1/publisher"
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