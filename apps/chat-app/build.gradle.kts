plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

val junitVersion = libs.versions.junit.get()
val slf4jVersion = libs.versions.slf4j.get()

dependencies {
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.thymeleaf)
    implementation(libs.spring.boot.starter.webflux)
    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.spring.boot.starter.rsocket)

    implementation(libs.r2dbc.h2)
    // FIXME https://docs.uptrace.dev/guide/java.html#introduction
    implementation(libs.snakeyaml)

    implementation(libs.jackson.module.kotlin)
    implementation(libs.javafaker)

    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactive)
    implementation(libs.kotlinx.coroutines.reactor)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.turbine.test)

    runtimeOnly(libs.h2)

    implementation(libs.markdown)
}

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}
