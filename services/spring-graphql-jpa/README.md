# Spring-GraphQL

Demo GraphQL APIs with Spring Boot 3.x

## Features 
- [x] Error Handling
- [ ] Input Validation 
- [x] JWT **AuthN** and **AuthZ**
- [x] Use `.env` for [sensitive properties](https://stackoverflow.com/questions/58549361/using-dotenv-files-with-spring-boot)

## Setup

> Make a copy `.env.template` as `.env` in the subproject root  
> Add any environment variables that you want to overwrite in application.yml

## Run

```bash
# To generate your own metadata using the annotation processor.
gradle :services:spring-graphq-jpa:kaptKotlin
```

```shell
gradle :services:spring-graphq-jpa:clean
gradle :services:spring-graphq-jpa:bootRun
# to use environment specific profiles
SPRING_PROFILES_ACTIVE=local gradle :services:spring-graphq-jpa:bootRun
# or
gradle :services:spring-graphq-jpa:bootRun -Dspring.profiles.active=local
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
http :8080/actuator/metrics # this should fail with 401
http -a actuator:rotautca :8080/actuator/metrics
http -a actuator:rotautca :8080/actuator
http -a actuator:rotautca :8080/actuator/metrics/spring.security.http.secured.requests
http -a actuator:rotautca :8080/actuator/metrics/jvm.info
http -a actuator:rotautca :8080/actuator/flyway
http -a actuator:rotautca :8080/actuator/env
```

## Test

```shell
gradle :services:spring-graphq-jpa:test
```

## Maintenance

```shell
gradle :services:spring-graphq-jpa:flywayInfo
gradle :services:spring-graphq-jpa:flywayBaseline
gradle :services:spring-graphq-jpa:flywayClean
gradle :services:spring-graphq-jpa:flywayMigrate
gradle :services:spring-graphq-jpa:flywayValidate
gradle :services:spring-graphq-jpa:flywayUndo
gradle :services:spring-graphq-jpa:flywayRepair
```


## Reference 

- [Spring for GraphQL Documentation](https://docs.spring.io/spring-graphql/docs/current/reference/html/#overview) 
- [GraphQL Fullstack Samples using Spring GraphQL & React](https://github.com/susimsek/spring-graphql-samples)
- [Spring Boot CodeGen GraphQL Gradle plugin](https://github.com/graphql-java-generator/graphql-maven-plugin-project/wiki/client_spring)
- [GraphQL Kotlin](https://opensource.expediagroup.com/graphql-kotlin/docs)
- [Managing flyway migrations using Placeholders in PostgreSQL/MySQL/H2 in Spring Boot](https://medium.com/@justdpk/managing-multiple-flyway-migrations-in-postgresql-mysql-h2-in-spring-boot-e790f07547b3)
