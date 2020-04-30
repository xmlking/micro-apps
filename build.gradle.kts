// import pl.allegro.tech.build.axion.release.domain.hooks.HooksConfig
import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import org.sonarqube.gradle.SonarQubeTask
import pl.allegro.tech.build.axion.release.domain.TagNameSerializationConfig

val kotlinVersion: String by project
val floggerVersion: String by project
val jacocoVersion: String by project
val jacocoQualityGate: String by project
val gcloudProject: String by project
val baseDockerImage: String by project
val ktlintVersion: String by project
val mockkVersion: String by project
val slf4jVersion: String by project
val kotlinLoggingVersion: String by project

val excludedProjects = setOf("apps", "libs")

plugins {
    base
    idea
    jacoco
    `maven-publish`
    // Code Quality
    id("org.sonarqube") version "2.8"
    // Keep your code spotless
    id("com.diffplug.gradle.spotless") version "3.28.1"
    // Apply the Kotlin JVM plugin to add support for Kotlin.
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.serialization") version "1.3.72" apply false
    kotlin("plugin.allopen") version "1.3.72" apply false
    id("org.jetbrains.dokka") version "0.10.1"
    id("com.google.protobuf") version "0.8.12" apply false
    id("io.quarkus") version "1.4.1.Final" apply false
    // Keep dependencies up to date
    // gradle dependencyUpdates -Drevision=release
    id("com.github.ben-manes.versions") version "0.28.0"
    // gradle useLatestVersions
    id("se.patrikerdes.use-latest-versions") version "0.2.13"

    // Versioning & Release with git tags
    // gradle currentVersion
    // gradle release
    id("pl.allegro.tech.build.axion-release") version "1.11.0"

    // Make fat runnable jars
    // gradle shadowJar
    // gradle runShadow
    id("com.github.johnrengelman.shadow") version "5.2.0"
    // Build & Publish docker images
    // gradle jib
    id("com.google.cloud.tools.jib") version "2.2.0"
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

sonarqube {
    properties {
        property("sonar.java.codeCoveragePlugin", "jacoco")
        property("sonar.dynamicAnalysis", "reuseReports")
        property("sonar.exclusions", "**/*Generated.java")
    }
    tasks.sonarqube {
        dependsOn("jacocoTestReport")
    }
}

spotless {
    kotlin {
        ktlint(ktlintVersion)
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(ktlintVersion)
    }
}

// all projects config
allprojects {
    repositories {
        jcenter()
        google()
        mavenLocal()
        mavenCentral()
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
            plugin("maven-publish")
            plugin("org.jetbrains.dokka")
            plugin("com.diffplug.gradle.spotless")
            // exclude for root `apps` and `greeting-quarkus` projects
            if (path.startsWith(":apps") && (name != "greeting-quarkus")) {
                plugin("application")
                plugin("com.github.johnrengelman.shadow")
                plugin("com.google.cloud.tools.jib")
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

            // Use the Kotlin test library.
            testImplementation(kotlin("test"))

            // Use the Kotlin JUnit integration.
            testImplementation(kotlin("test-junit"))
            // Use Mockk mocking library
            testImplementation("io.mockk:mockk:$mockkVersion")

            // Logging
            implementation("com.google.flogger:flogger:$floggerVersion") // TODO remove
            runtimeOnly("com.google.flogger:flogger-system-backend:$floggerVersion")
            implementation("io.github.microutils:kotlin-logging:$kotlinLoggingVersion")
            runtimeOnly("org.slf4j:slf4j-simple:$slf4jVersion")
        }

        java {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
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
                }
            }

            check {
                dependsOn("jacocoTestCoverageVerification")
                dependsOn("jacocoTestReport")
            }

            withType<SonarQubeTask> {
                group = "Verification"
                dependsOn("check")
            }

            test {
                // maxParallelForks = Runtime.getRuntime().availableProcessors() // FIXME: port conflict for quarkus
                testLogging {
                    showExceptions = true
                    showStandardStreams = true
                    // events(PASSED, SKIPPED, FAILED)
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

            val sourcesJar by creating(Jar::class) {
                dependsOn(JavaPlugin.CLASSES_TASK_NAME)
                archiveClassifier.set("sources")
                from(sourceSets.main.get().allSource)
            }
            val javadocJar by creating(Jar::class) {
                dependsOn(JavaPlugin.JAVADOC_TASK_NAME)
                group = JavaBasePlugin.DOCUMENTATION_GROUP
                archiveClassifier.set("javadoc")
                from(javadoc)
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

            artifacts {
                archives(sourcesJar)
                archives(javadocJar)
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
                    from(components["kotlin"])
                    artifact(tasks["sourcesJar"])
                    artifact(tasks["javadocJar"])
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
        gradleVersion = "6.3"
        distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-bin.zip"
    }
}
