# Test-Utils

Test Utils Lib module. 

contains test helper functions, fixtures and `kotest` tags etc

### Usage 

import it as `testImplementation` in your `build.gradle.kts`

```gradle
testImplementation(testFixtures(project(":libs:test")))
```

### Test
```bash
gradle libs:test:test
```
### Build
```bash
gradle libs:test:build
```
