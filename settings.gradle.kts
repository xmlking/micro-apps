pluginManagement {
    repositories {
        // maven { url = uri("https://mycompany.nexus/") }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
    plugins {
        val quarkusPluginVersion: String by settings
        id("io.quarkus") version quarkusPluginVersion
    }
}

rootProject.name = "micro-apps"
include(
    ":apps:account-service",
    ":apps:greeting-service",
    ":apps:classifier-pipeline",
    ":apps:ingestion-pipeline",
    ":apps:wordcount-pipeline",

    ":libs:core",
    ":libs:kbeam",
    ":libs:model",
    ":libs:proto",
    ":libs:test",
    ":libs:μservice",
    ":libs:pipeline"
)
