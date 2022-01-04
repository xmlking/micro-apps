# gRPC

gRPC lib contains **shared** code for all **gRPC Services**

Any duplicate business logic code from all **gRPC** in `apps/***-service` should be centralized in this module.

> Note: code here is not reusable beyond this workspace domain.

### Test

```bash
gradle libs:grpc:test
```

### Build

```bash
gradle libs:grpc:clean
gradle libs:grpc:build
```
