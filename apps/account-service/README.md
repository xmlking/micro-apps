# Account

Account gRPC service.

Showcase backpressure handling techniques:

- Dropping the messages.
- Sensible buffering strategies (time vs count).
- Blocking the execution and processing the current set of events.
- Throttling and debouncing strategies.

### 🚀 Run

```bash
# server
gradle :apps:account-service:run
# proxy
nerdctl compose up envoy
# client -> proxy -> server
gradle :apps:account-service:runAccountClient
# client -> server
CERTS_CACERT=config/certs/upstream-ca-cert.pem \
ACCOUNT_ENDPOINT=dns:///localhost:5001 \
ACCOUNT_AUTHORITY=localhost \
gradle :apps:account-service:runAccountClient

CERTS_CACERT=config/certs/upstream-ca-cert.pem \
ACCOUNT_ENDPOINT=dns:///localhost:5001 \
ACCOUNT_AUTHORITY=localhost \
gradle :apps:account-service:runEchoClient
```

### 🔭 Test

```bash
gradle :apps:account-service:test
gradle :apps:account-service:test -Dkotest.tags.exclude=Slow
gradle :apps:account-service:test -Dkotest.tags.include=E2E
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
gradle :apps:account-service:clean
# make fatJar
gradle :apps:account-service:build
# docker build
gradle :apps:account-service:jibDockerBuild
# prune dangling images.
docker image prune -f
# run image
docker run -it -v $PWD/config:/config xmlking/micro-apps-account-service:1.6.5-SNAPSHOT
```

```bash
# custom build
gradle :apps:account-service:jib \
    -Djib.to.image=myregistry/myimage:latest \
    -Djib.to.auth.username=$USERNAME \
    -Djib.to.auth.password=$PASSWORD
```

#### Native (W.I.P)

```bash
# build native
gradle :apps:account-service:nativeBuild
gradle :apps:account-service:nativeRun
gradle :apps:account-service:nativeTestBuild
gradle :apps:account-service:nativeTest
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
