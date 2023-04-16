# Account

Account gRPC service.

Showcase backpressure handling techniques:

- [x] Dropping the messages.
- [x] Sensible buffering strategies (time vs count).
- [x] Blocking the execution and processing the current set of events.
- [ ] Throttling and debouncing strategies.
- [ ] [Portable validations for Kotlin](https://github.com/konform-kt/konform)

### 🚀 Run

```bash
# server
gradle :services:account:run
# proxy
docker compose up envoy
# client -> proxy -> server
gradle :services:account:runAccountClient
# client -> server
CERTS_CACERT=config/certs/upstream-ca-cert.pem \
ACCOUNT_ENDPOINT=dns:///localhost:5001 \
ACCOUNT_AUTHORITY=localhost \
gradle :services:account:runAccountClient

CERTS_CACERT=config/certs/upstream-ca-cert.pem \
ACCOUNT_ENDPOINT=dns:///localhost:5001 \
ACCOUNT_AUTHORITY=localhost \
gradle :services:account:runEchoClient
```

### 🔭 Test

```bash
gradle :services:account:test
gradle :services:account:test -Dkotest.tags.exclude=Slow
gradle :services:account:test -Dkotest.tags.include=E2E
```

```bash
# test API directly with TLS
grpcurl -insecure \
-protoset <(buf build -o -) \
-d '{ "id":  "sumo" }' 0.0.0.0:5001 micro.apps.proto.account.v1.AccountService/Get

grpcurl -insecure \
-protoset <(buf build -o -) \
-d '{ "message":  "sumo" }' 0.0.0.0:5001 micro.apps.proto.echo.v1.EchoService/Echo

# test API via envoy with TLS, and client cert
grpcurl -cacert=config/certs/ca-cert.pem \
-cert=config/certs/client-cert.pem \
-key=config/certs/client-key.pem \
-protoset <(buf build -o -) \
-d '{ "id":  "sumo" }' localhost:9444 micro.apps.proto.account.v1.AccountService/Get

grpcurl -cacert=config/certs/ca-cert.pem \
-cert=config/certs/client-cert.pem \
-key=config/certs/client-key.pem \
-protoset <(buf build -o -) \
-d '{ "message":  "sumo" }' localhost:9444 micro.apps.proto.echo.v1.EchoService/Echo
```

```bash
# no TLS
grpcurl -plaintext \
-protoset <(buf build -o -) \
-d '{ "message":  "sumo" }' 0.0.0.0:9090 micro.apps.proto.echo.v1.EchoService/Echo
```

### 📦 Build

```bash
# clean
gradle :services:account:clean
# make fatJar
gradle :services:account:build
# docker build
gradle :services:account:jibDockerBuild
# prune dangling images.
docker image prune -f
# run image
docker run -it -v $PWD/config:/config xmlking/micro-services-account:1.6.5-SNAPSHOT
```

```bash
# custom build
gradle :services:account:jib \
    -Djib.to.image=myregistry/myimage:latest \
    -Djib.to.auth.username=$USERNAME \
    -Djib.to.auth.password=$PASSWORD
```

#### Native (W.I.P)

```bash
# build native
gradle :services:account:nativeCompile
gradle :services:account:nativeRun
gradle :services:account:nativeTestBuild
gradle :services:account:nativeTest
```

## 🔗 Credits

- [Announcing Open Source gRPC Kotlin Deck](https://www.cncf.io/wp-content/uploads/2020/04/Announcing-Open-Source-gRPC-Kotlin.pdf)
- [A collection of useful/essential gRPC Java Examples](https://github.com/saturnism/grpc-by-example-java)
- [Lessons Learned Implementing Microservices in Kubernetes](https://saturnism.me/talk/kubernetes-microservices-lessons-learned/)
- [Death Star demo App by Marharyta](https://github.com/leveretka/grpc-death-star)
- [kotlin-samples](https://github.com/GoogleCloudPlatform/kotlin-samples/tree/master/run)
- [gRPC Server Reflection Tutorial](https://github.com/grpc/grpc-java/blob/master/documentation/server-reflection-tutorial.md)
- [traffic-director-grpc-examples](https://github.com/GoogleCloudPlatform/traffic-director-grpc-examples)
- [Using FieldMask with gRPC/Protobuf to emulate GraphQL](https://netflixtechblog.com/practical-api-design-at-netflix-part-1-using-protobuf-fieldmask-35cfdc606518)
    - [Protobuf Field Masks](https://pinkiepractices.com/posts/protobuf-field-masks/)
