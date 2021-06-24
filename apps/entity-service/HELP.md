# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.1/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.1/gradle-plugin/reference/html/#build-image)
* [Coroutines section of the Spring Framework Documentation](https://docs.spring.io/spring/docs/5.3.8/spring-framework-reference/languages.html#coroutines)
* [Spring Native Reference Guide](https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/)
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.5.1/reference/htmlsingle/#boot-features-redis)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/2.5.1/reference/htmlsingle/#production-ready)

### Guides

The following guides illustrate how to use some features concretely:

* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)
* [Configure the Spring AOT Plugin](https://docs.spring.io/spring-native/docs/0.10.0/reference/htmlsingle/#spring-aot-gradle)

## Spring Native

This project has been configured to let you generate either a lightweight container or a native executable.

### Lightweight Container with Cloud Native Buildpacks

If you're already familiar with Spring Boot container images support, this is the easiest way to get started with Spring
Native. Docker should be installed and configured on your machine prior to creating the image,
see [the Getting Started section of the reference guide](https://docs.spring.io/spring-native/docs/0.10.0/reference/htmlsingle/#getting-started-buildpacks)
.

To create the image, run the following goal:

```
$ ./gradlew bootBuildImage
```

Then, you can run the app like any other container:

```
$ docker run --rm service:0.0.1-SNAPSHOT
```

### Executable with Native Build Tools

Use this option if you want to explore more options such as running your tests in a native image. The GraalVM
native-image compiler should be installed and configured on your machine,
see [the Getting Started section of the reference guide](https://docs.spring.io/spring-native/docs/0.10.0/reference/htmlsingle/#getting-started-native-build-tools)
.

To create the executable, run the following goal:

```
$ ./gradlew nativeBuild
```

Then, you can run the app as follows:

```
$ build/native-image/service
```
