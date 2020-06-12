# μService

`μService Lib module` contains **shared** code for all **μServices** 

Any  duplicate business logic code from all `apps/***-service` should be centralized in this module.

> Note: code here is not reusable beyond this workspace domain.
 
### Test
```bash
gradle libs:μservice:test
```
### Build
```bash
gradle libs:μservice:clean
gradle libs:μservice:build
```
