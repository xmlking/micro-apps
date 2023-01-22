pluginManagement {
    repositories {
        // maven { url = uri("https://mycompany.nexus/") }
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
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
            classpath("com.dropbox.affectedmoduledetector:affectedmoduledetector:0.1.6")
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
        val kotlinVersion = "1.7.10"

        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false
        kotlin("plugin.allopen") version kotlinVersion apply false
        kotlin("kapt") version kotlinVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        kotlin("plugin.noarg") version kotlinVersion apply false
        kotlin("plugin.lombok") version kotlinVersion apply false
        id("org.jetbrains.kotlinx.kover") version "0.6.0"
        id("io.freefair.lombok") version "6.5.1" apply false
        id("org.sonarqube") version "3.4.0.2513"
        id("com.diffplug.spotless") version "6.10.0"
        id("org.jetbrains.dokka") version "1.7.10"
        id("com.github.ben-manes.versions") version "0.42.0"
        id("com.diffplug.spotless-changelog") version "2.4.0"
        id("se.patrikerdes.use-latest-versions") version "0.2.18"
        id("pl.allegro.tech.build.axion-release") version "1.14.0"
        id("com.github.johnrengelman.shadow") version "7.1.2"
        id("com.google.cloud.tools.jib") version "3.3.0"
        id("dev.jacomet.logging-capabilities") version "0.10.0"
        id("com.google.protobuf") version "0.8.19" apply false
        id("io.quarkus") version "2.16.0.Final" apply false
        // id("org.springframework.boot") version "2.5.4" apply false
        id("org.springframework.boot") version "2.7.3" apply false
        id("org.springframework.experimental.aot") version "0.12.1" apply false
        id("io.spring.dependency-management") version "1.0.13.RELEASE" apply false
        id("org.graalvm.buildtools.native") version "0.9.13" apply false
        id("com.dropbox.affectedmoduledetector") version "0.1.2" apply false
        id("com.avast.gradle.docker-compose") version "0.16.9" apply false
        id("com.github.node-gradle.node") version "3.4.0" apply false
        id("org.owasp.dependencycheck") version "7.1.2"
    }
}

rootProject.name = "micro-apps"
include(
    ":apps:account-service",
    ":apps:keying-service",
    ":apps:linking-service",
    ":apps:greeting-service",
    ":apps:person-service",
    ":apps:spring-service",
    ":apps:chat-service",
    ":apps:entity-service",
    ":apps:redis-service",
    ":apps:entity-webapp",
    ":apps:streams-service",
    ":apps:wordcount-service",
    ":apps:classifier-pipeline",
    ":apps:ingestion-pipeline",
    ":apps:wordcount-pipeline",

    ":libs:core",
    ":libs:kbeam",
    ":libs:kstream",
    ":libs:model",
    ":libs:proto",
    ":libs:test",
    ":libs:grpc",
    ":libs:service",
    ":libs:pipeline",
    ":libs:crypto",
    ":libs:avro"
)
