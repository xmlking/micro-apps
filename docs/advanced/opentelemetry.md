# Open Telemetry

## Setup

Gradle setup

```gradle
    // openTelemetry bom
    implementation(enforcedPlatform(libs.opentelemetry.bom.get()))
    implementation(enforcedPlatform(libs.opentelemetry.bomAlpha.get()))

    // openTelemetry agent
    openTelemetry(variantOf(libs.opentelemetry.javaagent) { classifier("all") })

    // openTelemetry essential
    implementation(libs.bundles.opentelemetry.api)
    implementation(libs.bundles.opentelemetry.sdk)

    // openTelemetry exporters
    implementation(libs.opentelemetry.exporter.prometheus)
    // implementation(libs.opentelemetry.exporter.logging)
    // implementation(libs.opentelemetry.exporter.otlp)
    // implementation(libs.opentelemetry.exporter.jaeger)

    // micrometer for openTelemetry
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    // extensions for openTelemetry
    implementation(libs.opentelemetry.extension.annotations) // to use  @WithSpan etc
    implementation(libs.opentelemetry.extension.kotlin)

    testImplementation(libs.bundles.opentelemetry.test)
    
    tasks {
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
                "-Dotel.propagators=tracecontext,baggage", // no b3 for logging exporter
                "-Dotel.service.name=${project.name}",
                "-Dotel.resource.attributes=service.name=${project.name}",
            )
        }
    }

    /*** copy oTel agent ***/
    val copyOpenTelemetryAgent = tasks.register<Sync>("copyOpenTelemetryAgent") {
        from(openTelemetry.asPath)
        into("$buildDir/agent")
        rename("opentelemetry-javaagent-(.+?)-all.jar", "opentelemetry-javaagent-all.jar")
    }
    tasks.named("processResources") {
        dependsOn(copyOpenTelemetryAgent)
    }
```

### instrumentation

Creating spans around methods with **@WithSpan**

    https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/docs/manual-instrumentation.md

Auto Configuration

    To configure the OpenTelemetry SDK based on the standard set of environment variables and system properties, you can use the `opentelemetry-sdk-extension-autoconfigure` module.

## Disabling

### Disabling the agent entirely

    You can disable the agent entirely using `-Dotel.javaagent.enabled=false` (or using the equivalent environment variable `OTEL_JAVAAGENT_ENABLED=false`).


###  Suppressing specific agent instrumentation

You can suppress agent instrumentation of specific libraries by using `-Dotel.instrumentation.[name].enabled=false` where `name` is the corresponding instrumentation [name](https://github.com/open-telemetry/opentelemetry-java-instrumentation/blob/main/docs/suppressing-instrumentation.md)

Example: 

```
-Dotel.instrumentation.[spring-webflux].enabled=false
-Dotel.instrumentation.lettuce.enabled=false
```

## Testing

https://github.com/craigatk/projektor/blob/master/server/server-app/src/test/kotlin/projektor/ApplicationTestCase.kt
https://github.com/craigatk/projektor/blob/master/server/server-app/src/main/kotlin/projektor/testrun/TestRunDatabaseRepository.kt
```
testImplementation("io.opentelemetry:opentelemetry-sdk")
testImplementation("io.opentelemetry:opentelemetry-sdk-testing")
```

## Code Samples

- RPC Sample
  - [Setup](https://github.com/helloworlde/netty-rpc/blob/master/opentelemetry/src/main/java/io/github/helloworlde/netty/rpc/opentelemetry/metrics/MetricsConfiguration.java)
  - [Instrumentation](https://github.com/helloworlde/netty-rpc/blob/master/opentelemetry/src/main/java/io/github/helloworlde/netty/rpc/opentelemetry/metrics/ClientMetricsInterceptor.java)


## Reference

- [boot-opentelemetry-tempo](https://github.com/mnadeem/boot-opentelemetry-tempo/tree/0.17.0_complex)
