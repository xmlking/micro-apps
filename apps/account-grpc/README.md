# Account

Account gRPC service. 

### 🚀 Run
```bash
gradle :apps:account-grpc:run
```

### 🔭 Test
```bash
gradle :apps:account-grpc:test
gradle :apps:account-grpc:test -Dkotest.tags.exclude=Slow
gradle :apps:account-grpc:test -Dkotest.tags.include=E2E
```

### 📦 Build
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

## 🔗 Credits
- [Announcing Open Source gRPC Kotlin Deck](https://www.cncf.io/wp-content/uploads/2020/04/Announcing-Open-Source-gRPC-Kotlin.pdf)
- [A collection of useful/essential gRPC Java Examples](https://github.com/saturnism/grpc-by-example-java)
- [Lessons Learned Implementing Microservices in Kubernetes](https://saturnism.me/talk/kubernetes-microservices-lessons-learned/)
- [Death Star demo App by Marharyta](https://github.com/leveretka/grpc-death-star)
