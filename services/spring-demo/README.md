# Spring Example Service

Sample service that uses [Spring Native](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
with [Kotlin Coroutines](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
and [Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin). <br/>
Generate a native image to improve the start-up time.

Find more *Spring Native* [samples](https://github.com/spring-projects-experimental/spring-native/tree/main/samples)
here

This µService also showcase running both **REST** and **gRPC** services on single server.

This project demonstrates Observability using:
* [Prometheus](https://prometheus.io/) for monitoring and alerting
* [Loki](https://grafana.com/oss/loki/) for Distributed Logging
* [Tempo](https://grafana.com/oss/tempo/) for Distributed Tracing
* [Grafan](https://grafana.com/) for visualization
* 
## Run

```bash
gradle :services:spring-demo:bootRun
# log at debug level
gradle :services:spring-demo:bootRun --debug
```

#### Options
``` 
-Dspring.devtools.restart.enabled=true
```

```bash
open http://localhost:8080/intro
```

## Test

```
gradle :services:spring-demo:test
gradle :services:spring-demo:integrationTest
```

### Test REST services

We are using [httpie](https://httpie.io/) CLI for REST testing

```bash
# list
http :8080/intro
http :8080/entity/abcd
# health
http :8080/actuator
http :8080/actuator/health
http :8080/actuator/info
http :8080/actuator/metrics
http :8080/actuator/metrics/http.server.requests
http :8080/actuator/prometheus
````

### Test gRPC services 

```bash
grpcurl -plaintext \
-protoset <(buf build -o -) \
-d '{ "message":  "sumo" }' 0.0.0.0:6565 micro.apps.proto.echo.v1.EchoService/Echo      

grpcurl -plaintext \
-protoset <(buf build -o -) \
-d '{ "message":  "sumo" }' 0.0.0.0:6565 micro.apps.proto.echo.v1.EchoService/EchoStream
```

## Build

### Build fatJar

```bash
gradle :services:spring-demo:build
```

### Build native image

```bash
gradle :services:spring-demo:bootBuildImage -x test
```

Then, you can run the app like any other container:

```bash
docker run -i --rm -p 8080:8080 -p 6565:6565 spring-service:1.6.5-SNAPSHOT
```

To debug docker image layers:

```bash
dive spring-service:1.6.5-SNAPSHOT
```

## Reference

- [Spring Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin)
- [Write Less Code with Kotlin and Spring Boot](https://www.infoq.com/presentations/kotlin-spring-boot/)
- [The State of Kotlin Support in Spring](https://resources.jetbrains.com/storage/products/kotlin/events/kotlin14/Slides/spring.pdf)
- [Structured Concurrency](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
- [KoFu](https://github.com/spring-projects-experimental/spring-fu/tree/main/kofu)
- [KoFu Samples](https://github.com/spring-projects-experimental/spring-fu/tree/main/samples)
- [Functional Bean Registration Example](https://github.com/pwestlin/webshopkotlin/blob/master/core-service/src/main/kotlin/nu/westlin/webshop/core/CoreApplication.kt)
- [Spring WebClient and WebTestClient Tutorial with Examples](https://www.callicoder.com/spring-5-reactive-webclient-webtestclient-examples/)
- [mockk verify](https://notwoods.github.io/mockk-guidebook/docs/mocking/verify/)
- [Tracing in Spring Boot with OpenTracing/OpenTelemetry](https://medium.com/swlh/tracing-in-spring-boot-with-opentracing-opentelemetry-dd724134ca93), and Sample [Code](https://github.com/fpaparoni/tracing)
- OpenTelemetry Java SDK
  - [Getting Started with the Java SDK on Traces and Metrics Instrumentation](https://aws-otel.github.io/docs/getting-started/java-sdk)
  - [Manual Instrumentation](https://opentelemetry.io/docs/java/manual_instrumentation/)
  - [Instrumentation Examples](https://github.com/mnadeem/boot-opentelemetry-tempo)
- Health
  - Liveness, Readiness [example](https://github.com/hellosatish/springboot2.3-features-demo/blob/master/probes-demo/src/main/java/org/sk/ms/probes/ExampleController.java)
  - 
