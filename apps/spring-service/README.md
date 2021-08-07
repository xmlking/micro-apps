# Spring Example Service

Sample service that uses [Spring Native](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
with [Kotlin Coroutines](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
and [Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin). <br/>
Generate a native image to improve the start-up time.

Find more *Spring Native* [samples](https://github.com/spring-projects-experimental/spring-native/tree/main/samples)
here

This µService also showcase running both **REST** and **gRPC** services on single server.

## Run

```bash
gradle :apps:spring-service:bootRun
# log at debug level
gradle :apps:spring-service:bootRun --debug
```

```bash
open http://localhost:8080/intro
```

## Test

```
gradle :apps:spring-service:test
gradle :apps:spring-service:integrationTest
```

### Test REST services

We are using [httpie](https://httpie.io/) CLI for REST testing

```bash
# list
http :8080/intro
# health
http :8080/actuator
http :8080/actuator/health
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
gradle :apps:spring-service:build
```

### Build native image

```bash
gradle :apps:spring-service:bootBuildImage -x test
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

