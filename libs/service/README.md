# Service

`Micro Service Lib module` contains **shared** code for all **Services**

Any duplicate business logic code from all `apps/***-service` should be centralized in this module.

> Note: code here is not reusable beyond this workspace domain.

### Test

```bash
gradle libs:service:test
```

### Build

```bash
gradle libs:service:clean
gradle libs:service:build
```
