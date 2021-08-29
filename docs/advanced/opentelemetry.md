# Open Telemetry

## Setup

```gradle
    implementation platform("io.opentelemetry:opentelemetry-bom:$opentelemetry_version")
    implementation 'io.opentelemetry:opentelemetry-api'
    implementation 'io.opentelemetry:opentelemetry-extension-kotlin'
    testImplementation("io.opentelemetry:opentelemetry-sdk")
    testImplementation("io.opentelemetry:opentelemetry-sdk-testing")
```

### instrumentation

Creating spans around methods with @WithSpan

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

testImplementation("io.opentelemetry:opentelemetry-sdk")
testImplementation("io.opentelemetry:opentelemetry-sdk-testing")

## Reference
- [boot-opentelemetry-tempo](https://github.com/mnadeem/boot-opentelemetry-tempo/tree/0.17.0_complex)
