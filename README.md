# Java GitOps

An experimental project for testing [Multi-branch Jenkins Pipeline](https://jenkins.io/doc/tutorials/build-a-multibranch-pipeline-project/)

Using [axion-release-plugin](https://axion-release-plugin.readthedocs.io/en/latest/) for Release & Version management 

Using [spotless-changelog](https://github.com/diffplug/spotless-changelog) plugin for generating changelog

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
```

### Changelog

> With every release, run `gradle changelogPush`

```bash
gradle changelogPrint
gradle changelogCheck
gradle changelogBump
# changelogPush depends on changelogBump depends on changelogChecks
gradle changelogPush
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
# Build image
gradle jib
# Build image useing your Docker daemon
gradle jibDockerBuild
```

### Gradle

```bash
# upgrade gradlew
VERSION=${1:-6.1}
gradle wrapper --gradle-version "${VERSION}"
# Report dependencies
gradle dependencyUpdates -Drevision=release
gradle dependencyUpdates -Drevision=release -DoutputFormatter=json,xml
```

### Reference
- https://github.com/banan1988/spring-demo/
- https://github.com/detekt/sonar-kotlin
- https://android.jlelse.eu/sonarqube-code-coverage-for-kotlin-on-android-with-bitrise-71b2fee0b797

### Version
Current 0.2.0 - pushing