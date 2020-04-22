# Model

This module contains only models.
 
Convention is to keep `model` module not to depend on 3rd party dependencies except `serialization`.
Contain data classes used across multiple `apps`.
All `model` serializable to `JSON`, `Avro` and `ProtoBuf`

### Run
```bash
gradle libs:model:clean
```
### Test
```bash
gradle libs:model:test
```
### Build
```bash
gradle libs:model:build
```
