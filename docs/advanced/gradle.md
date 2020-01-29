# Gradle

## Plugins

1. ### Axion plugin

    We are using Gradle's [axion-release](https://axion-release-plugin.readthedocs.io/en/latest/) plugin for  release & version management.
    
    If current commit is tagged commit, project has a release version( i.e., 0.1.0).<br/>
    If there were any commits after last tag, project is in SNAPSHOT version( i.e., 0.1.1-SNAPSHOT). 
    
    > `axion-release` follows the structure of git, only knows about tags on given branch.


## Usage
 
> first time generating a new library project

```bash
gradle init --type kotlin-library --dsl kotlin
```

    Options for all gradle commands: -D --system-prop, -P --project-prop

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
# Build image
gradle jib
# Build image useing your Docker daemon
gradle jibDockerBuild
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

> miscellaneous Gradle commands

```bash
# upgrade gradlew
VERSION=${1:-6.1}
gradle wrapper --gradle-version "${VERSION}"
```

### Reference
- https://github.com/banan1988/spring-demo/
- https://github.com/detekt/sonar-kotlin
- https://android.jlelse.eu/sonarqube-code-coverage-for-kotlin-on-android-with-bitrise-71b2fee0b797