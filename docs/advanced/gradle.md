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

```shell
gradle init --type kotlin-library --dsl kotlin
```

    Options for all gradle commands: -D or --system-prop, -P or --project-prop, -m or --dry-run

Check Task Dependencies With a Dry Run with dryRun: -m or --dry-run i.e., `gradle publish -m`

### Version

> display computed version

```shell
gradle cV
gradle currentVersion
# to get version # as plain text
export VERSION=$(gradle cV -q -Prelease.quiet)
echo $VERSION
```

### Verification

```shell
gradle test
gradle jacocoTestReport
gradle jacocoTestCoverageVerification
# gotcha: jacoco reports need to be generated before any of the sonar task
gradle check
gradle sonarqube
```

#### Benchmark

Using [kotlinx-benchmark](https://github.com/Kotlin/kotlinx-benchmark)

Benchmark tasks
```shell
gradle assembleBenchmarks - Generate and build all benchmarks in a project
gradle benchmark - Execute all benchmarks in a project
gradle benchmarksBenchmark - Execute benchmark for 'benchmarks'
gradle benchmarksBenchmarkCompile - Compile JMH source files for 'benchmarks'
gradle benchmarksBenchmarkGenerate - Generate JMH source files for 'benchmarks'
gradle benchmarksBenchmarkJar - Build JAR for JMH compiled files for 'benchmarks'
```


#### dependency-check

Software Composition Analysis (SCA) tool that attempts to detect publicly disclosed vulnerabilities contained within a project’s dependencies.

```shell
gradle dependencyCheckAggregate
```

#### kover

[Kover](https://lengrand.fr/kover-code-coverage-plugin-for-kotlin/) is Code Coverage plugin for Kotlin

Note: _Cross-module tests are not supported in reports and validation yet. For each test, only the classpath belonging to the current module is taken._

```shell
gradle koverMergedReport -x test # Generates code coverage report for all enabled test tasks in all projects.
gradle koverMergedVerify # Verifies code coverage metrics of all projects based on specified rules. Always executes before `check` task.
gradle koverReport # Executes both koverXmlReport and koverHtmlReport tasks for one project.
gradle koverVerify # Verifies code coverage metrics of one project based on specified rules.
```

### Spotless tasks

```shell
# spotlessCheck - Checks that sourcecode satisfies formatting steps.
gradle spotlessCheck
# spotlessApply - Applies code formatting steps to sourcecode in-place.
gradle spotlessApply
```

### Quarkus tasks

```shell
# addExtension - Adds Quarkus extensions specified by the user to the project.
gradle :services:greeting:addExtension --extensions="health,metrics,openapi"
gradle :services:greeting:addExtension --extensions="hibernate-validator"
gradle :services:greeting:addExtension --extensions="jdbc,agroal,non-exist-ent"
# buildNative - Building a native image
gradle :services:greeting:buildNative
gradle :services:greeting:testNative
# generateConfig - Generates an example config file
# listExtensions - Lists the available quarkus extensions
gradle :services:greeting:listExtensions
# quarkusBuild - Quarkus builds a runner jar based on the build jar
# quarkusDev - Development mode: enables hot deployment with background compilation
gradle :services:greeting:quarkusDev -Dsuspend -Ddebug
# quarkusTestConfig - Sets the necessary system properties for the Quarkus tests to run.
```

### Flyway

Docs: Gradle [Flyway Plugin](https://github.com/flyway/flyway/blob/main/documentation/Flyway%20CLI%20and%20API/Usage/Gradle%20Task.md)

Ref [Adding dependencies on Flyway Database Types](https://github.com/flyway/flyway/blob/main/documentation/Flyway%20CLI%20and%20API/Usage/Gradle%20Task.md#adding-dependencies-on-flyway-database-types)

For some Flyway database types, like [Cloud Spanner] and [SQL Server], you'll need to add a dependency to the database type in a `buildscript` closure to get your Gradle commands to work properly.
This puts the database type on the build classpath, and not the project classpath.

Here is an example `settings.gradle.kts`:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath "org.flywaydb:flyway-mysql:{{ site.flywayVersion }} "
    }
}
```

Without this you may see an error like the following: `No database found to handle jdbc:...`

### Docker Compose

start/stop dependent services for your app
```shell
# start local redis
gradle redisComposeUp
# stop local redis before restart again
gradle redisComposeDown
# copy container logs to `build/containers-logs`
gradle redisComposeLogs
```

### Affected Module Detector (AMD)

The detector allows for three options for affected modules:

- **Changed Projects:** These are projects which had files changed within them – enabled with `-Paffected_module_detector.changedProjects`)
- **Dependent Projects:** These are projects which are dependent on projects which had changes within them enabled with `-Paffected_module_detector.dependentProjects`)
- **All Affected Projects:** This is the union of Changed Projects and Dependent Projects (this is the default configuration)

```shell
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

```shell
gradle build
gradle build -x test
# overriding version
gradle build -Prelease.forceVersion=3.0.0
```

### Run

```shell
gradle run
```

### Docs

> https://github.com/Kotlin/dokka/blob/master/docs/src/doc/docs/user_guide/gradle/usage.md

```shell
gradle dokkaHtmlMultimodule -Dorg.gradle.jvmargs="-Xmx4096m -XX:MaxMetaspaceSize=512m"
gradle dokkaHtmlMultimodule
gradle dokkaHtml
```

### Release

> bump git Tag and Push

```shell
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

```shell
# first time
git-chglog --init
# on release branch, generate CHANGELOG.md and commit before merging back to develop & main.
git-chglog -c .github/chglog/config.yml -o CHANGELOG.md
git-chglog -c .github/chglog/config.yml -o CHANGELOG.md --next-tag 2.0.0
```

```shell
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

```shell
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

```shell
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

```shell
# pull base image first
docker pull gcr.io/distroless/java:11
# use local docker image as base, build image only (on publish)
gradle jibDockerBuild -PbaseDockerImage=docker://gcr.io/distroless/java:11
# you can run your local docker image
docker run -it xmlking/micro-apps-demo:1.6.5-SNAPSHOT
```

### Dependencies

This use [versions plugin](https://github.com/ben-manes/gradle-versions-plugin) and [version-catalog-update-plugin](https://github.com/littlerobots/version-catalog-update-plugin)

```shell
# Report dependencies
gradle dependencyUpdates -Drevision=release -DoutputFormatter=json,xml
# Update dependencies, `dependsOn dependencyUpdates`
# Creating a libs.versions.toml file, If you don't have a catalog file yet
gradle versionCatalogUpdate --create
# Updating the libs.versions.toml file
gradle versionCatalogUpdate
# Generate delta updates file `libs.versions.updates.toml` that you can apply manually 
gradle versionCatalogUpdate --interactive
```

### Gradle

```shell
# upgrade gradlew
# upgrade project gradle version
gradle wrapper --distribution-type all
# gradle wrapper --gradle-version=8.1
# gradle daemon status 
gradle --status
gradle --stop
# show dependencies
gradle classifier:dependencies
gradle classifier:dependencyInsight --dependency spring-messaging
# refresh dependencies
gradle build -x test --refresh-dependencies 
# viewing and debugging dependencies
gradle -q :services:greeting:dependencyInsight  --dependency org.ow2.asm:asm --configuration testCompileClasspath

# display version 
gradle cV
```

### Maven

Remove a local built dependency from local-maven-repository

```shell
mvn dependency:purge-local-repository -DmanualInclude="com.redis.om:redis-om-spring"
```

### FAQ

- Gradle [version catalogs](https://melix.github.io/blog/2021/03/version-catalogs-faq.html) FAQ

### Reference

- https://github.com/banan1988/spring-demo/
- https://github.com/detekt/sonar-kotlin
- https://android.jlelse.eu/sonarqube-code-coverage-for-kotlin-on-android-with-bitrise-71b2fee0b797
- [Reporting code coverage with JaCoCo Sample](https://docs.gradle.org/6.4.1/samples/sample_jvm_multi_project_with_code_coverage.html)
- [Kotlin kover](https://github.com/Kotlin/kotlinx-kover#apply-plugin-to-single-module-project)
