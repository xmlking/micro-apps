# Demo

Demo app. 

### Run
```bash
gradle :apps:demo:run
```

### Test
```bash
gradle :apps:demo:test
```

### Build
```bash
# clean
gradle :apps:demo:clean
# make fatJar
gradle :apps:demo:build
# docker build
gradle :apps:demo:jibDockerBuild
# run image
docker run -it xmlking/micro-apps-demo:1.6.1-SNAPSHOT
```
