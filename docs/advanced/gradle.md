# Gradle

## Plugins

1. ### Axion plugin

   We are using Gradle's [axion-release](https://axion-release-plugin.readthedocs.io/en/latest/) plugin for release &
   version management.

   If current commit is tagged commit, project has a release version( i.e., 0.1.0).<br/>
   If there were any commits after last tag, project is in SNAPSHOT version( i.e., 0.1.1-SNAPSHOT).

   > `axion-release` follows the structure of git, only knows about tags on given branch.

## Usage

> first time generating a new library project

```bash
gradle init --type kotlin-library --dsl kotlin
```

    Options for all gradle commands: -D or --system-prop, -P or --project-prop, -m or --dry-run

Check Task Dependencies With a Dry Run with dryRun: -m or --dry-run i.e., `gradle publish -m`

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
# gotcha: jacoco reports need to be generated before any of the sonar task
gradle check
gradle sonarqube
```

### Spotless tasks

```bash
# spotlessCheck - Checks that sourcecode satisfies formatting steps.
gradle spotlessCheck
# spotlessApply - Applies code formatting steps to sourcecode in-place.
gradle spotlessApply
```

### Quarkus tasks

```bash
# addExtension - Adds Quarkus extensions specified by the user to the project.
gradle :apps:greeting-service:addExtension --extensions="health,metrics,openapi"
gradle :apps:greeting-service:addExtension --extensions="hibernate-validator"
gradle :apps:greeting-service:addExtension --extensions="jdbc,agroal,non-exist-ent"
# buildNative - Building a native image
gradle :apps:greeting-service:buildNative
gradle :apps:greeting-service:testNative
# generateConfig - Generates an example config file
# listExtensions - Lists the available quarkus extensions
gradle :apps:greeting-service:listExtensions
# quarkusBuild - Quarkus builds a runner jar based on the build jar
# quarkusDev - Development mode: enables hot deployment with background compilation
gradle :apps:greeting-service:quarkusDev -Dsuspend -Ddebug
# quarkusTestConfig - Sets the necessary system properties for the Quarkus tests to run.
```

### Affected Module Detector

The detector allows for three options for affected modules:

- **Changed Projects:** These are projects which had files changed within them – enabled with `-Paffected_module_detector.changedProjects`)
- **Dependent Projects:** These are projects which are dependent on projects which had changes within them enabled with `-Paffected_module_detector.dependentProjects`)
- **All Affected Projects:** This is the union of Changed Projects and Dependent Projects (this is the default configuration)

```bash
# Running All Affected Projects (Changed Projects + Dependent Projects)
gradle runAffectedUnitTests -Paffected_module_detector.enable
# Running All Changed Projects
gradle runAffectedUnitTests -Paffected_module_detector.enable -Paffected_module_detector.changedProjects
# Running All Dependent Projects
gradle runAffectedUnitTests -Paffected_module_detector.enable -Paffected_module_detector.dependentProjects
 
 # runs connected tests
gradle runAffectedAndroidTests -Paffected_module_detector.enable
# assembles but does not run on device tests, useful when working with device labs
gradle assembleAffectedAndroidTests -Paffected_module_detector.enable 
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

### Docs

> https://github.com/Kotlin/dokka/blob/master/docs/src/doc/docs/user_guide/gradle/usage.md

```bash
gradle dokkaHtmlMultimodule -Dorg.gradle.jvmargs="-Xmx4096m -XX:MaxMetaspaceSize=512m"
gradle dokkaHtmlMultimodule
gradle dokkaHtml
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
git-chglog -c .github/chglog/config.yml -o CHANGELOG.md
git-chglog -c .github/chglog/config.yml -o CHANGELOG.md --next-tag 2.0.0
```

```bash
# prints the last published version and calculated next version
gradle changelogPrint
# throws an error if the changelog is not formatted according to your rules
gradle changelogCheck

## Update the changelog, commit, push

# updates the changelog on disk with the next version and the current UTC date
gradle changelogBump -Prelease=true
# commits the changelog, tags, and pushes
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
# set `CI=true` to publish from laptop
CI=true gradle publish -Prelease.forceSnapshot
CI=true GITHUB_USER=xmlking GITHUB_TOKEN=<GITHUB_PACKAGES_TOKEN> gradle publish
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
docker run -it xmlking/micro-apps-demo:1.6.5-SNAPSHOT
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
# upgrade project gradle version
gradle wrapper --distribution-type all
# gradle wrapper --gradle-version=7.0
# gradle daemon status 
gradle --status
gradle --stop
# show dependencies
gradle classifier:dependencies
gradle classifier:dependencyInsight --dependency spring-messaging
# refresh dependencies
gradle build -x test --refresh-dependencies 
# viewing and debugging dependencies
gradle -q :apps:greeting-service:dependencyInsight  --dependency org.ow2.asm:asm --configuration testCompileClasspath

# display version 
gradle cV
```

### Reference

- https://github.com/banan1988/spring-demo/
- https://github.com/detekt/sonar-kotlin
- https://android.jlelse.eu/sonarqube-code-coverage-for-kotlin-on-android-with-bitrise-71b2fee0b797
- [Reporting code coverage with JaCoCo Sample](https://docs.gradle.org/6.4.1/samples/sample_jvm_multi_project_with_code_coverage.html)
