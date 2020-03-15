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

### Quality
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=xmlking_jvm-gitops&metric=alert_status)](https://sonarcloud.io/dashboard?id=xmlking_jvm-gitops)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=xmlking_jvm-gitops&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=xmlking_jvm-gitops)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=xmlking_jvm-gitops&metric=security_rating)](https://sonarcloud.io/dashboard?id=xmlking_jvm-gitops)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=xmlking_jvm-gitops&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=xmlking_jvm-gitops)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=xmlking_jvm-gitops&metric=bugs)](https://sonarcloud.io/dashboard?id=xmlking_jvm-gitops)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=xmlking_jvm-gitops&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=xmlking_jvm-gitops)

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
access **sonarqube** at http://localhost:9000/ (admin/admin)

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

