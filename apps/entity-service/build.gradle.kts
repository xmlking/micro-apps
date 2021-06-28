import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("plugin.spring")
    id("org.springframework.experimental.aot")
    //id("org.graalvm.buildtools.native")

    // kotlin("plugin.serialization")
}

val slf4jVersion = libs.versions.slf4j.get()

dependencies {
    implementation(project(":libs:model"))
    // implementation("org.springframework.fu:spring-fu-kofu:0.4.5-SNAPSHOT")
    implementation(libs.bundles.spring.basic)
    implementation(libs.spring.boot.starter.rsocket)

    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // projectreactor
    implementation(libs.spring.boot.reactor.kotlin.extensions)
    testImplementation(libs.spring.boot.reactor.test)

    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(testFixtures(project(":libs:model")))
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.boot.mockk.test)
    testImplementation(libs.kotest.assertions.json.jvm)
    testImplementation(libs.kotest.extensions.spring)
}

affectedTestConfiguration { jvmTestTask = "check" }

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}

tasks.named("integrationTest") { dependsOn(rootProject.tasks.named("redisComposeUp")) }

tasks.withType<BootBuildImage> {
    builder = "paketobuildpacks/builder:tiny"
    environment = mapOf(
        "BP_NATIVE_IMAGE" to "true",
        "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "--enable-https " +
            "-H:+ReportExceptionStackTraces -H:+ReportUnsupportedElementsAtRuntime " +
            "--initialize-at-build-time=org.slf4j.jul.JDK14LoggerAdapter,org.slf4j.simple.SimpleLogger,org.slf4j.LoggerFactory",
    )
}

// springAot {
//    removeSpelSupport.set(true)
//    removeYamlSupport.set(true)
// }
