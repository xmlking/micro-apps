# greeting-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Prerequisites

1. A working container runtime (Docker, podman)
1. JDK 11 installed with JAVA_HOME configured appropriately
    ```bash
    sdk install java 11.0.6.j9-adpt
    export JAVA_HOME=$HOME/.sdkman/candidates/java/current
    ```
1. GraalVM version 20.0.0.r11 installed and configured appropriately
    ```bash
    sdk install java 20.0.0.r11-grl
    export GRAALVM_HOME=$HOME/.sdkman/candidates/java/20.0.0.r11-grl
    ```
1. Install the native-image tool using gu install:
    ```bash
    ${GRAALVM_HOME}/bin/gu install native-image
    ```
1. `brew install httpie`

1. Adding an empty `META-INF/beans.xml` to `src/main/resources` of dependency project, sothat POJO classes will also be indexed by **Quarkus**

    Ref: https://stackoverflow.com/questions/55513502/how-to-create-a-jandex-index-in-quarkus-for-classes-in-a-external-module

## scaffolding projects

> your can also use [code.quarkus.io](https://code.quarkus.io/?g=micro.apps&a=greeting-quarkus&v=1.0.0-SNAPSHOT&b=GRADLE&c=micro.apps.ExampleResource&s=ARC.dZK.tqK.OxX.Ll4.qZz&cn=code.quarkus.io) webApp to generate a new project

```bash
cd apps
mvn io.quarkus:quarkus-maven-plugin:1.5.0.Final:create \
    -DprojectGroupId=micro.apps \
    -DprojectArtifactId=greeting-quarkus \
    -DprojectVersion=0.1.0 \
    -DclassName="micro.apps.greeting.GreetingResource" \
    -Dpath="/greeting" \
    -Dextensions="kotlin, resteasy-jsonb, kubernetes, jib" \
    -DbuildTool=gradle
cd ..
```

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
gradle :apps:greeting-quarkus:quarkusDev
# You can also run a Quarkus application in debug mode with a suspended JVM using:
gradle :apps:greeting-quarkus:quarkusDev -Dsuspend -Ddebug
# Then, attach your debugger to localhost:5005.
```
> In development mode, Quarkus starts by default with debug mode enabled, listening to port 5005 without suspending the JVM.

## Packaging and running the application

The application can be packaged using `gradle :apps:greeting-quarkus:quarkusBuild`.
It produces the `greeting-quarkus-0.1.0-runner.jar` file in the `build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

The application is now runnable using `java -jar build/greeting-quarkus-0.1.0-runner.jar`.

If you want to build an _über-jar_, just add the `--uber-jar` option to the command line:
```
gradle :apps:greeting-quarkus:quarkusBuild --uber-jar
```

## Creating a native executable

You can create a native executable using: `gradle :apps:greeting-quarkus:buildNative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `gradle :apps:greeting-quarkus:buildNative --docker-build=true`.

You can then execute your native executable with: `./apps/greeting-quarkus/build/greeting-quarkus-1.6.1-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling#building-a-native-executable.


## Docker 

`quarkusBuild` command also creates docker image if `quarkus-container-image-jib` plugin is enabled.
> Check with `docker images`

`quarkusBuild` command also creates kubernetes YAML if `quarkus-kubernetes` plugin is enabled.
> Check `apps/greeting-quarkus/build/kubernetes/kubernetes.yml`

You can run image with `docker run -i --rm -p 8080:8080 xmlking/greeting-quarkus:1.6.5-SNAPSHOT`

### Manual Docker Build

```bash
# build jar/native first
gradle :apps:greeting-quarkus:quarkusDev
# or native
gradle :apps:greeting-quarkus:buildNative --docker-build=true
```

Then build docker image

```bash
cd apps/greeting-quarkus/

# DOCKER_REGISTRY=us.gcr.io
DOCKER_REGISTRY=docker.pkg.github.com
DOCKER_CONTEXT_PATH=xmlking/micro-apps
TARGET=greeting-quarkus
VERSION=1.6.1-SNAPSHOT

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

```bash
gradle :apps:greeting-quarkus:quarkusDev
open http://localhost:8080/swagger-ui/
```

Swagger UI:  http://localhost:8080/swagger-ui/

```bash
http :8080/health
http :8080/openapi
http :8080/metrics
http :8080/metrics/application
http :8080/api/v1/greeting
http :8080/api/v1/greeting/world

http :8080/api/fruits
```

### Unit tests

```bash
gradle :apps:greeting-quarkus:check # Runs all checks
gradle :apps:greeting-quarkus:test # Runs the unit tests.
gradle :apps:greeting-quarkus:testNative # Runs native image tests
```

## Gradle 

```bash

gradle :apps:greeting-quarkus:listExtensions
gradle :apps:greeting-quarkus:addExtension --extensions="health,metrics,openapi"
gradle :apps:greeting-quarkus:addExtension --extensions="hibernate-validator"
gradle :apps:greeting-quarkus:addExtension --extensions="jdbc,agroal,non-exist-ent"
gradle :apps:greeting-quarkus:addExtension --extensions="container-image-jib, kubernetes"

gradle :apps:greeting-quarkus:quarkusDev -Dsuspend -Ddebug

gradle :apps:greeting-quarkus:buildNative
gradle :apps:greeting-quarkus:testNative
```

## Reference 

- [BUILDING QUARKUS APPS WITH GRADLE](https://quarkus.io/guides/gradle-tooling)
- [Quarkus - Using Kotlin](https://github.com/quarkusio/quarkus/blob/master/docs/src/main/asciidoc/kotlin.adoc)
- [Quarkus - kubernetes Extension](https://quarkus.io/guides/kubernetes)
- [Quarkus - container image Extension](https://quarkus.io/guides/container-image)
