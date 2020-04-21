pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
//        maven { url = uri("https://nexus/") }
//        maven { url = uri("https://dl.bintray.com/gradle/gradle-plugins") }
    }
//    plugins {
//        id ("io.quarkus:$quarkusPluginVersion")
//    }
}

rootProject.name = "micro-apps"
include(
    ":apps:demo",
    ":apps:wordcount-pipeline",
    ":apps:streaming-pipeline",
    ":apps:greeting-quarkus",
    ":apps:account-grpc",
    ":libs:core",
    ":libs:kbeam",
    ":libs:dlib",
    ":libs:proto"
)
