# Pipeline

`Pipeline Lib module` contains **shared** code for all **Pipelines** 

Any duplicate business logic code from all `apps/***-pipeline` should be centralized in this module

### Test
```bash
gradle libs:pipeline:test
```
### Build
```bash
gradle libs:pipeline:clean
gradle libs:pipeline:build
```
