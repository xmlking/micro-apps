plugins {
    // Support convention plugins written in Kotlin. Convention plugins are build scripts in 'src/main' that automatically become available as plugins in the main build.
    kotlin("jvm") version "1.9.20" // TODO: remove after stable
    `kotlin-dsl`
}

repositories {
    // Use the plugin portal to apply community plugins in convention plugins.
    gradlePluginPortal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    // add plugin artifacts, so we can reference them in plugins block in the precompiled script
    // in the future maybe we could update below to <plugin id>:<plugin id>.gradle.plugin:<plugin version> coordinates
//    implementation(libs.kotlin.gradle.plugin)
//    implementation(libs.kotlin.allopen.plugin)
//    implementation(libs.kotlin.benchmark.plugin)

    // this is a workaround to enable version catalog usage in the convention plugin
    // see https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
