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
    ":apps:wordcount",
    ":apps:greeting-quarkus",
    ":libs:core",
    ":libs:kbeam",
    ":libs:dlib"
)
