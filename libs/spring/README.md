# Spring Shared Components

This `Micro Service Lib module` contains **shared** code for all **Spring Boot REST Services**

Any duplicate business logic code from all **Spring REST Services** in `services/***` should be centralized in this module.

> Note: code here is not reusable beyond this workspace domain.

### Test

```bash
gradle libs:spring:test
```

### Build

```bash
gradle libs:spring:clean
gradle libs:spring:build
```
