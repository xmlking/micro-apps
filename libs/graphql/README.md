# Spring GraphQL Components

This `GraphQL Service Lib module` contains **shared** code for all **Spring Boot GraphQL Services**

Any duplicate business logic code from all **Spring GraphQL Services** in `services/***` should be centralized in this module.

> Note: code here is not reusable beyond this workspace domain.

### Test

```bash
gradle libs:graphql:test
```

### Build

```bash
gradle libs:graphql:clean
gradle libs:graphql:build
```
