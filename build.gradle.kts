// import pl.allegro.tech.build.axion.release.domain.hooks.HooksConfig
import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

val baseDockerImage: String by project
val gradleToolVersion = libs.versions.gradleTool.get()
val ktlintVersion = libs.versions.ktlint.get()

val slf4jVersion = libs.versions.slf4j.get()
val kotlinLoggingVersion = libs.versions.kotlinLogging.get()

val excludedProjects = setOf("services", "pipelines", "libs")
val webappProjects = setOf("webapp")
val grpcProjects = setOf("account", "keying", "linking")
val springProjects = setOf("spring-graphql", "chat", "spring", "entity", "redis")
val quarkusProjects = setOf("greeting", "person")
val pipelineProjects = setOf("classifier", "ingestion", "wordcount")

@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    base
    idea
    `maven-publish`

    alias(libs.plugins.kotlin.jvm)

    // Code Quality
    // Keep your code spotless
    alias(libs.plugins.gradle.spotless)
    // kotlin code coverage
    alias(libs.plugins.kotlin.kover)
    // Software Composition Analysis (SCA) tool
    alias(libs.plugins.gradle.dependencycheck)

    // Keep dependencies up to date
    // gradle dependencyUpdates -Drevision=release
    alias(libs.plugins.gradle.versions)

    // gradle versionCatalogUpdate --interactiv
    alias(libs.plugins.gradle.update)
    // Versioning & Release with git tags
    // gradle currentVersion
    // gradle release
    alias(libs.plugins.gradle.release)

    // Make fat runnable jars
    // gradle shadowJar
    // gradle runShadow
    alias(libs.plugins.gradle.shadow)

    // Affected Module Detector: must only be applied to rootProject
    alias(libs.plugins.gradle.amd)
    // used by subprojects
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.allopen) apply false
    alias(libs.plugins.kotlin.spring) apply false
    alias(libs.plugins.kotlin.jpa) apply false
    alias(libs.plugins.kotlin.noarg) apply false
    alias(libs.plugins.kotlin.lombok) apply false
    alias(libs.plugins.gradle.lombok) apply false
    alias(libs.plugins.gradle.jib)
    alias(libs.plugins.gradle.logging) apply false
    alias(libs.plugins.gradle.protobuf) apply false
    alias(libs.plugins.gradle.quarkus) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.spring.dependencyManagement) apply false
    alias(libs.plugins.spring.aot) apply false
    alias(libs.plugins.gradle.flyway) apply false
    alias(libs.plugins.gradle.native) apply false
}

// rootProject config
// scmVersion should be in the top, as it sets the version
scmVersion {
    useHighestVersion.set(true)

    tag {
        prefix.set("v") // golang and appctl need SemVer tags with `v` prefix
        versionSeparator.set("")
    }
}

// rootProject config
affectedModuleDetector {
    baseDir = "${project.rootDir}"
    pathsAffectingAllModules = setOf("gradle/libs.versions.toml")
    logFilename = "output.log"
    logFolder = "${rootProject.buildDir}/affectedModuleDetector"
    specifiedBranch = "main"
    compareFrom = "SpecifiedBranchCommit" // default is PreviousCommit
}

version = scmVersion.version
val shortRevision: String = scmVersion.scmPosition.shortRevision
val isSnapshot = version.toString().endsWith("-SNAPSHOT")
val isCI = System.getenv("CI").isNullOrBlank().not()
if (!project.hasProperty("release.quiet")) {
    println("Version: $version,  Branch: ${scmVersion.scmPosition.branch}, isCI: $isCI")
}

spotless {
    kotlin {
        ktlint(ktlintVersion)
        // Then whenever Spotless encounters a pair of fmt:off / fmt:on, it will exclude the code between them from formatting
        toggleOffOn("fmt:off", "fmt:on")
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(ktlintVersion)
    }
}

versionCatalogUpdate {
    pin {
        // pins all libraries and plugins using the given versions
        versions.add("my-version-name")
        versions.add("other-version")
    }
    keep {
        keepUnusedVersions.set(true)
    }
}

// Kotlin Code Coverage Reporting
koverMerged {
    enable()
    filters {
        projects {
            excludes += ":libs:proto"
        }
    }

    htmlReport {
        enable()
        reportDir.set(layout.buildDirectory.dir("kover/html"))
    }
    xmlReport {
        enable()
        reportFile.set(layout.buildDirectory.file("kover/coverage.xml"))
    }
}

// dependencyCheck generate SARIF file to publish to GitHub security
dependencyCheck {
    formats = listOf("HTML", "SARIF")
    // suppressionFile = "$projectDir/config/owasp/owasp-supression.xml"
}

// all projects config
allprojects {
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://packages.confluent.io/maven/") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

// sub projects config
subprojects {
    if (this.name !in excludedProjects) {
        version = rootProject.version
        apply {
            plugin(rootProject.project.libs.plugins.kotlin.jvm.get().pluginId)
            plugin("maven-publish")
            plugin(rootProject.project.libs.plugins.gradle.spotless.get().pluginId)
            plugin(rootProject.project.libs.plugins.kotlin.kover.get().pluginId)
            // apply for pipelines
            if (path.startsWith(":pipelines")) {
                plugin("application")
                plugin(rootProject.project.libs.plugins.gradle.shadow.get().pluginId)
            }
            // apply for libs
            if (path.startsWith(":libs")) {
                plugin("java-library")
            }
        }

        group = if (path.startsWith(":libs")) {
            "hc360.libs"
        } else if (path.startsWith(":services")) {
            "hc360.services"
        } else if (path.startsWith(":pipelines")) {
            "hc360.pipelines"
        } else {
            "hc360"
        }

        dependencies {
            // Align versions of all Kotlin components
            implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

            // Use the Kotlin JDK 8 standard library.
            implementation(kotlin("stdlib-jdk8"))
            implementation(kotlin("reflect"))

            // Use kotest for testing
            testImplementation(rootProject.project.libs.bundles.testing.common)

            // Logging with slf4j 2.0.x API.
            // Add your favorite runtimeOnly lib (slf4j-jdk14 or slf4j-simple or logback) in subproject's build.gradle.kts
            // Don't add slf4j runtimeOnly lib to libs' build.gradle.kts
            implementation(rootProject.project.libs.bundles.logging.common)
        }

        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
            withSourcesJar()
            withJavadocJar()
        }

        kotlin {
            jvmToolchain(17)
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
                toggleOffOn("fmt:off", "fmt:on")
                ktlint(ktlintVersion).editorConfigOverride(mapOf("disabled_rules" to "filename"))
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
                    jvmTarget = JavaVersion.VERSION_17.toString()
                    javaParameters = true
                    freeCompilerArgs = listOf(
                        // "-Xjvm-enable-preview",
                        "-Xjsr305=strict",
                        "-opt-in=kotlin.RequiresOptIn",
                        "-opt-in=kotlin.OptIn"
                    )
                }
            }

            compileTestKotlin {
                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_17.toString()
                    javaParameters = true
                    freeCompilerArgs = listOf(
                        // "-Xjvm-enable-preview",
                        "-Xjsr305=strict",
                        "-opt-in=kotlin.RequiresOptIn",
                        "-opt-in=kotlin.OptIn"
                    )
                }
            }

            test {
                // select test framework before configuring options
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
            }

            register<Test>("integrationTest") {
                // select test framework before configuring options
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

            // Reproducible Builds https://reproducible-builds.org/
            withType<AbstractArchiveTask>() {
                isPreserveFileTimestamps = false
                isReproducibleFileOrder = true
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
                        credHelper {
                            helper = "osxkeychain"
                        }
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
                        // creationTime = "USE_CURRENT_TIMESTAMP"
                        creationTime.set("USE_CURRENT_TIMESTAMP")
                        // creationTime = project.provider { project.ext.git['git.commit.time'] }
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

    wrapper {
        distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleToolVersion-bin.zip"
    }

    // Register Custom Task with root project
    register<AffectedTask>("affected") {
        group = "Affected Module Detector"
        description = "print all affected subprojects due to code changes"
    }

//    koverMergedVerify {
//        rule {
//            name = "75% Coverage"
//            bound {
//                minValue = 75
//                // valueType = kotlinx.kover.api.VerificationValueType.COVERED_LINES_PERCENTAGE //  by default
//            }
//        }
//    }
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
