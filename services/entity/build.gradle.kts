plugins {
    kotlin("plugin.noarg")
    kotlin("plugin.spring")
    kotlin("plugin.serialization")
    kotlin("kapt")

    // kotlin("plugin.lombok")
    // id("io.freefair.lombok")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val slf4jVersion = libs.versions.slf4j.get()

dependencies {
    implementation(projects.libs.core)
    // TODO: enable when `entity-webapp` is ready
    // implementation(projects.services.webapp))
    implementation(projects.libs.model)
    implementation(projects.libs.spring)

    // spring boot
    implementation(libs.bundles.spring.basic)
    api(libs.spring.boot.starter.validation)
    kapt(libs.spring.boot.configuration.processor) // For ConfigurationProperties
    compileOnly(libs.spring.boot.configuration.processor) // Workaround FIXME: https://youtrack.jetbrains.com/issue/KT-15040

    // Optional: for redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    // spring-boot 2.6.0 will now automatically enable redis pooling when commons-pool2 is on the classpath
    implementation("org.apache.commons:commons-pool2")
    implementation("com.redislabs:spring-redisearch:3.1.2")
    // implementation("com.redislabs:lettucemod:1.7.2") // https://github.com/redis-developer/lettucemod
    // implementation("com.redislabs:jredisgraph")
    // implementation("com.redis.om:redis-om-spring:0.5.2-SNAPSHOT")

    // projectreactor
    implementation(libs.spring.boot.reactor.kotlin.extensions)
    testImplementation(libs.spring.boot.reactor.test)

    // GCP logging and metrics
    implementation(enforcedPlatform(libs.spring.cloud.gcp.bom.get().toString()))
    runtimeOnly(libs.bundles.spring.cloud.gcp.basic)

    // DevTools
    developmentOnly(libs.spring.boot.devtools)

    testImplementation(testFixtures(projects.libs.test))
    testImplementation(testFixtures(projects.libs.model))
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

tasks.named("integrationTest") { dependsOn(rootProject.tasks.named("redisComposeUp")) }

tasks {
    bootBuildImage {
        verboseLogging.set(true)

        // buildpacks = listOf("gcr.io/paketo-buildpacks/adopt-openjdk")

        // add `ca-certificates` bindings, if you are running `gradle bootBuildImage` from behind corp proxy.
        bindings.set(listOf(
            // "${rootDir}/infra/bindings/ca-certificates:/platform/bindings/ca-certificates",
            // "$buildDir/agent:/workspace/agent:ro"
        ))

        // builder = "paketobuildpacks/builder:tiny"
        // runImage = "paketobuildpacks/run:tiny:tiny-cnb"

        environment.set(mapOf(
            // "HTTPS_PROXY" to "https://proxy.example.com",
            // "HTTPS_PROXY" to "https://proxy.example.com"
            // "BP_DEBUG_ENABLED" to "true",
            "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
            "BPE_JAVA_TOOL_OPTIONS" to "-Dfile.encoding=UTF-8", // Optional, just for docs
            "BPE_APPEND_JAVA_TOOL_OPTIONS" to "-XX:+HeapDumpOnOutOfMemoryError",
            "BPE_BPL_SPRING_CLOUD_BINDINGS_ENABLED" to "false",
        ))

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

noArg {
    invokeInitializers = true
    annotation("micro.apps.model.NoArg")
    annotation("com.redis.om.spring.annotations.Document")
    annotation("org.springframework.data.redis.core.RedisHash")
    annotation("org.springframework.data.relational.core.mapping.Table")
}
