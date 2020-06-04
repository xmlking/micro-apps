// import pl.allegro.tech.build.axion.release.domain.hooks.HooksConfig
import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig

val kotlinVersion: String by project
val jacocoVersion: String by project
val jacocoQualityGate: String by project
val gcloudProject: String by project
val baseDockerImage: String by project
val ktlintVersion: String by project
val mockkVersion: String by project
val slf4jVersion: String by project
val kotlinLoggingVersion: String by project
val kotestVersion: String by project

val excludedProjects = setOf("apps", "libs")

plugins {
    base
    idea
    jacoco
    `maven-publish`
    // Code Quality
    id("org.sonarqube") version "3.0"
    // Keep your code spotless
    id("com.diffplug.gradle.spotless") version "4.2.1"
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72" apply false
    kotlin("plugin.allopen") version "1.3.72" apply false
    id("org.jetbrains.dokka") version "0.10.1"
    id("com.google.protobuf") version "0.8.12" apply false
    // Keep dependencies up to date
    // gradle dependencyUpdates -Drevision=release
    id("com.github.ben-manes.versions") version "0.28.0"
    // keep your changelog spotless
    // gradle changelogPrint // gradle changelogBump
    id("com.diffplug.spotless-changelog") version "1.1.0"
    // gradle useLatestVersions
    id("se.patrikerdes.use-latest-versions") version "0.2.14"

    // Versioning & Release with git tags
    // gradle currentVersion
    // gradle release
    id("pl.allegro.tech.build.axion-release") version "1.12.0"

    // Make fat runnable jars
    // gradle shadowJar
    // gradle runShadow
    id("com.github.johnrengelman.shadow") version "5.2.0"
    // Build & Publish docker images
    // gradle jib
    id("com.google.cloud.tools.jib") version "2.3.0"
    // detect slf4j conflicts and configure desired backend
    id("dev.jacomet.logging-capabilities") version "0.9.0"
}

// rootProject config
// scmVersion should be in the top, as it sets the version
scmVersion {
    useHighestVersion = true

    tag(closureOf<TagNameSerializationConfig> {
        prefix = "v" // golang and appctl need SemVer tags with `v` prefix
        versionSeparator = ""
    })

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

// all projects config
allprojects {
    repositories {
        jcenter()
        google()
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") } // TODO: remove
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
            plugin("com.diffplug.gradle.spotless")
            // plugin("dev.jacomet.logging-capabilities")
            if (name != "greeting-service") {
                plugin("dev.jacomet.logging-capabilities")
            }
            // exclude for root `apps` and `greeting-service` projects
            if (path.startsWith(":apps") && (name != "greeting-service")) {
                plugin("application")
                plugin("com.github.johnrengelman.shadow")
                plugin("com.google.cloud.tools.jib")
            }
            if (path.startsWith(":libs")) {
                plugin("java-library")
            }
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
            testImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion") // for kotest framework
            testImplementation("io.kotest:kotest-runner-console-jvm:$kotestVersion") // for kotest framework
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

        java {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
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
            }
            kotlin {
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
                    jvmTarget = JavaVersion.VERSION_11.toString()
                    javaParameters = true
                    freeCompilerArgs = listOf("-Xjsr305=strict")
                }
                dependsOn("spotlessCheck")
            }
            compileTestKotlin {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_11.toString()
                    javaParameters = true
                    freeCompilerArgs = listOf("-Xjsr305=strict")
                }
            }

            jacocoTestReport {
                reports {
                    html.isEnabled = true
                    xml.isEnabled = true
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

            check {
                dependsOn("jacocoTestCoverageVerification")
                dependsOn("jacocoTestReport")
            }

            test {
                useJUnitPlatform()
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

            // KDoc
            dokka {
                //  we need to do this, due to corp proxy
                configuration {
                    externalDocumentationLink {
                        noJdkLink = true
                        noStdlibLink = true
                        noAndroidSdkLink = true
                        // any url you want, doesn't matter
                        url = URL("https://whatever")
                        packageListUrl = URL("file:///$rootDir/package-list")
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
                        labels = mapOf("version" to "${project.version}", "name" to project.name, "group" to "${project.group}")
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
                            username = System.getenv("GITHUB_USER")
                            password = System.getenv("GITHUB_TOKEN")
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

    wrapper {
        gradleVersion = "6.5"
        distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
    }
}
