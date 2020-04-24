pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
//        maven { url = uri("https://nexus/") }
//        maven { url = uri("https://dl.bintray.com/gradle/gradle-plugins") }
    }
    plugins {
        id("io.quarkus") version "1.4.1.Final"
    }
}

rootProject.name = "micro-apps"
include(
    ":apps:account-grpc",
    ":apps:greeting-quarkus",
    ":apps:streaming-pipeline",
    ":apps:wordcount-pipeline",

    ":libs:core",
    ":libs:kbeam",
    ":libs:model",
    ":libs:proto"
)
