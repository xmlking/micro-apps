// pluginManagement {
//    repositories {
//        maven { url = uri("https://nexus/") }
//        maven { url = uri("https://dl.bintray.com/gradle/gradle-plugins") }
//        gradlePluginPortal()
//    }
// }

rootProject.name = "micro-apps"
include(
    ":apps:demo",
    ":apps:wordcount",
    ":libs:core",
    ":libs:kbeam",
    ":libs:dlib"
)
