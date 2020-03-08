# Micro Apps

Modern microservices for Post-Kubernetes Era.

Monorepo(apps, libs) project to showcase workspace setup with multiple apps and shared libraries

### Features
1. **Polyglot** - Support multiple languages (java, kotlin, groovy)
2. Support multiple app frameworks (apache-beam, cli, micronaut, quarkus)
3. Support multiple testing frameworks (spock, spek, kotlin-test and junit5) 
4. Build **lightweight** Docker and [OCI](https://github.com/opencontainers/image-spec) images with [Jib](https://github.com/GoogleContainerTools/jib)
5. Build native binaries using [GraalVM](https://www.graalvm.org/)
6. Cloud Native (Service Mesh, health checks, observability)


### Run

#### Docker
> start app dependencies: postgres, redis
```bash
# start local sonarqube
docker-compose up -V postgres
# stop local postgres before restart again
docker-compose down -v
```

> start optional CI/CD infra dependencies: sonarqube, nexus 
```bash
# start local sonarqube
docker-compose up -V sonarqube
# stop local sonarqube before restart again
docker-compose down -v
# start local nexus
docker-compose up nexus
```

See [gradle commands](docs/advanced/gradle.md) for this project.


#### Apache Beam pipelines
> Start [wordcount](./apps/wordcount/)

#### Micronaut
> Start [Greeting API](./apps/greeting-api/)

> Start [Hello World Native API](./apps/hello-world-native/)

#### Quarkus

### Inspiration 
* Creating a [Multi Module Project](https://spring.io/guides/gs/multi-module/)
* Microservices in a Post-Kubernetes Era [link](https://www.infoq.com/articles/microservices-post-kubernetes)
* Why is a [workspace](https://nrwl.io/nx/why-a-workspace) (or monorepo) needed? 

