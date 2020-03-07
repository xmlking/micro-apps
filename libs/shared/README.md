# Shared Lib

Contains shared spring domain objects, repositories , beans, utils etc.
Can contain dependencies from spring boot/cloud/streams.
Please make sure app modules which include this `shared` module also includes those dependencies.

### Run
```bash
gradle libs:shared:clean
```
### Test
```bash
gradle libs:shared:test
```
### Build
```bash
gradle libs:shared:build
```