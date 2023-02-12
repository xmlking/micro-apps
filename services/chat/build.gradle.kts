@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.spring)

    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.kotlin.serialization)

    alias(libs.plugins.gradle.native)
}

val slf4jVersion = libs.versions.slf4j.get()

dependencies {
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.spring.boot.starter.rsocket)

    implementation(libs.database.r2dbc.h2)
    // FIXME https://docs.uptrace.dev/guide/java.html#introduction
    implementation(libs.snakeyaml)

//    implementation(libs.jackson.module.kotlin)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.javafaker)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactive)
    implementation(libs.kotlinx.coroutines.reactor)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.turbine.test)

    runtimeOnly(libs.database.r2dbc.h2)

    implementation(libs.markdown)
}

affectedTestConfiguration { jvmTestTask = "check" }

// tasks.named("integrationTest") { dependsOn(rootProject.tasks.named("redisComposeUp")) }

tasks {
    bootBuildImage {
        // isVerboseLogging = true
        // add `bindings` if you are running `gradle bootBuildImage` from behind corp proxy.
        // bindings = listOf("${rootDir}/infra/bindings/ca-certificates:/platform/bindings/ca-certificates")

        builder.set("paketobuildpacks/builder:tiny")
        environment.set(
            mapOf(
                "BP_NATIVE_IMAGE" to "true"
            )
        )
    }

    bootRun {
        // This will set logs level DEBUG only for local development.
        jvmArgs = listOf("-Dlogging.level.micro.apps=DEBUG")
    }
}
