plugins {
    kotlin("plugin.spring")
    kotlin("plugin.serialization")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val slf4jVersion = libs.versions.slf4j.get()
val javaFakerVersion = libs.versions.javaFaker.get()

repositories {
    maven("https://packages.confluent.io/maven/")
}

dependencies {
    implementation(project(":libs:service"))
    implementation(project(":libs:kstream"))
    implementation(libs.bundles.spring.basic)
    developmentOnly(libs.spring.boot.devtools)

    // projectreactor
    implementation(libs.spring.boot.reactor.kotlin.extensions)
    testImplementation(libs.spring.boot.reactor.test)

    // java faker for data generation FIXME https://github.com/DiUS/java-faker/issues/327
    implementation("com.github.javafaker:javafaker:$javaFakerVersion") { exclude(module = "org.yaml") }
    implementation("org.yaml:snakeyaml:1.33")

    // spring-cloud bom
    implementation(enforcedPlatform(libs.spring.cloud.bom.get()))
    // we need add `kafka` binder for `Supplier` functions
    // We can only use `Consumer` and `Function` functions with KStream binder.
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")
    // FIXME: Kotlin Lambda support https://github.com/spring-cloud/spring-cloud-function/issues/780
    implementation("org.springframework.cloud:spring-cloud-function-kotlin")

    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.boot.mockk.test)
    testImplementation(libs.kotest.assertions.json.jvm)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation("org.springframework.kafka:spring-kafka-test")
}

affectedTestConfiguration { jvmTestTask = "check" }

configurations {
    all {
        exclude(group = "ch.qos.logback")
    }
}

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}

tasks {
    bootBuildImage {
        isVerboseLogging = true
    }

    bootRun {
        jvmArgs = listOf(
            // This will set logs level DEBUG only for local development.
            "-Dlogging.level.micro.apps=DEBUG",
        )
    }
}
