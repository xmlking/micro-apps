plugins {
    kotlin("plugin.noarg")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
    kotlin("kapt")

    // kotlin("plugin.lombok")
    // id("io.freefair.lombok")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.springframework.experimental.aot")
    id("org.graalvm.buildtools.native")
}

val slf4jVersion = libs.versions.slf4j.get()

dependencies {
    implementation(project(":libs:core"))
    // TODO: enable when `entity-webapp` is ready
    // implementation(project(":apps:entity-webapp"))
    implementation(project(":libs:model"))
    implementation(project(":libs:service"))

    // spring boot
    implementation(libs.bundles.spring.basic)
    api(libs.spring.boot.starter.validation)
    kapt(libs.spring.boot.configuration.processor) // For ConfigurationProperties
    compileOnly(libs.spring.boot.configuration.processor) // FIXME: https://youtrack.jetbrains.com/issue/KT-15040

    // Optional: if you also want rsocket
    // implementation(libs.spring.boot.starter.rsocket)

    // Optional: for redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    // spring-boot 2.6.0 will now automatically enable redis pooling when commons-pool2 is on the classpath
    implementation("org.apache.commons:commons-pool2")
    implementation("com.redislabs:spring-redisearch:3.1.2")
    // implementation("com.redislabs:lettucemod:1.4.0")
    // implementation("com.redislabs:jredisgraph")

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

configurations {
    all {
        exclude(group = "ch.qos.logback")
    }
}

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}

tasks.named("integrationTest") { dependsOn(rootProject.tasks.named("redisComposeUp")) }

tasks {
    bootBuildImage {
        // isVerboseLogging = true
        // add `bindings` if you are running `gradle bootBuildImage` from behind corp proxy.
        // bindings = listOf("${rootDir}/infra/bindings/ca-certificates:/platform/bindings/ca-certificates")

        builder = "paketobuildpacks/builder:tiny"
        environment = mapOf(
            "BP_NATIVE_IMAGE" to "true"
        )
    }

    bootRun {
        // This will set logs level DEBUG only for local development.
        jvmArgs = listOf("-Dlogging.level.micro.apps=DEBUG")
    }
}

springAot {
    verify.set(false)
    failOnMissingSelectorHint.set(false)
//    removeSpelSupport.set(true)
//    removeYamlSupport.set(true)
}

noArg {
    annotation("org.springframework.data.redis.core.RedisHash")
}
