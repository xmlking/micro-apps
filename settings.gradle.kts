pluginManagement {
    repositories {
        // maven { url = uri("https://mycompany.nexus/") }
        maven { url = uri("https://repo.spring.io/release") }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    // FIXME: remove after: https://github.com/diffplug/spotless/issues/587
    buildscript {
        repositories {
            mavenLocal()
            mavenCentral()
        }
        dependencies {
            classpath("org.eclipse.jgit:org.eclipse.jgit:5.10.0.202012080955-r")
            classpath("com.dropbox.affectedmoduledetector:affectedmoduledetector:0.1.2")
        }
        configurations.classpath {
            resolutionStrategy {
                force("org.eclipse.jgit:org.eclipse.jgit:5.10.0.202012080955-r")
            }
        }
    }

    enableFeaturePreview("VERSION_CATALOGS")
    // FIXME: https://github.com/gradle/gradle/issues/16815
    enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    // enableFeaturePreview("GRADLE_METADATA")

    plugins {
        val kotlinVersion = "1.5.21"

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false
        kotlin("plugin.allopen") version kotlinVersion apply false
        kotlin("kapt") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.noarg") version kotlinVersion apply false
        kotlin("plugin.lombok") version kotlinVersion apply false
        id("io.freefair.lombok") version "6.0.0-m2" apply false
        id("org.sonarqube") version "3.3"
        id("com.diffplug.spotless") version "5.14.1"
        id("org.jetbrains.dokka") version "1.5.0"
        id("com.github.ben-manes.versions") version "0.39.0"
        id("com.diffplug.spotless-changelog") version "2.2.0"
        id("se.patrikerdes.use-latest-versions") version "0.2.17"
        id("pl.allegro.tech.build.axion-release") version "1.13.3"
        id("com.github.johnrengelman.shadow") version "7.0.0"
        id("com.google.cloud.tools.jib") version "3.1.2"
        id("dev.jacomet.logging-capabilities") version "0.9.0"
        id("com.google.protobuf") version "0.8.17" apply false
        id("io.quarkus") version "2.1.0.CR1" apply false
        id("org.springframework.boot") version "2.5.2" apply false
        id("org.springframework.experimental.aot") version "0.10.1" apply false
        id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
        id("org.graalvm.buildtools.native") version "0.9.2" apply false
        id("com.dropbox.affectedmoduledetector") version "0.1.2" apply false
        id("com.avast.gradle.docker-compose") version "0.14.4" apply false
        id("com.github.node-gradle.node") version "3.1.0" apply false
    }
}

rootProject.name = "micro-apps"
include(
    ":apps:account-service",
    ":apps:keying-service",
    ":apps:linking-service",
    ":apps:greeting-service",
    ":apps:person-service",
    ":apps:chat-service",
    ":apps:entity-service",
    ":apps:entity-webapp",
    ":apps:classifier-pipeline",
    ":apps:ingestion-pipeline",
    ":apps:wordcount-pipeline",

    ":libs:core",
    ":libs:kbeam",
    ":libs:model",
    ":libs:proto",
    ":libs:test",
    ":libs:service",
    ":libs:pipeline"
)
