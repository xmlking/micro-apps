# Spring-GraphQL

Demo GraphQL APIs with Spring Boot 3.x

## Features 
- [x] [Error Handling](https://www.baeldung.com/spring-graphql-error-handling)
  - [x] [DataFetcherExceptionResolverAdapter](https://docs.spring.io/spring-graphql/docs/current/reference/html/#execution.exceptions)
- [ ] Input Validation 
- [ ] JWT Authentication Provider & fine-grained security `@PreAuthorize`
 
## Run

```shell
gradle :services:spring-graphql:clean
gradle :services:spring-graphql:bootRun
# to use environment specific profiles 
SPRING_PROFILES_ACTIVE=test gradle :services:spring-graphql:bootRun
# or
gradle :services:spring-graphql:bootRun -Dspring.profiles.active=test
```

open http://localhost:8080/graphiql

> add token in JSON format in the `graphiql` Web Console under headers section.
```json
{
  "Authorization": "Bearer YOUR_TOKEN"
}
```


open http://localhost:8080/h2-console


## Test

```shell
http -v http://localhost:8080/actuator/info \
'Authorization: Bearer YOUR_TOKEN'

http http://localhost:8080/actuator/health \
'Authorization: Bearer YOUR_TOKEN'

```

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
