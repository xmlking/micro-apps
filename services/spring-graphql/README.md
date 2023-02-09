# Spring-GraphQL

Demo GraphQL APIs with Spring Boot 3.x

## Features 
- [x] Error Handling
- [ ] Input Validation 
- [x] JWT **AuthN** and **AuthZ**
- [x] Use `.env` for [sensitive properties](https://stackoverflow.com/questions/58549361/using-dotenv-files-with-spring-boot)

## Setup

> Make a copy `.env.example` as `.env` in the subproject root  
> Add any environment variables that you want to overwrite in application.yml

## Run

```shell
gradle :services:spring-graphql:clean
gradle :services:spring-graphql:bootRun
# to use environment specific profiles 
SPRING_PROFILES_ACTIVE=test gradle :services:spring-graphql:bootRun
# or
gradle :services:spring-graphql:bootRun -Dspring.profiles.active=test
```

### GraphQL Web Console
open http://localhost:8080/graphiql

Login: admin : nimda

> add token in JSON format in the `graphiql` Web Console under headers section.
```json
{
    "Authorization": "Basic YWRtaW46bmltZGE="
}
```

### GraphQL H2 Console

open http://localhost:8080/h2-console

Login: admin : nimda

```shell
http :8080/actuator/info
http :8080/actuator/health
http :8080/actuator/metrics
http -a actuator:rotautca :8080/actuator/metrics
http -a actuator:rotautca :8080/actuator
http -a actuator:rotautca :8080/actuator/metrics/spring.security.http.secured.requests
http -a actuator:rotautca :8080/actuator/metrics/jvm.info
http -a actuator:rotautca :8080/actuator/flyway
http -a actuator:rotautca :8080/actuator/env
```

## Test

```shell
gradle :services:spring-graphql:test
```

## Maintenance

```shell
gradle :services:spring-graphql:flywayInfo
gradle :services:spring-graphql:flywayMigrate
gradle :services:spring-graphql:flywayRepair
gradle :services:spring-graphql:flywayValidate
```


## Reference 

- [Spring for GraphQL Documentation](https://docs.spring.io/spring-graphql/docs/current/reference/html/#overview) 
