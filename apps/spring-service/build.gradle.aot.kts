plugins {
    kotlin("plugin.spring")
    kotlin("plugin.serialization")

    // kotlin("plugin.lombok")
    // id("io.freefair.lombok")

    id("org.springframework.boot")
    id("io.spring.dependency-management")

    id("org.springframework.experimental.aot")
    id("org.graalvm.buildtools.native")
}

val slf4jVersion = libs.versions.slf4j.get()
val otelVersion = libs.versions.otel.get()

val openTelemetry: Configuration by configurations.creating

dependencies {
    implementation(project(":libs:core"))
    // TODO: enable when `entity-webapp` is ready
    // implementation(project(":apps:entity-webapp"))
    implementation(project(":libs:model"))
    implementation(project(":libs:service"))

    // implementation("org.springframework.fu:spring-fu-kofu:0.4.5-SNAPSHOT")
    implementation(libs.bundles.spring.basic)
    api(libs.spring.boot.starter.validation)

    // Optional: if you also want rsocket
    // implementation(libs.spring.boot.starter.rsocket)

    // Optional: JPA
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-h2")

    // Optional: if you also want to add some gRPC services
    // TODO: gRPC not working with GraalVM
    implementation(project(":libs:proto"))
    implementation(libs.bundles.spring.grpc)

    // projectreactor
    implementation(libs.spring.boot.reactor.kotlin.extensions)
    testImplementation(libs.spring.boot.reactor.test)

    // openTelemetry minimal
    implementation(enforcedPlatform("io.opentelemetry:opentelemetry-bom:${otelVersion}"))
    implementation("io.opentelemetry:opentelemetry-api")
    openTelemetry("io.opentelemetry.javaagent:opentelemetry-javaagent:${otelVersion}:all")

    implementation("io.opentelemetry:opentelemetry-sdk")

//    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
//    implementation("io.opentelemetry:opentelemetry-exporter-otlp-metrics")
//    implementation("io.opentelemetry.instrumentation:opentelemetry-otlp-exporter-starter")

    // micrometer for otel
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // extension for otel
    implementation("io.opentelemetry:opentelemetry-extension-annotations") // to use  @WithSpan etc
    implementation("io.opentelemetry:opentelemetry-extension-kotlin")

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

tasks {
    bootBuildImage {
        isVerboseLogging = true

        // buildpacks = listOf("gcr.io/paketo-buildpacks/adopt-openjdk")

        // add `ca-certificates` bindings, if you are running `gradle bootBuildImage` from behind corp proxy.
        bindings = listOf(
            // "${rootDir}/infra/bindings/ca-certificates:/platform/bindings/ca-certificates",
            "${buildDir}/agent:/workspace/agent:ro"
        )

        // builder = "paketobuildpacks/builder:tiny"
        // runImage = "paketobuildpacks/run:tiny:tiny-cnb"

        environment = mapOf(
            // "HTTPS_PROXY" to "https://proxy.example.com",
            // "HTTPS_PROXY" to "https://proxy.example.com"
            "BP_NATIVE_IMAGE" to "true",
            // "BP_DEBUG_ENABLED" to "true",
            "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
            "BPE_JAVA_TOOL_OPTIONS" to "-Dfile.encoding=UTF-8", // Optional, just for docs
            "BPE_PREPEND_JAVA_TOOL_OPTIONS" to "-javaagent:/workspace/agent/opentelemetry-javaagent-all.jar",
            "BPE_APPEND_JAVA_TOOL_OPTIONS" to
                // "-Dotel.traces.exporter=otlp " +
                "-Dotel.traces.exporter=logging " +
                "-Dotel.propagators=b3,tracecontext,baggage " +
                "-Dotel.service.name=${project.name} " +
                "-Dotel.resource.attributes=service.name=${project.name} " +
                "-Dotel.metrics.exporter=prometheus",
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
        println("-javaagent:/${openTelemetry.singleFile.name}")
        jvmArgs = listOf(
            // This will set logs level DEBUG only for local development.
            "-Dlogging.level.micro.apps=DEBUG",
            "-javaagent:$buildDir/agent/opentelemetry-javaagent-all.jar",
//            "-Dotel.traces.exporter=otlp",
            "-Dotel.traces.exporter=logging",
            "-Dotel.propagators=b3,tracecontext,baggage",
            "-Dotel.service.name=${project.name}",
            "-Dotel.resource.attributes=service.name=${project.name}",
            "-Dotel.metrics.exporter=prometheus"
        )
    }
}

springAot {
    verify.set(false)
    failOnMissingSelectorHint.set(false)
//    removeSpelSupport.set(true)
//    removeYamlSupport.set(true)
}

/*** copy oTel agent ***/
val copyOpenTelemetryAgent = tasks.register<Sync>("copyOpenTelemetryAgent") {
    from(openTelemetry.asPath)
    into("$buildDir/agent")
    rename("opentelemetry-javaagent-${otelVersion}-all.jar", "opentelemetry-javaagent-all.jar")
}
tasks.named("processAotResources") {
    dependsOn(copyOpenTelemetryAgent)
}