# Account

Account gRPC service. 

### Run
```bash
gradle :apps:account-grpc:run
```

### Test
```bash
gradle :apps:account-grpc:test
```

### Build
```bash
# clean
gradle :apps:account-grpc:clean
# make fatJar
gradle :apps:account-grpc:build
# docker build
gradle :apps:account-grpc:jibDockerBuild
# prune dangling images.
docker image prune -f
# run image
docker run -it xmlking/micro-apps-account-grpc:1.6.5-SNAPSHOT
```

```bash
# custom build
gradle :apps:account-grpc:jib \
    -Djib.to.image=myregistry/myimage:latest \
    -Djib.to.auth.username=$USERNAME \
    -Djib.to.auth.password=$PASSWORD
```
