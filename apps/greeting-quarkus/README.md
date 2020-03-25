# greeting-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## scaffolding projects

```bash
cd apps
mvn io.quarkus:quarkus-maven-plugin:1.3.0.Final:create \
    -DprojectGroupId=micro.apps \
    -DprojectArtifactId=greeting-quarkus \
    -DprojectVersion=0.1.0 \
    -DclassName="micro.apps.greeting.GreetingResource" \
    -Dpath="/greeting" \
    -Dextensions="kotlin,resteasy-jsonb,health" \
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

You can then execute your native executable with: `./build/greeting-quarkus-0.1.0-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling#building-a-native-executable.


## Docker 
```bash
docker build -f src/main/docker/Dockerfile.native -t quarkus/quarkus-project .
docker run -i --rm -p 8080:8080 quarkus/quarkus-project
```

## Testing

```bash
http :8080/health
http :8080/openapi
http :8080/api/v1/greeting
http :8080/api/v1/greeting/world
```

## Gradle 

```bash
gradle :apps:greeting-quarkus:listExtensions
gradle :apps:greeting-quarkus:addExtension --extensions="hibernate-validator"
gradle :apps:greeting-quarkus:addExtension --extensions="jdbc,agroal,non-exist-ent"
 
gradle :apps:greeting-quarkus:quarkusDev -Dsuspend -Ddebug
```

## Reference 

- [BUILDING QUARKUS APPS WITH GRADLE](https://quarkus.io/guides/gradle-tooling)
- [Quarkus - Using Kotlin](https://github.com/quarkusio/quarkus/blob/master/docs/src/main/asciidoc/kotlin.adoc)
