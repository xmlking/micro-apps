// import pl.allegro.tech.build.axion.release.domain.hooks.HooksConfig
import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

val jacocoQualityGate: String by project
val baseDockerImage: String by project

val gradleToolVersion = libs.versions.gradleTool.get()
val kotlinVersion = libs.versions.kotlin.get()
val jacocoVersion = libs.versions.jacoco.get()
val ktlintVersion = libs.versions.ktlint.get()
val mockkVersion = libs.versions.mockk.get()
val slf4jVersion = libs.versions.slf4j.get()
val kotlinLoggingVersion = libs.versions.kotlinLogging.get()
val kotestVersion = libs.versions.kotest.get()

val excludedProjects = setOf("apps", "libs")
val springProjects = setOf("chat-service", "entity-service")
val grpcProjects = setOf("account-service", "keying-service", "linking-service")
val quarkusProjects = setOf("greeting-service", "person-service")
val pipelineProjects = setOf("classifier-pipeline", "ingestion-pipeline", "wordcount-pipeline")

plugins {
    base
    idea
    jacoco
    `maven-publish`
    // Code Quality
    id("org.sonarqube")
    // Keep your code spotless
    id("com.diffplug.spotless")
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm")
    // gradle dokkaHtmlMultimodule
    id("org.jetbrains.dokka")
    // Keep dependencies up to date
    // gradle dependencyUpdates -Drevision=release
    id("com.github.ben-manes.versions")
    // keep your changelog spotless
    // gradle changelogPrint // gradle changelogBump
    id("com.diffplug.spotless-changelog")
    // gradle useLatestVersions
    id("se.patrikerdes.use-latest-versions")
    // Versioning & Release with git tags
    // gradle currentVersion
    // gradle release
    id("pl.allegro.tech.build.axion-release")
    // Make fat runnable jars
    // gradle shadowJar
    // gradle runShadow
    id("com.github.johnrengelman.shadow")
    // Build & Publish docker images
    // gradle jib
    id("com.google.cloud.tools.jib")
    // detect slf4j conflicts and configure desired backend
    id("dev.jacomet.logging-capabilities")
    // Affected Module Detector: must only be apply to rootProject
    id("com.dropbox.affectedmoduledetector")
    // TODO: https://kotlinlang.org/docs/lombok.html
    // kotlin("plugin.lombok")
    id("com.avast.gradle.docker-compose")
}

// rootProject config
// scmVersion should be in the top, as it sets the version
scmVersion {
    useHighestVersion = true

    tag(
        closureOf<TagNameSerializationConfig> {
            prefix = "v" // golang and appctl need SemVer tags with `v` prefix
            versionSeparator = ""
        }
    )

    branchVersionIncrementer = mapOf(
        "feature/.*" to "incrementMinor",
        "hotfix/.*" to "incrementPatch",
        "release/.*" to "incrementPrerelease",
        "develop" to "incrementPatch",
        "master" to "incrementMinor"
    )
    // hooks(closureOf<HooksConfig> {
    //     pre("fileUpdate", mapOf(
    //             "file" to "README.md",
    //             "pattern" to "{v,p -> /('$'v)/}",
    //             "replacement" to """{v, p -> "'$'v"}]))"""))
    //     pre("commit")
    // })
}

// rootProject config
affectedModuleDetector {
    baseDir = "${project.rootDir}"
    pathsAffectingAllModules = setOf("gradle/libs.versions.toml")
    logFilename = "output.log"
    logFolder = "${rootProject.buildDir}/affectedModuleDetector"
    specifiedBranch = "develop"
    compareFrom = "SpecifiedBranchCommit" // default is PreviousCommit
}

version = scmVersion.version
val shortRevision = scmVersion.scmPosition.shortRevision
val isSnapshot = version.toString().endsWith("-SNAPSHOT")
val isCI = System.getenv("CI").isNullOrBlank().not()
if (!project.hasProperty("release.quiet")) {
    println("Version: $version,  Branch: ${scmVersion.scmPosition.branch}, isCI: $isCI")
}

spotlessChangelog {
    changelogFile("CHANGELOG.md")
    // enforceCheck true
    setAppendDashSnapshotUnless_dashPrelease(true)
    ifFoundBumpBreaking("**BREAKING**")
    ifFoundBumpAdded("### Added", "### Feat")
    tagPrefix("v")
    commitMessage("Release v{{version}}")
    remote("origin")
    branch("release")
}

println("SpotlessChangelog Version Next: ${spotlessChangelog.versionNext}  Last: ${spotlessChangelog.versionLast}")

spotless {
    kotlin {
        ktlint(ktlintVersion)
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(ktlintVersion)
    }
}

sonarqube {
    properties {
        property("sonar.java.codeCoveragePlugin", "jacoco")
        property("sonar.dynamicAnalysis", "reuseReports")
        property("sonar.exclusions", "**/*Generated.java")
    }
    tasks.sonarqube {
        // gotcha: jacoco reports need to be generated before `sonarqube` task
        // dependsOn("jacocoTestReport")
        subprojects.filter { it.name !in excludedProjects }.forEach {

            dependsOn(":${it.path}:check")
        }
    }
}

// HINT: add this like to all subprojects that depends on dockerCompose
// tasks.named("integrationTest") { dependsOn(rootProject.tasks.named("redisComposeUp")) }
dockerCompose {
    nested("redis").apply {
        useComposeFiles = listOf("infra/redis.yml")
    }
    nested("dgraph").apply {
        useComposeFiles = listOf("infra/dgraph.yml")
    }
}

// all projects config
allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") } // TODO: remove
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://packages.confluent.io/maven/") }
    }
}

// sub projects config
subprojects {
    if (this.name !in excludedProjects) {

        version = rootProject.version
        apply {
            plugin("org.jetbrains.kotlin.jvm")
            plugin("jacoco")
            plugin("org.sonarqube")
            // TODO replace "maven-publish" with  id("com.jfrog.artifactory") version "4.10.0"
            // Adds "build information" when uploading to Artifactory
            plugin("maven-publish")
            plugin("org.jetbrains.dokka")
            plugin("com.diffplug.spotless")
            // plugin("dev.jacomet.logging-capabilities")
            if (name !in quarkusProjects) {
                plugin("dev.jacomet.logging-capabilities")
            }
            // apply for `grpcProjects` & `pipelineProjects` projects under `apps`
            if (path.startsWith(":apps") && (name in grpcProjects + pipelineProjects)) {
                plugin("application")
                plugin("com.github.johnrengelman.shadow")
                plugin("com.google.cloud.tools.jib")
            }
            // apply for libs
            if (path.startsWith(":libs")) {
                plugin("java-library")
            }
        }

        if (path.startsWith(":libs")) {
            group = "micro.libs" // else default. i.e., "micro.apps"
        }

        // do we need this?
        configurations {
            register("bom")
            implementation {
                resolutionStrategy.failOnVersionConflict()
            }
        }

        dependencies {
            // Align versions of all Kotlin components
            implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

            // Use the Kotlin JDK 8 standard library.
            implementation(kotlin("stdlib-jdk8"))
            implementation(kotlin("reflect"))

            // Use kotest for testing
            testImplementation("io.kotest:kotest-framework-engine-jvm:$kotestVersion") // for kotest framework
            testImplementation("io.kotest:kotest-framework-api-jvm:$kotestVersion") // for kotest framework
            testImplementation("io.kotest:kotest-assertions-core-jvm:$kotestVersion") // for kotest core jvm assertions
            testImplementation("io.kotest:kotest-property-jvm:$kotestVersion") // for kotest property test
            testImplementation("io.mockk:mockk:$mockkVersion") // Use Mockk mocking library

            // Logging with slf4jVersion=2.0.0-alpha1
            implementation("org.slf4j:slf4j-api:$slf4jVersion")
            implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
            runtimeOnly("org.slf4j:slf4j-jdk14:$slf4jVersion")
            runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
        }

        // enforce `slf4j-simple` for all sub-projects.
        // Dataflow projects can overwrite it with `slf4j-jdk14` in project specific build.gradle.kts file
        plugins.withId("dev.jacomet.logging-capabilities") {
            loggingCapabilities {
                selectSlf4JBinding("org.slf4j:slf4j-simple:$slf4jVersion")
            }
        }

        // FIXME: specify which task to run per subproject
        // affectedTestConfiguration { jvmTestTask = "check" }

        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(11))
            }
            withSourcesJar()
            withJavadocJar()
        }

        jacoco {
            toolVersion = jacocoVersion
        }

        // For every submodule we set paths
        sonarqube {
            properties {
                property("sonar.junit.reportPaths", "$buildDir/test-results/test")
                property("sonar.java.binaries", "$buildDir/classes/java, $buildDir/classes/kotlin")
                property("sonar.coverage.jacoco.xmlReportPaths", "$buildDir/reports/jacoco/test/jacocoTestReport.xml")
            }
        }

        spotless {
            java {
                removeUnusedImports()
                trimTrailingWhitespace()
                endWithNewline()
                targetExclude("**/build/**")
            }
            kotlin {
                targetExclude("**/build/**")
                ktlint(ktlintVersion)
            }
            kotlinGradle {
                target("*.gradle.kts")
                ktlint(ktlintVersion)
            }
        }

        tasks {
            compileKotlin {
                kotlinOptions {
                    // TODO: Ultimately we need allWarningsAsErrors = true
                    // allWarningsAsErrors = true // Treat all Kotlin warnings as errors
                    jvmTarget = JavaVersion.VERSION_11.toString()
                    // languageVersion = "1.6"
                    // apiVersion = "1.6"
                    javaParameters = true
                    freeCompilerArgs = listOf(
                        // "-Xjvm-enable-preview",
                        "-Xjsr305=strict",
                        "-Xopt-in=kotlin.RequiresOptIn",
                        "-Xopt-in=kotlin.OptIn"
                    )
                }
                // dependsOn("spotlessCheck") // TODO: Circular dependency for generateTestAot
            }
            compileTestKotlin {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_11.toString()
                    // languageVersion = "1.6"
                    // apiVersion = "1.6"
                    javaParameters = true
                    freeCompilerArgs = listOf(
                        // "-Xjvm-enable-preview",
                        "-Xjsr305=strict",
                        "-Xopt-in=kotlin.RequiresOptIn",
                        "-Xopt-in=kotlin.OptIn"
                    )
                }
            }

            jacocoTestReport {
                reports {
                    html.required.set(true)
                    xml.required.set(true)
                }
            }

            jacocoTestCoverageVerification {
                violationRules {
                    rule { limit { minimum = jacocoQualityGate.toBigDecimal() } }
                    rule {
                        enabled = false
                        element = "CLASS"
                        includes = listOf("micro.apps.proto.*")
                    }
                }
            }

            test {
                useJUnitPlatform {
                    excludeTags("slow", "integration")
                }
                filter {
                    isFailOnNoMatchingTests = false
                }
                // maxParallelForks = Runtime.getRuntime().availableProcessors() // FIXME: port conflict for quarkus
                testLogging {
                    exceptionFormat = FULL
                    showExceptions = true
                    showStandardStreams = true
                    events(PASSED, FAILED, SKIPPED, STANDARD_OUT, STANDARD_ERROR)
                }
                finalizedBy("jacocoTestReport")
            }

            register<Test>("integrationTest") {
                useJUnitPlatform {
                    includeTags("integration", "e2e")
                }
                filter {
                    isFailOnNoMatchingTests = false
                }
                testLogging {
                    exceptionFormat = FULL
                    showExceptions = true
                    showStandardStreams = true
                    events(PASSED, FAILED, SKIPPED, STANDARD_OUT, STANDARD_ERROR)
                }
                shouldRunAfter(test)
                finalizedBy("jacocoTestReport")
            }

            check {
                dependsOn("jacocoTestCoverageVerification")
                dependsOn("jacocoTestReport")
                // dependsOn(integrationTest)
            }

            dokkaHtml {
                dokkaSourceSets {
                    /* configure main source set */
                    // named("main") {}

                    /* configure all source sets */
                    configureEach {
                        includes.from("README.md")
                        /* we need to do this, due to corp proxy  */
                        externalDocumentationLink {
                            noJdkLink.set(true)
                            noStdlibLink.set(true)
                            noAndroidSdkLink.set(true)
                            // any url you want, doesn't matter
                            url.set(URL("https://whatever"))
                            packageListUrl.set(URL("file:///$rootDir/package-list"))
                        }
                    }
                }
            }

            jar {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                sdf.timeZone = TimeZone.getTimeZone("UTC")

                manifest.attributes.apply {
                    put("Build-By", System.getProperty("user.name"))
                    put("Build-Date", sdf.format(Date()))
                    put("Build-JDK", org.gradle.internal.jvm.Jvm.current())
                    put("Build-Revision", shortRevision)
                    put("Specification-Title", project.name)
                    put("Specification-Version", project.version)
                    put("Specification-Vendor", project.group)
                    put("Implementation-Title", project.name)
                    put("Implementation-Version", project.version)
                    put("Implementation-Vendor", project.group)
                }
            }
            plugins.withId("com.github.johnrengelman.shadow") {
                shadowJar {
                    isZip64 = true
                    mergeServiceFiles()
                }
            }

            plugins.withId("com.google.cloud.tools.jib") {
                jib {
                    setAllowInsecureRegistries(true)
                    from {
                        if (project.hasProperty("baseDockerImage")) {
                            image = baseDockerImage
                        }
                    }
                    to {
                        image = "xmlking/${rootProject.name}-${project.name}:${project.version}"
                        // image = "us.gcr.io/${gcloudProject}/${rootProject.name}/${project.name}:${project.version}"

                        /**
                         gcr: Google Container Registry (GCR)
                         osxkeychain: Docker Hub
                         */
                        credHelper = "osxkeychain"
                        /**
                         auth {
                         username = "*******"
                         password = "*******"
                         }
                         */
                        tags = setOf("${project.version}")
                    }
                    container {
                        jvmFlags = listOf("-Djava.security.egd=file:/dev/./urandom", "-Xms512m", "-server")
                        creationTime = "USE_CURRENT_TIMESTAMP"
                        ports = listOf("8080", "8443")
                        labels.putAll(
                            mapOf(
                                "version" to "${project.version}",
                                "name" to project.name,
                                "group" to "${project.group}"
                            )
                        )
                        format = ImageFormat.OCI
                    }
                }
            }
        }

        // `publishing` has to be last in `subprojects` as it depends on sourcesJar, javadocJar defs
        publishing {
            publications {
                create<MavenPublication>("micro-apps") {
                    from(components["java"])
                    plugins.withId("com.github.johnrengelman.shadow") {
                        artifact(tasks["shadowJar"])
                    }
                }
            }
            repositories {
                if (isCI) {
                    maven {
                        name = "GitHubPackages"
                        val releasesRepoUrl = "https://maven.pkg.github.com/xmlking/micro-apps"
                        val snapshotsRepoUrl = "https://maven.pkg.github.com/xmlking/micro-apps"
                        url = if (isSnapshot) uri(snapshotsRepoUrl) else uri(releasesRepoUrl)
                        credentials {
                            username = findProperty("nexus.username") as String? ?: System.getenv("GITHUB_USER")
                            password = findProperty("nexus.password") as String? ?: System.getenv("GITHUB_TOKEN")
                        }
                    }
                } else {
                    maven {
                        val releasesRepoUrl = "$buildDir/repos/releases"
                        val snapshotsRepoUrl = "$buildDir/repos/snapshots"
                        url = if (isSnapshot) uri(snapshotsRepoUrl) else uri(releasesRepoUrl)
                    }
                }
            }
        }
    }
}

// rootProject tasks
tasks {
    fun isNonStable(version: String): Boolean {
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        return isStable.not()
    }

    dependencyUpdates {
        rejectVersionIf {
            isNonStable(candidate.version)
        }

        // optional parameters
        outputDir = "$buildDir/dependencyUpdates"
        checkForGradleUpdate = true
        revision = "release"
        gradleReleaseChannel = "current"
    }

    dokkaHtmlMultiModule {
        // outputDirectory.set(buildDir.resolve("dokka"))
        // documentationFileName.set("README.md")
    }

    wrapper {
        distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleToolVersion-bin.zip"
    }

    // Register Custom Task with root project
    register<AffectedTask>("affected") {
        group = "Affected Module Detector"
        description = "print all affected subprojects due to code changes"
    }
}

// Define Custom Task
open class AffectedTask : DefaultTask() {
    @TaskAction
    fun printAffected() {
        project.subprojects.forEach {
            println("Is ${it.name} Affected? : " + com.dropbox.affectedmoduledetector.AffectedModuleDetector.isProjectAffected(it))
        }
    }
}
