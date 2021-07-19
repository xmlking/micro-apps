# Entity Service

Sample service that uses [Spring Native](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
with [Kotlin Coroutines](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
and [Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin). <br/>
Generate a native image to improve the start-up time.

Find more *Spring Native* [samples](https://github.com/spring-projects-experimental/spring-native/tree/main/samples)
here

This µService also showcase running both **REST** and **gRPC** services on single server.

## Features

- [x] Kotlin Coroutines
- [x] Kotlin [Serialization](https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md)
- [x] Global Exception Handler
- [x] Input Validation
- [x] Spring Data Redis Repositories 
    - [x] Redis Hash
    - [ ] Redis Search 
    - [ ] Redis Graph
- [ ] Observability


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
# start local redis
docker compose -f infra/redis.yml up
# stop local redis before restart again
docker compose -f infra/redis.yml down
# this will stop redis and remove all volumes
docker compose -f infra/redis.yml down -v 
```

**Redis Insight:** Redis db visualization dashboard

> when prompted, use host:redis, port:6379 and name:any_name

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

We are using [httpie](https://httpie.io/) CLI for REST testing

```bash
# list
http :8080/account

# get by Id
http :8080/account/67531e6d-fe00-4d78-afa1-ac008ced47af

# create
http POST :8080/account << END 
{
    "name": {
      "first": "sumo",
      "last": "demo"
    },
    "addresses": [
      {
        "suite": "A212",
        "street": "FourWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "34453",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      },
      {
        "suite": "B212",
        "street": "ThreeWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "44553",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      }
    ],
    "gender": "MALE",
    "age": 34,
    "email": "sumo@demo.com",
    "phone": "3334442222"
  }
END
# bad request
http POST :8080/account << END 
{
    "name": {
      "first": "q😀"
      "last": ""
    },
    "addresses": [
      {
        "suite": "A212",
        "street": "FourWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "34453",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      },
      {
        "suite": "B212",
        "street": "ThreeWinds Dr",
        "city": "Corona",
        "state": "CA",
        "code": "11",
        "country": "USA",
        "location": [-77.0364, -38.8951]
      }
    ],
    "gender": "MALE",
    "age": 11,
    "email": "sumodemo.com",
    "phone": "3334442222"
  }
END
```

## Build

### Build fatJar

```bash
gradle :apps:entity-service:build
```

### Build native image

```bash
gradle :apps:entity-service:bootBuildImage 
```

## Reference

- [Spring Kotlin](https://docs.spring.io/spring-framework/docs/current/reference/html/languages.html#kotlin)
- [Structured Concurrency](https://spring.io/blog/2019/04/12/going-reactive-with-spring-coroutines-and-kotlin-flow)
- [KoFu](https://github.com/spring-projects-experimental/spring-fu/tree/main/kofu)
- [KoFu Samples](https://github.com/spring-projects-experimental/spring-fu/tree/main/samples)
- [Functional Bean Registration Example](https://github.com/pwestlin/webshopkotlin/blob/master/core-service/src/main/kotlin/nu/westlin/webshop/core/CoreApplication.kt)

