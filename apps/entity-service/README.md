# Entity Service

Sample service that uses [Spring Native](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
with [Kotlin Coroutines](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
and [Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin). <br/>
Generate a native image to improve the start-up time.

Find more *Spring Native* [samples](https://github.com/spring-projects-experimental/spring-native/tree/main/samples)
here

This µService also showcase running both **REST** and **gRPC** services on single server.

## Run

### Redis

```bash
# start local redis
gradle redisComposeUp
# stop local redis before restart again
gradle redisComposeDown
# copy container logs to `build/containers-logs`
gradle redisComposeLogs
```

**Redis Insight:** Redis db visualization dashboard

> when prompted, use host:redisearch, port:6379 and name:any_name

```bash
open http://localhost:8001/
```

```bash
gradle :apps:entity-service:bootRun
# log at debug level
gradle :apps:entity-service:bootRun --debug
```

```bash
open http://localhost:8080/
```

## Test

```
gradle :apps:entity-service:test
gradle :apps:entity-service:integrationTest
```

Test gRPC services 

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

```
gradle :apps:entity-service:build
```

### Build native image

```
gradle :apps:entity-service:bootBuildImage 
```

## Reference

- [Spring Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin)
- [Structured Concurrency](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
- [KoFu](https://github.com/spring-projects-experimental/spring-fu/tree/main/kofu)
- [KoFu Samples](https://github.com/spring-projects-experimental/spring-fu/tree/main/samples)
- [Functional Bean Registration Example](https://github.com/pwestlin/webshopkotlin/blob/master/core-service/src/main/kotlin/nu/westlin/webshop/core/CoreApplication.kt)
