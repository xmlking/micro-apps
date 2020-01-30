# JVM GitOps

An experimental project for testing [Multi-branch Jenkins Pipeline](https://jenkins.io/doc/tutorials/build-a-multibranch-pipeline-project/)

Using [axion-release-plugin](https://axion-release-plugin.readthedocs.io/en/latest/) for Release & Version management 

Changelog format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

> first time generating a new library project

```bash
gradle init --type kotlin-library --dsl kotlin
```

## Gradle

> miscellaneous Gradle commands

> -D, --system-prop -P, --project-prop

### Version

> display computed version 

```bash
gradle cV
gradle currentVersion
# to get version # as plain text
export VERSION=$(gradle cV -q -Prelease.quiet)
echo $VERSION
```

### Verification
```bash
gradle test
gradle jacocoTestReport
gradle jacocoTestCoverageVerification
gradle sonarqube
gradle check
```

### Build

> build jar

```bash
gradle build
gradle build -x test
# overriding version
gradle build -Prelease.forceVersion=3.0.0
```

### Run
```bash
gradle run
```

### Release

> bump git Tag and Push

```bash
# dryRun
gradle release -Prelease.dryRun
# disable all checks for current release
gradle release -Prelease.disableChecks -Prelease.dryRun
# real release
gradle release -Prelease.customUsername=sxc460 -Prelease.customPassword=
# bump Minor Version : 0.1.0 -> 0.2.0
gradle markNextVersion -Prelease.incrementer=incrementMinor -Prelease.dryRun
# in CI enveroment 
gradle release -Prelease.disableChecks -Prelease.pushTagsOnly -x test --profile
# if you want to manually set version with Jenkins params
gradle createRelease    -Prelease.disableChecks -Prelease.versionIncrementer=incrementMajor     -Prelease.dryRun
gradle markNextVersion  -Prelease.disableChecks -Prelease.incrementer=incrementMajor            -Prelease.dryRun
```

### Changelog

> With every release, run `git-chglog`

```bash
# first time
git-chglog --init
# on release branch, generate CHANGELOG.md and commit before merging back to develop & master.
git-chglog  -o CHANGELOG.md
git-chglog  -o CHANGELOG.md -next-tag 2.0.0
```

### Publish

> publish after release

```bash
gradle publish
# skip tests in CI
gradle publish -x test --profile
gradle build publish -Prelease.forceSnapshot
gradle build publish -Prelease.forceVersion=3.0.0
```

### Docker

```bash
# Build an image tarball,
# then you can load with `docker load --input build/jib-image.tar`
gradle jibBuildTar
# Build and publish docker image
gradle jib
# Build image locally useing your Docker daemon (on publish)
gradle jibDockerBuild
```

#### local testing

pull a remote image and locally use it as base image

```bash
# pull base image first
docker pull gcr.io/distroless/java:11
# use local docker image as base, build image only (on publish)
gradle jibDockerBuild -PbaseDockerImage=docker://gcr.io/distroless/java:11
# you can run your local docker image
docker run -it xmlking/jvm-gitops-jvm-gitops:0.2.0-SNAPSHOT
```

### Dependencies

```bash
# Report dependencies
gradle dependencyUpdates -Drevision=release -DoutputFormatter=json,xml
# Update dependencies, `dependsOn dependencyUpdates`
gradle useLatestVersions
# This task will succeed if all available updates were successfully applied by useLatestVersions
gradle useLatestVersions && gradle useLatestVersionsCheck
```

### Gradle

```bash
# upgrade gradlew
VERSION=${1:-6.1}
gradle wrapper --gradle-version "${VERSION}"
```

### Reference
- https://github.com/banan1988/spring-demo/
- https://github.com/detekt/sonar-kotlin
- https://android.jlelse.eu/sonarqube-code-coverage-for-kotlin-on-android-with-bitrise-71b2fee0b797
