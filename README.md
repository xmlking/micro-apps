# Micro Apps

Modern microservices for Post-Kubernetes Era.

Monorepo(apps, libs) project to showcase workspace setup with multiple apps and shared libraries

## Features

1. **Polyglot** - Support multiple languages (java, kotlin, groovy)
2. Support multiple app frameworks (apache-beam, cli, micronaut, quarkus)
3. Support multiple serialization methods (JSON, Avro, ProtoBuf)
4. A type-safe cascading configuration with [konf](https://github.com/uchuhimo/konf)
5. Integrated with best testing frameworks ([kotest](https://github.com/kotest/kotest/blob/master/doc/reference.md)
   , [MockK](https://mockk.io/))
6. Build **lightweight** Docker and [OCI](https://github.com/opencontainers/image-spec) images
   with [Jib](https://github.com/GoogleContainerTools/jib)
7. Build native binaries using [GraalVM](https://www.graalvm.org/)
8. Cloud Native (Service Mesh, health checks, observability)
9. Platform independent Observability instrumentation via [OpenTelemetry](https://opentelemetry.io/)
10. Deployment automation with [kustomize](https://kustomize.io/) and Event-driven Autoscaling
    with [KEDA](https://keda.sh/)
11. End-to-End Google Cloud DevOps flow [pop-kustomize](https://github.com/vszal/pop-kustomize)

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

### Sub Modules

| Services                                         | Libs                      | Pipelines                            |
|--------------------------------------------------|---------------------------|--------------------------------------|
| [GraphQL-r2dbc](./services/spring-graphql-r2dbc) | [Core](./libs/core)       | [WordCount](./pipelines/wordcount)   |
| [Redis OM](./services/redis)                     | [KBeam](./libs/kbeam)     | [Classifier](./pipelines/classifier) |
| [Streams](./services/streams)                    | [Spring](./libs/spring)   | [Ingestion](./pipelines/ingestion)   |
| [Account](./services/account)                    | [GraphQL](./libs/graphql) |                                      |
|                                                  | [Test](./libs/test)       |                                      |
|                                                  | [Model](./libs/model)     |                                      |

## Setup

### Prerequisites

```shell
sdk rm java
sdk i java 17.0.6-zulu
java --version

sdk rm gradle
sdk i gradle
gradle --version

sdk i springboot
sdk i quarkus
```

### IntelliJ Plugins

1. [Spotless](https://plugins.jetbrains.com/plugin/18321-spotless-gradle)
2. [Ktlint](https://plugins.jetbrains.com/plugin/15057-ktlint-unofficial-/)
3. [Kotest](https://plugins.jetbrains.com/plugin/14080-kotest)
4. [GraphQL](https://plugins.jetbrains.com/plugin/8097-graphql)
5. [Env files support](https://plugins.jetbrains.com/plugin/9525--env-files-support)
6. [lombok](https://plugins.jetbrains.com/plugin/6317-lombok)

## Contribution

### Pre-commit checks

```shell
# spotlessCheck - Checks that sourcecode satisfies formatting steps.
gradle spotlessCheck
# spotlessApply - Applies code formatting steps to sourcecode in-place.
gradle spotlessApply
```

Run tests, lint, coverage for all subprojects

```shell
gradle check
```

### Code Quality

```shell
# Generates code coverage HTML and XML reports for all enabled test tasks in one project.
gradle koverMergedReport
```

Software Composition Analysis (SCA) tool that attempts to detect publicly disclosed vulnerabilities contained within a
project’s dependencies.

```shell
gradle dependencyCheckAggregate
```

### Maintenance

```shell
# Update versions in libs.versions.toml file
gradle versionCatalogUpdate --interactive
```

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

#### Dashboard access

* SonarQube → Browser http://localhost:9000 → `user: admin, password: admin`
* Grafana → Browser http://localhost:3000/plugins/redis-app/ → `user: admin, password: admin`
* Redis Insight → Browser http://localhost:8001/
* Jaeger → Browser http://localhost:16686
* Kibana → Browser http://localhost:5601
* Prometheus → Browser http://localhost:9090

See [gradle commands](docs/advanced/gradle.md) for this project.

### Apps

#### Apache Beam Pipelines

> Explore [Wordcount Pipeline](./pipelines/wordcount/)

> Explore [Streaming Ingestion Pipeline](./pipelines/ingestion/)

> Explore [Classifier Pipeline](./pipelines/classifier/)

#### Quarkus

> Explore [REST, OpenAPI, OpenID Connect, API](./services/greeting/)

> Explore [GraphQL API](./services/person/)

#### Kotlin-gRPC

> Explore [Account-gRPC API](./services/account/)

> Explore [Kotlin-gRPC API](./services/keying/)

> Explore [Kotlin-gRPC API](./services/linking/)

#### Spring

> Explore [Spring-RSocket Chat APP](./services/chat/)

> Explore [Spring CRUD + Redis Search API](./services/entity/)

#### Svelte

> Explore [SvelteKit WebApp](./services/webapp/)

### Libs

#### Reusable libraries

> Explore [Apache Beam Kotlin Extensions](./libs/kbeam/)

> Explore [Shared gRPC Components](./libs/grpc/)

#### Shared code within this repo

> Explore [Shared Core Code](./libs/core/)

> Explore [Shared Model Objects](./libs/model/)

> Explore [Shared Pipelines Code](./libs/pipeline/)

> Explore [Shared Spring Services Code](./libs/spring/)

> Explore [Shared Spring GraphQL Services Code](./libs/graphql/)
>
> Explore [Shared ProtoBuf Contracts](./libs/proto/)

> Explore [Shared Test Fixtures](./libs/test/)

### Inspiration

* Creating a [Multi Module Project](https://spring.io/guides/gs/multi-module/)
* Microservices in a Post-Kubernetes Era [link](https://www.infoq.com/articles/microservices-post-kubernetes)
* Why is a [workspace](https://nrwl.io/nx/why-a-workspace) (or monorepo) needed?
* Gradle Setup [arara](https://github.com/cereda/arara)
* GoogleCloudPlatform's [microservices-demo](https://github.com/GoogleCloudPlatform/microservices-demo)
