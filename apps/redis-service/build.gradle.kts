plugins {
    kotlin("plugin.noarg")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
    kotlin("kapt")
    kotlin("plugin.lombok")
    id("io.freefair.lombok")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val slf4jVersion = libs.versions.slf4j.get()

repositories {
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots") }
}

dependencies {
    implementation(project(":libs:core"))
    // TODO: enable when `entity-webapp` is ready
    // implementation(project(":apps:entity-webapp"))
    implementation(project(":libs:model"))
    implementation(project(":libs:service"))

    // implementation(platform("org.springframework.data:spring-data-bom:2021.1.0-M2"))

    // spring boot
    implementation(libs.bundles.spring.basic)
    api(libs.spring.boot.starter.validation)
    kapt(libs.spring.boot.configuration.processor) // For ConfigurationProperties
    compileOnly(libs.spring.boot.configuration.processor) // Workaround FIXME: https://youtrack.jetbrains.com/issue/KT-15040

    // Optional: for redis
//    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.apache.commons:commons-pool2")
    implementation("com.redis.om:redis-om-spring:0.3.1-SNAPSHOT")

    // projectreactor
    implementation(libs.spring.boot.reactor.kotlin.extensions)
    testImplementation(libs.spring.boot.reactor.test)

    // DevTools
    developmentOnly(libs.spring.boot.devtools)

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
        isVerboseLogging = true

        // buildpacks = listOf("gcr.io/paketo-buildpacks/adopt-openjdk")

        // add `ca-certificates` bindings, if you are running `gradle bootBuildImage` from behind corp proxy.
        bindings = listOf(
            // "${rootDir}/infra/bindings/ca-certificates:/platform/bindings/ca-certificates",
            // "$buildDir/agent:/workspace/agent:ro"
        )

        // builder = "paketobuildpacks/builder:tiny"
        // runImage = "paketobuildpacks/run:tiny:tiny-cnb"

        environment = mapOf(
            // "HTTPS_PROXY" to "https://proxy.example.com",
            // "HTTPS_PROXY" to "https://proxy.example.com"
            // "BP_DEBUG_ENABLED" to "true",
            "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
            "BPE_JAVA_TOOL_OPTIONS" to "-Dfile.encoding=UTF-8", // Optional, just for docs
            "BPE_APPEND_JAVA_TOOL_OPTIONS" to "-XX:+HeapDumpOnOutOfMemoryError",
            "BPE_BPL_SPRING_CLOUD_BINDINGS_ENABLED" to "false",
        )

        /* Image Publishing
        imageName = "docker.example.com/library/${project.name}"
        isPublish = true
        docker {
            publishRegistry {
                username = "user"
                password = "secret"
                url = "https://docker.example.com/v1/"
                email = "user@example.com"
            }
        }
        */
    }

    bootRun {
        // This will set logs level DEBUG only for local development.
        jvmArgs = listOf("-Dlogging.level.micro.apps=DEBUG")
    }
}

kapt {
    keepJavacAnnotationProcessors = true
}

noArg {
    invokeInitializers = true
    annotation("micro.apps.model.NoArg")
    annotation("com.redis.om.spring.annotations.Document")
    annotation("org.springframework.data.redis.core.RedisHash")
}
