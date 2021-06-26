# Micro Apps

Modern microservices for Post-Kubernetes Era.

Monorepo(apps, libs) project to showcase workspace setup with multiple apps and shared libraries

### Features

1. **Polyglot** - Support multiple languages (java, kotlin, groovy)
1. Support multiple app frameworks (apache-beam, cli, micronaut, quarkus)
1. Support multiple serialization methods (JSON, Avro, ProtoBuf)
1. A type-safe cascading configuration with [konf](https://github.com/uchuhimo/konf)
1. Integrated with best testing frameworks ([kotest](https://github.com/kotest/kotest/blob/master/doc/reference.md)
   , [MockK](https://mockk.io/))
1. Build **lightweight** Docker and [OCI](https://github.com/opencontainers/image-spec) images
   with [Jib](https://github.com/GoogleContainerTools/jib)
1. Build native binaries using [GraalVM](https://www.graalvm.org/)
1. Cloud Native (Service Mesh, health checks, observability)
1. Deployment automation with [kustomize](https://kustomize.io/) and Event-driven Autoscaling
   with [KEDA](https://keda.sh/)

[![Check](https://github.com/xmlking/micro-apps/workflows/Check/badge.svg)](https://github.com/xmlking/micro-apps/actions?query=workflow%3ACheck)
[![Version](https://img.shields.io/github/v/tag/xmlking/micro-apps)](https://github.com/xmlking/micro-apps/tags)
[![License](https://img.shields.io/github/license/xmlking/micro-apps)](https://github.com/xmlking/micro-apps/blob/develop/LICENSE)

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
docker compose up postgres # docker compose up -V postgres
# stop local postgres before restart again
docker compose down # docker compose down -v
```

> start optional CI/CD infra dependencies: sonarqube, nexus

```bash
# start local sonarqube
docker compose up sonarqube # docker compose up -V sonarqube
# stop local sonarqube before restart again
docker compose down
# (optional) this remove volumes - needed when you upgrade image versions 
docker compose down -v
# start local nexus
docker compose up nexus
```

access **sonarqube** at http://localhost:9000/ (admin/admin)

See [gradle commands](docs/advanced/gradle.md) for this project.

#### Apache Beam pipelines

> Start [wordcount Pipeline](./apps/wordcount-pipeline/)
>
> Start [streaming Pipeline](./apps/ingestion-pipeline/)

#### Quarkus

> Start [Greeting API](./apps/greeting-service/)

#### Kotlin-gRPC

> Start [Kotlin-gRPC API](./apps/account-service/)

#### Spring-RSocket

> Start [Spring Chat APP](./apps/chat-service/)

#### Micronaut

> Start [Greeting API](./apps/greeting-micronaut/)

### Inspiration

* Creating a [Multi Module Project](https://spring.io/guides/gs/multi-module/)
* Microservices in a Post-Kubernetes Era [link](https://www.infoq.com/articles/microservices-post-kubernetes)
* Why is a [workspace](https://nrwl.io/nx/why-a-workspace) (or monorepo) needed?
* Gradle Setup [arara](https://github.com/cereda/arara)
