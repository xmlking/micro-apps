plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
}

val slf4jVersion: String by project
val turbineVersion: String by project
val markdownVersion: String by project
val javafakerVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("io.r2dbc:r2dbc-h2")
    // FIXME https://docs.uptrace.dev/guide/java.html#introduction
    implementation("org.yaml:snakeyaml:1.28")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.github.javafaker:javafaker:$javafakerVersion")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("app.cash.turbine:turbine:$turbineVersion")

    runtimeOnly("com.h2database:h2")

    implementation("org.jetbrains:markdown:$markdownVersion")
}

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}
