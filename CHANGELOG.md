# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

<a name="unreleased"></a>
## [Unreleased]

### Build
- **clog:** fix changelog template to remove 'v' from tag name
- **deploy:** adding: Scheduled Apache Beam jobs using Kubernetes Cronjobs
- **deps:** updated quarkus to 1.4.2.Final, now k8s and jib docker builds works
- **deps:** updated quarkus to 1.4.2.Final
- **deps:** updating deps for quarkus
- **deps:** quarkus --> 1.4.0.Final
- **gradle:** change spotlessChangelog plugin to remove V prefix from tag

### Chore
- **build:** adding spotless-changelog gradle plugin, making Junit5 as default
- **deps:** update dependency io.mockk:mockk to v1.10.0
- **deps:** update dependency io.grpc:grpc-testing to v1.29.0
- **deps:** update dependency io.quarkus:io.quarkus.gradle.plugin to v1.4.0.final
- **deps:** update dependency io.quarkus:quarkus-bom to v1.4.1.final
- **deps:** update dependency io.quarkus:quarkus-bom to v1.4.1.final
- **logging:** remove flogger
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding test helper to create topics
- **streaming:** adding test helper to create topics
- **streaming:** updated person schema
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** adding EnricherPipeline
- **streaming:** fix tests
- **streaming:** using tags to split output

### Ci
- **streaming:** improve test helper
- **streaming:** updated logging backend
- **streaming:** adding build time config

### Docs
- **readme:** updated grpc link

### Feat
- **micro:** refactor
- **streaming:** adding side-input and split usage
- **streaming:** adding  PubSubProducer test data generator

### Fix
- **deps:** remove Arrow deps

### Improvement
- **account-service:** set port to 8080 , fix docker builds
- **account-service:** set port to 8080 , fix docker builds
- **account-service:** set port to 8080 , fix docker builds
- **account-service:** adding account-service client for testing streaming API
- **core:** adding more tests for avro serialization
- **core:** using kotlinx-serialization for json,avro,protobuf
- **gradle:** polich gradle files
- **kbeam:** adding default name for ParDo
- **kbeam:** refactor API
- **streaming:** adding google pubsub emulator
- **streaming:** adding streaming pipeline docs under docs/recipes
- **streaming:** updated docs to query messages from pubsub emulator
- **workspace:** removed demo/dlib, split core into core + model projects

### Style
- **grpc:** rename class


<a name="1.6.4"></a>
## [1.6.4] - 2020-04-18
### Build
- **deps:** updated deps

### Chore
- **clog:** updating changelog
- **deps:** update dependency org.apache.beam:beam-sdks-java-io-google-cloud-platform to v2.20.0
- **deps:** update dependency org.apache.beam:beam-sdks-java-core to v2.20.0
- **deps:** update dependency org.apache.beam:beam-runners-google-cloud-dataflow-java to v2.20.0
- **deps:** update dependency org.apache.beam:beam-runners-direct-java to v2.20.0
- **deps:** update dependency org.jetbrains.kotlin.jvm:org.jetbrains.kotlin.jvm.gradle.plugin to v1.3.72

### Feat
- **account-service:** adding new kotlin-grpc example
- **proto:** adding proto module

### Improvement
- **deploy:** removed unused code from yaml
- **mock:** adding mockk

### Style
- **lint:** ktlint fix


<a name="1.6.3"></a>
## [1.6.3] - 2020-04-15
### Docs
- **clog:** updating change log

### Improvement
- **deploy:** polish Kustomization
- **deploy:** polish Kustomization


<a name="1.6.2"></a>
## [1.6.2] - 2020-04-15
### Chore
- **deploy:** test Kustomization patches


<a name="1.6.1"></a>
## [1.6.1] - 2020-04-15
### Appctl
- added scaffolding structure for the new environment: production.
- added scaffolding structure for the new environment: staging.
- added scaffolding structure for the new environment: development.
- Initialized app repository, scaffolding the app configuration in kustomize format under config directory config

### Build
- **deps:** upgraded to 1.3.2.Final
- **gradle:** adding excludedProjects for empty projects `apps` `libs`
- **gradle:** fix kotlin version
- **quarkus:** adding an empty `META-INF/beans.xml` to `src/main/resources` of dependency project

### Chore
- **deploy:** bootstraping config
- **deploy:** adding Elasticsearch deployment via kustomization
- **deploy:** adding nifi k8s deployment via kustomization
- **deps:** update dependency com.diffplug.gradle.spotless:com.diffplug.gradle.spotless.gradle.plugin to v3.28.1
- **deps:** update dependency io.quarkus:io.quarkus.gradle.plugin to v1.3.2.final
- **deps:** add renovate.json
- **deps:** update dependency io.quarkus:io.quarkus.gradle.plugin to v1.3.1.final
- **deps:** update dependency io.quarkus:quarkus-universe-bom to v1.3.2.final
- **deps:** update dependency com.google.flogger:flogger-system-backend to v0.5.1
- **deps:** update dependency com.google.flogger:flogger-slf4j-backend to v0.5.1
- **deps:** update dependency com.google.flogger:flogger to v0.5.1
- **deps:** updated gradle
- **quarkus:** adding kubernetes, jib extensions
- **readme:** adding version badge

### Ci
- **actions:** adding github action for build docker and deploy to GKE
- **jenkins:** polish Jenkinsfile

### Docs
- **changelog:** updating change log
- **commitizen:** add commitizen setup docs
- **docker:** updated docker build step
- **docker:** adding Docker build instructions
- **gitbook:** updated playbook
- **gitflow:** fix typo
- **gradle:** updated gradle docs for sonarqube spotless and Quarkus

### Feat
- **deploy:** now using google appctl and Kustomize
- **infra:** adding health,metrics,openapi
- **ktlint:** adding ktlint gradle plugin
- **quarkus:** adding quarkus example

### Fix
- **actions:** fix check action comment step
- **actions:** fix check action comment step
- **actions:** fix github actions
- **docs:** add image
- **docs:** updated docs for `git flow feature track`
- **docs:** add image
- **readme:** adding github badge

### Improvement
- **readme:** adding sonarcloud badges
- **structure:** project structure

### Refactor
- **gradle:** reduce duplicate code
- **gradle:** polish gradle
- **gradle:** polish gradle
- **gradle:** refactore gradle tasks
- **shared:** renamed shared module to kbeam

### Style
- **ktlint:** fix ktlint issues

### Test
- **bom:** ability to switch quarkus-universe-bom <=> quarkus-bom (local quarkus build)
- **flogger:** adding unit tests for flogger
- **quarkus:** test with local built Quarkus from master
- **quarkus:** fix: with quarkusPlatformVersion=1.3.1.Final, tests fails


<a name="1.6.0"></a>
## [1.6.0] - 2020-01-30
### Chore
- **gitbook:** fix types
- **gitbook:** adding gitbook config
- **gradle:** jibDockerBuild from local image
- **jenkins:** polish jenkins

### Feat
- **docs:** jenkins pipelines

### Fix
- **docs:** changelog
- **docs:** manually set version with Jenkins params
- **gradle:** fix gradle
- **jenkins:** Jenkins


<a name="1.5.0"></a>
## [1.5.0] - 2020-01-28
### Chore
- **changelog:** adding ahangelog2
- **docs:** adding gitbook2
- **docs:** adding gitbook


<a name="1.4.0"></a>
## [1.4.0] - 2020-01-26
### Feat
- **ci:** test4

### Perf
- **docs:** fix docs


<a name="1.3.2"></a>
## [1.3.2] - 2020-01-26
### Feat
- **ci:** test3


<a name="1.3.1"></a>
## [1.3.1] - 2020-01-26

<a name="1.3.0"></a>
## [1.3.0] - 2020-01-26
### Feat
- **ci:** test2
- **hi:** test

### Fix
- **deps:** test


<a name="1.2.0"></a>
## [1.2.0] - 2020-01-26
### Feat
- **gradle:** adding use-latest-versions plugin6

### Fix
- **deps:** pin dependency2
- **deps:** pin dependency
- **deps:** pin dependency


<a name="1.1.0"></a>
## [1.1.0] - 2020-01-26
### Feat
- **gradle:** adding use-latest-versions plugin5


<a name="0.5.0"></a>
## [0.5.0] - 2020-01-26
### Chore
- **code:** rename java package1

### Feat
- **gradle:** adding use-latest-versions plugin3
- **gradle:** adding use-latest-versions plugin2


<a name="0.4.0"></a>
## [0.4.0] - 2020-01-26
### Chore
- **code:** rename java package

### Feat
- **api:** testing
- **gradle:** adding use-latest-versions plugin

### Fix
- **deps:** update dependency


<a name="0.3.0"></a>
## [0.3.0] - 2020-01-24

<a name="0.2.0"></a>
## [0.2.0] - 2020-01-24

<a name="0.1.1"></a>
## [0.1.1] - 2020-01-24

<a name="0.1.0"></a>
## 0.1.0 - 2020-01-24

[Unreleased]: https://github.com/xmlking/micro-apps/compare/v1.6.4...HEAD
[v1.6.4]: https://github.com/xmlking/micro-apps/compare/v1.6.3...v1.6.4
[v1.6.3]: https://github.com/xmlking/micro-apps/compare/v1.6.2...v1.6.3
[v1.6.2]: https://github.com/xmlking/micro-apps/compare/v1.6.1...v1.6.2
[v1.6.1]: https://github.com/xmlking/micro-apps/compare/v1.6.0...v1.6.1
[v1.6.0]: https://github.com/xmlking/micro-apps/compare/1.5.0...v1.6.0
[1.5.0]: https://github.com/xmlking/micro-apps/compare/1.4.0...1.5.0
[1.4.0]: https://github.com/xmlking/micro-apps/compare/1.3.2...1.4.0
[1.3.2]: https://github.com/xmlking/micro-apps/compare/1.3.1...1.3.2
[1.3.1]: https://github.com/xmlking/micro-apps/compare/1.3.0...1.3.1
[1.3.0]: https://github.com/xmlking/micro-apps/compare/1.2.0...1.3.0
[1.2.0]: https://github.com/xmlking/micro-apps/compare/1.1.0...1.2.0
[1.1.0]: https://github.com/xmlking/micro-apps/compare/0.5.0...1.1.0
[0.5.0]: https://github.com/xmlking/micro-apps/compare/0.4.0...0.5.0
[0.4.0]: https://github.com/xmlking/micro-apps/compare/0.3.0...0.4.0
[0.3.0]: https://github.com/xmlking/micro-apps/compare/0.2.0...0.3.0
[0.2.0]: https://github.com/xmlking/micro-apps/compare/0.1.1...0.2.0
[0.1.1]: https://github.com/xmlking/micro-apps/compare/0.1.0...0.1.1
