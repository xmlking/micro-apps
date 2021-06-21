pluginManagement {
    repositories {
        // maven { url = uri("https://mycompany.nexus/") }
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
        val sonarPluginVersion: String by settings
        val spotlessPluginVersion: String by settings
        val spotlessClogPluginVersion: String by settings
        val dokkaPluginVersion: String by settings
        val checkLatestPluginVersion: String by settings
        val useLatestPluginVersion: String by settings
        val axionPluginVersion: String by settings
        val shadowPluginVersion: String by settings
        val jibPluginVersion: String by settings
        val loggerPluginVersion: String by settings
        val quarkusPluginVersion: String by settings
        val kotlinVersion: String by settings
        val protobufPluginVersion: String by settings
        val springVersion: String by settings
        val springDependencyManagementPluginVersion: String by settings
        val graalvmPluginVersion: String by settings

        id("org.sonarqube") version sonarPluginVersion
        id("com.diffplug.spotless") version spotlessPluginVersion
        kotlin("jvm") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false
        kotlin("plugin.allopen") version kotlinVersion apply false
        kotlin("kapt") version kotlinVersion apply false
        id("org.jetbrains.dokka") version dokkaPluginVersion
        id("com.github.ben-manes.versions") version checkLatestPluginVersion
        id("com.diffplug.spotless-changelog") version spotlessClogPluginVersion
        id("se.patrikerdes.use-latest-versions") version useLatestPluginVersion
        id("pl.allegro.tech.build.axion-release") version axionPluginVersion
        id("com.github.johnrengelman.shadow") version shadowPluginVersion
        id("com.google.cloud.tools.jib") version jibPluginVersion
        id("dev.jacomet.logging-capabilities") version loggerPluginVersion
        id("com.google.protobuf") version protobufPluginVersion apply false
        id("io.quarkus") version quarkusPluginVersion apply false
        id("org.springframework.boot") version springVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementPluginVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false
        id("org.graalvm.buildtools.native") version graalvmPluginVersion apply false
    }
}

rootProject.name = "micro-apps"
include(

    ":apps:account-service",
    ":apps:chat-app",
    ":apps:greeting-service",
    ":apps:person-graphql",
    ":apps:keying-service",
    ":apps:linking-service",
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
