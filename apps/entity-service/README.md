# Entity Service

Sample service that uses [Spring Native](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
with [Kotlin Coroutines](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
and [Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin). <br/>
Generate a native image to improve the start-up time.

Find more *Spring Native* [samples](https://github.com/spring-projects-experimental/spring-native/tree/main/samples)
here

### Showcase

- Interacting with *Reactive* Redis API with Kotlin *Coroutines*. Check the [blog](https://todd.ginsberg.com/post/springboot-reactive-kotlin-coroutines/)

## Features

- [x] Kotlin Coroutines
- [x] Kotlin [Serialization](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md)
- [x] Global Exception Handler
- [x] Input Validation
- [x] **Spring Data Redis** Repositories CRUD API
    - [x] Redis Hash
    - [ ] Redis Search 
    - [ ] Redis Graph
- [ ] Observability

## Setup

```bash
# To generate your own metadata using the annotation processor.
gradle :apps:entity-service:kaptKotlin
```

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

via docker-compose

```bash
# start local redis and grafana 
nerdctl compose -f infra/redis.yml up
# (Or) start only redis
nerdctl compose -f infra/redis.yml up redis
# stop local redis before restart again
nerdctl compose -f infra/redis.yml down
# this will stop redis and remove all volumes
nerdctl compose -f infra/redis.yml down -v 
```

**Redis Insight:** Redis db visualization dashboard

After opening RedisInsight App, connect using host:localhost, port:6379 and name:any_name_is_ok

for Redis Insight 1.x

```bash
open http://localhost:8001/
```

To monitor redis, use Grafana Dashboard

```bash
open http://localhost:3000/
```


```bash
gradle :apps:entity-service:bootRun
# log at debug level
gradle :apps:entity-service:bootRun --debug
```

```bash
open http://localhost:8080/account
```

## Test

```
gradle :apps:entity-service:test
gradle :apps:entity-service:integrationTest
```

We are using [httpie](https://httpie.io/) CLI for REST testing

```bash
# list
http :8080/account
# health
http :8080/actuator
http :8080/actuator/health
````

Check [API-TEST](./API-TEST.md) for more tests.

## Build

### Build fatJar

```bash
gradle :apps:entity-service:build
```

### Build native image

```bash
gradle :apps:entity-service:bootBuildImage -x test
```

Then, you can run the app like any other container:

```bash
docker run -i --rm -p 8080:8080 entity-service:1.6.5-SNAPSHOT
```

### Run in GCP
Set environment variables  in GKE

```bash
GOOGLE_CLOUD_PROJECT=my-project-id
GOOGLE_APPLICATION_CREDENTIALS=/path/to/googleCredentials.json
# optional
STACKDRIVER_LOG_NAME
STACKDRIVER_LOG_FLUSH_LEVEL
```

## TODO

- https://github.com/rnbWarden/jredisearch-spring-boot-starter
- use Redis Test Container [example](https://github.com/redis-developer/lettucemod/blob/master/subprojects/spring-lettucemod/src/test/java/com/redislabs/spring/lettucemod/RedisModulesAutoConfigurationIntegrationTests.java)
- `testImplementation("com.redislabs.testcontainers:testcontainers-redis:1.2.0")`

## Reference

- [Spring Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin)
- [The State of Kotlin Support in Spring](https://resources.jetbrains.com/storage/products/kotlin/events/kotlin14/Slides/spring.pdf)
- [Structured Concurrency](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
- [KoFu](https://github.com/spring-projects-experimental/spring-fu/tree/main/kofu)
- [KoFu Samples](https://github.com/spring-projects-experimental/spring-fu/tree/main/samples)
- [Functional Bean Registration Example](https://github.com/pwestlin/webshopkotlin/blob/master/core-service/src/main/kotlin/nu/westlin/webshop/core/CoreApplication.kt)
- [Spring Cloud GCP](https://googlecloudplatform.github.io/spring-cloud-gcp/reference/html/index.html)
