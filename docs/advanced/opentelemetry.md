# Open Telemetry

[OpenTelemetry](https://opentelemetry.io/) is a set of API, SDKs, libraries, and integrations aiming to standardize the generation, collection, and management of telemetry data(logs, metrics, and traces). OpenTelemetry is a Cloud Native Computing Foundation project created after the merger of OpenCensus(from Google) and OpenTracing(From Uber).
The data you collect with OpenTelemetry is vendor-agnostic and can be exported in many formats. Telemetry data has become critical to observe the state of distributed systems. With microservices and polyglot architectures, there was a need to have a global standard. OpenTelemetry aims to fill that space and is doing a great job at it thus far.

There are two important components in OpenTelemetry that comes in handy to collect telemetry data:

- **Client Libraries**
For Java applications, OpenTelemetry provides a JAR agent that can be attached to any Java 8+ application. It can detect a number of popular libraries and frameworks and instrument applications right out of the box for generating telemetry data.

- **OpenTelemetry Collector**
It is a stand-alone service provided by OpenTelemetry. It can be used as a telemetry-processing system with a lot of flexible configurations to collect and manage telemetry data.

Typically, here's how an application architecture instrumented with OpenTelemetry looks like.
![opentelemetry_architecture.webp](..%2Fimages%2Fopentelemetry_architecture.webp)
_Architecture - How OpenTelemetry fits in an application architecture. OTel collector refers to OpenTelemetry Collector_


OpenTelemetry provides client libraries and agents for most of the popular programming languages. There are two types of instrumentation:

- Auto-instrumentation
OpenTelmetry can collect data for many popular frameworks and libraries automatically. You don’t have to make any code changes.
- Manual instrumentation
If you want more application-specific data, OpenTelemetry SDK provides you with the capabilities to capture that data using OpenTelemetry APIs and SDKs.

For Spring Boot applications, we can use the OpenTelemetry Java Jar agent. We just need to download the latest version of the Java Jar agent and run the application with it.
![opentelemetry_java_instrument.webp](..%2Fimages%2Fopentelemetry_java_instrument.webp)
_OpenTelemetry helps generate and collect telemetry data from Spring Boot applications which can then be sent to SigNoz for storage, visualization, and analysis._

OpenTelemetry does not provide storage and visualization layer for the collected data. The advantage of using OpenTelemetry is that it can export the collected data in many different formats. So you're free to choose your telemetry backend. Natively, OpenTelemetry supports a wire protocol known as OTLP. This protocol sends the data to OpenTelemetry Collector as shown in the diagram above.


## Setup

Gradle setup

```gradle
    // openTelemetry bom
    implementation(enforcedPlatform(libs.opentelemetry.bom.get().toString()))
    implementation(enforcedPlatform(libs.opentelemetry.bomAlpha.get().toString()))

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
- [Monitor your Spring Boot application with OpenTelemetry and SigNoz](https://signoz.io/blog/opentelemetry-spring-boot/)
