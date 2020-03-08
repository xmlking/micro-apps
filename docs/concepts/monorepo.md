# Monorepos

`Monorepo` encourages developers to split monolith codebase into microservices and microapps, 
and manage them in single repository so that we can effortlessly keep all sub-modules in sync with consistent dependencies and versions.
Developer can share code via `lib` modules. 

All `app` modules that includes `application` and `springboot` gradle plugins will automatically enabled for containerization with [Jib](https://github.com/GoogleContainerTools/jib)

#### Testing 
This `Monorepo` template project is test framework agnostic. You can use `Spek`, `JUnit` or `Spock` Framework.

#### Deploy
you can build `Docker` and [OCI](https://github.com/opencontainers/image-spec) images with [Jib](https://github.com/GoogleContainerTools/jib) and deploy to `Kubernetes` 

#### Polyglot
Each module can use your choice of JVM languages. Supports Java, Groovy, Kotlin, Scala. 
