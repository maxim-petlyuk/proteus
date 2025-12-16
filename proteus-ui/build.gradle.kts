import org.jreleaser.model.Active
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    id("org.jreleaser")
    id("maven-publish")
    id("signing")
}

android {
    namespace = "io.proteus.ui"
    compileSdk = 36

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.kotlinx.serialization)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewModel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.constraintlayout.compose)

    implementation(platform(libs.proteus.bom))
    implementation(libs.proteus.core)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

val publishProperties = Properties().apply {
    load(FileInputStream(File(rootProject.rootDir, "publish.properties")))
}

version = publishProperties["proteus.ui.version"].toString()
description = publishProperties["proteus.ui.description"].toString()

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = publishProperties["proteus.group"].toString()
            artifactId = publishProperties["proteus.ui.artifact"].toString()

            pom {
                name.set(publishProperties["proteus.ui.version"].toString())
                description.set(publishProperties["proteus.ui.description"].toString())
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

                afterEvaluate {
                    from(components["release"])
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


val artifactName = publishProperties["proteus.ui.artifact"].toString()
val artifactVersion = publishProperties["proteus.ui.version"].toString()

jreleaser {
    project {
        name = artifactName
        version = artifactVersion
        inceptionYear = "2025"
        license.set("Apache-2.0")
        author("@maxim-petlyuk")
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
                external = File(rootProject.rootDir, "proteus-ui/CHANGELOG.md")
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