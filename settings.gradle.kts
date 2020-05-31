pluginManagement {
    repositories {
        // maven { url = uri("https://mycompany.nexus/") }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "micro-apps"
include(
    ":apps:account-service",
    ":apps:greeting-service",
    ":apps:streaming-pipeline",
    ":apps:wordcount-pipeline",

    ":libs:core",
    ":libs:kbeam",
    ":libs:model",
    ":libs:proto",
    ":libs:test"
)
