# greeting-service

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

Find more *Quarkus QuickStart* [samples](https://github.com/quarkusio/quarkus-quickstarts) here

## Prerequisites

1. A working container runtime (Docker, podman)
1. JDK 17 installed with JAVA_HOME configured appropriately
    ```bash
    sdk install java 17.0.4-zulu
    export JAVA_HOME=$HOME/.sdkman/candidates/java/current
    ```
1. GraalVM version 22.2.r17-grl installed and configured appropriately
    ```bash
    sdk install java  22.2.r17-grl
    export GRAALVM_HOME=$HOME/.sdkman/candidates/java/22.2.r17-grl
    ```
1. Install the native-image tool using gu install:
    ```bash
    ${GRAALVM_HOME}/bin/gu install native-image
    ```
1. `brew install httpie`

1. Adding an empty `META-INF/beans.xml` to `src/main/resources` of dependency project, sothat POJO classes will also be
   indexed by **Quarkus**

   Ref: https://stackoverflow.com/questions/55513502/how-to-create-a-jandex-index-in-quarkus-for-classes-in-a-external-module

## scaffolding projects

> your can also use [code.quarkus.io](https://code.quarkus.io/?g=micro.apps&a=greeting-service&v=1.0.0-SNAPSHOT&b=GRADLE_KOTLIN_DSL&s=ARC.dZK.tqK.qZz.Ll4.OxX.fgL&cn=code.quarkus.io) webApp to generate a new project

```bash
cd apps
mvn io.quarkus:quarkus-maven-plugin:2.11.2.Final:create \
    -DprojectGroupId=micro.apps \
    -DprojectArtifactId=greeting-service \
    -DprojectVersion=0.1.0 \
    -DclassName="micro.apps.service.GreetingResource" \
    -Dpath="/greeting" \
    -Dextensions="kotlin, config-yaml, resteasy-jsonb, oidc, kubernetes, jib, grpc" \
    -DbuildTool=gradle
cd ..
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```
gradle :apps:greeting-service:quarkusDev
# You can also run a Quarkus application in debug mode with a suspended JVM using:
gradle :apps:greeting-service:quarkusDev -Dsuspend -Ddebug
# Then, attach your debugger to localhost:5005.
```

> In development mode, Quarkus starts by default with debug mode enabled, listening to port 5005 without suspending the JVM.

## Packaging and running the application

The application can be packaged using

```shell script
gradle :apps:greeting-service:quarkusBuild
```

It produces the `greeting-service-0.1.0-SNAPSHOT-runner.jar` file in the `/build` directory. Be aware that it’s not an _
über-jar_ as the dependencies are copied into the `build/lib` directory.

If you want to build an _über-jar_, execute the following command:

```shell script
gradle :apps:greeting-service:quarkusBuild --uber-jar
```

The application is now runnable using `java -jar build/greeting-service-0.1.0-SNAPSHOT-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
# ignore error: The native binary produced by the build is not a Linux binary
gradle :apps:greeting-service:build -Dquarkus.package.type=native
```

You can then execute your native executable with: `./apps/greeting-service/build/greeting-service-1.6.5-SNAPSHOT-runner`

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
gradle :apps:greeting-service:build -Dquarkus.package.type=native -Dquarkus.native.container-build=true
gradle :apps:greeting-service:build -Dquarkus.package.type=native -Dquarkus.native.container-build=true -Dquarkus.native.native-image-xmx=8g
```

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling.

## Docker

`quarkusBuild` command also creates docker image if `quarkus-container-image-jib` plugin is enabled.
> Check with `docker images`

`quarkusBuild` command also creates kubernetes YAML if `quarkus-kubernetes` plugin is enabled.
> Check `apps/greeting-service/build/kubernetes/kubernetes.yml`

You can run image with `docker run -i --rm -p 8080:8080 xmlking/greeting-service:1.6.5-SNAPSHOT`

### Manual Docker Build

```bash
# build jar/native first
gradle :apps:greeting-service:quarkusDev
# or native
gradle :apps:greeting-service:buildNative --docker-build=true
```

Then build docker image

```bash
cd apps/greeting-service/

# DOCKER_REGISTRY=us.gcr.io
DOCKER_REGISTRY=docker.pkg.github.com
DOCKER_CONTEXT_PATH=xmlking/micro-apps
TARGET=greeting-service
VERSION=1.6.5-SNAPSHOT

IMANGE_NAME=${DOCKER_REGISTRY:+${DOCKER_REGISTRY}/}${DOCKER_CONTEXT_PATH}/${TARGET}:${VERSION}

# with jvm 
docker build -f src/main/docker/Dockerfile.jvm -t $IMANGE_NAME .
# or with native
docker build -f src/main/docker/Dockerfile.native -t $IMANGE_NAME .

docker run -i --rm -p 8080:8080 $IMANGE_NAME

# check
docker inspect  $IMANGE_NAME

# push
docker push $IMANGE_NAME

# remove temp images after build
docker image prune -f
```

## Testing

### REST

```bash
gradle :apps:greeting-service:quarkusDev
open http://localhost:8080/q/swagger-ui/
```

Swagger UI: http://localhost:8080/q/swagger-ui/
Health UI: http://localhost:8080/q/health-ui

```bash
http :8080/q/health
http :8080/q/health/live
http :8080/q/health/ready

http :8080/q/openapi
http :8080/q/metrics
http :8080/q/metrics/application

http :8080/api/v1/greeting
http :8080/api/v1/greeting/world

http :8080/api/fruits

http :8080/api/v1/account/id/abc123
http :8080/api/v1/account/user-info 'Authorization:Bearer GOOGLE.TOKEN'
```

### GRPC

```bash
# no TLS
grpcurl -plaintext \
-protoset <(buf build -o -) \
-d '{ "message":  "sumo" }' 0.0.0.0:9000 micro.apps.proto.echo.v1.EchoService/Echo

grpcurl -plaintext \
-protoset <(buf build -o -) \
-d '{ "message":  "sumo" }' 0.0.0.0:9000 micro.apps.proto.echo.v1.EchoService/EchoStream
```

### Unit tests

```bash
gradle :apps:greeting-service:check # Runs all checks
gradle :apps:greeting-service:test # Runs the unit tests.
gradle :apps:greeting-service:testNative # Runs native image tests
```

## Gradle

```bash

gradle :apps:greeting-service:listExtensions
gradle :apps:greeting-service:addExtension --extensions="health,metrics,openapi"
gradle :apps:greeting-service:addExtension --extensions="hibernate-validator"
gradle :apps:greeting-service:addExtension --extensions="jdbc,agroal,non-exist-ent"
gradle :apps:greeting-service:addExtension --extensions="container-image-jib, kubernetes"

gradle :apps:greeting-service:quarkusDev -Dsuspend -Ddebug

gradle :apps:greeting-service:buildNative
gradle :apps:greeting-service:testNative
```

## Reference

- [Quarkus - Command Mode](https://quarkus.io/guides/command-mode-reference)
- [BUILDING QUARKUS APPS WITH GRADLE](https://quarkus.io/guides/gradle-tooling)
- [Quarkus - Using Kotlin](https://github.com/quarkusio/quarkus/blob/master/docs/src/main/asciidoc/kotlin.adoc)
- [Quarkus - kubernetes Extension](https://quarkus.io/guides/kubernetes)
- [Quarkus - container image Extension](https://quarkus.io/guides/container-image)
- [Quarkus - Java - Todos Example](https://github.com/ineat/karate-quarkus-demo)
- [MapStruct + Quarkus](https://github.com/mapstruct/mapstruct-examples/tree/master/mapstruct-quarkus)
