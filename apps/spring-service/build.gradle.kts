plugins {
    kotlin("plugin.spring")
    kotlin("plugin.serialization")

    // kotlin("plugin.lombok")
    // id("io.freefair.lombok")

    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

val slf4jVersion = libs.versions.slf4j.get()

val openTelemetry: Configuration by configurations.creating

dependencies {
    implementation(project(":libs:core"))
    // TODO: enable when `entity-webapp` is ready
    // implementation(project(":apps:entity-webapp"))
    implementation(project(":libs:model"))
    implementation(project(":libs:service"))

    implementation(libs.bundles.spring.basic)
    api(libs.spring.boot.starter.validation)
    developmentOnly(libs.spring.boot.devtools)

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

    /**
     * OpenTelemetry Note:
     *  **Libraries** that want to export telemetry data using OpenTelemetry MUST only depend on the `opentelemetry-api`
     *  **Applications** should also depend on the `opentelemetry-sdk`
     *  This way, libraries will obtain a real implementation only if the user application is configured for it
     */

    // openTelemetry bom
    implementation(enforcedPlatform(libs.opentelemetry.bom.get()))
    implementation(enforcedPlatform(libs.opentelemetry.bomAlpha.get()))

    // openTelemetry agent
    // openTelemetry(variantOf(libs.opentelemetry.javaagent) { classifier("all") })
    openTelemetry(variantOf(libs.opentelemetry.javaagent) { })

    // openTelemetry essential
    implementation(libs.bundles.opentelemetry.api)
    implementation(libs.bundles.opentelemetry.sdk) // Optional

    // openTelemetry exporters
    implementation(libs.opentelemetry.exporter.prometheus) // Optional
    // implementation(libs.opentelemetry.exporter.logging)
    // implementation(libs.opentelemetry.exporter.otlp)
    // implementation(libs.opentelemetry.exporter.jaeger)

    // micrometer for openTelemetry
    runtimeOnly(libs.micrometer.registry.prometheus)

    // extensions for openTelemetry
    implementation(libs.opentelemetry.extension.annotations) // to use  @WithSpan etc
    implementation(libs.opentelemetry.extension.kotlin)

    testImplementation(libs.bundles.opentelemetry.test)

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
            "$buildDir/agent:/workspace/agent:ro"
        )

        // builder = "paketobuildpacks/builder:tiny"
        // runImage = "paketobuildpacks/run:tiny:tiny-cnb"

        environment = mapOf(
            // "HTTPS_PROXY" to "https://proxy.example.com",
            // "HTTPS_PROXY" to "https://proxy.example.com"
            // "BP_DEBUG_ENABLED" to "true",
            "BPE_DELIM_JAVA_TOOL_OPTIONS" to " ",
            "BPE_JAVA_TOOL_OPTIONS" to "-Dfile.encoding=UTF-8", // Optional, just for docs
            "BPE_PREPEND_JAVA_TOOL_OPTIONS" to "-javaagent:/workspace/agent/opentelemetry-javaagent-all.jar",
            "BPE_APPEND_JAVA_TOOL_OPTIONS" to
                // "-Dotel.javaagent.debug=true " +
                // "-Dotel.traces.exporter=jaeger " +
                "-Dotel.traces.exporter=logging " +
                "-Dotel.metrics.exporter=prometheus " +
                "-Dotel.propagators=b3,tracecontext,baggage " +
                "-Dotel.service.name=${project.name} " +
                "-Dotel.resource.attributes=service.name=${project.name}",

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
        jvmArgs = listOf(
            // This will set logs level DEBUG only for local development.
            "-Dlogging.level.micro.apps=DEBUG",
            "-javaagent:$buildDir/agent/opentelemetry-javaagent-all.jar",
            // "-Dotel.javaagent.debug=true",
            "-Dotel.traces.exporter=logging",
            // "-Dotel.traces.exporter=jaeger",
            // "-Dotel.metrics.exporter=logging",
            "-Dotel.metrics.exporter=prometheus",
            "-Dotel.propagators=tracecontext,baggage", // no b3 for logging
            "-Dotel.service.name=${project.name}",
            "-Dotel.resource.attributes=service.name=${project.name}",
        )
    }
}

/*** copy oTel agent ***/
val copyOpenTelemetryAgent = tasks.register<Sync>("copyOpenTelemetryAgent") {
    println(openTelemetry.asPath)
    from(openTelemetry.asPath)
    into("$buildDir/agent")
    rename("opentelemetry-javaagent-(.+?).jar", "opentelemetry-javaagent.jar")
    // rename("opentelemetry-javaagent-(.+?)-all.jar", "opentelemetry-javaagent-all.jar")
}
tasks.named("processResources") {
    dependsOn(copyOpenTelemetryAgent)
}
