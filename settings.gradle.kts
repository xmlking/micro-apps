pluginManagement {
    repositories {
        // maven { url = uri("https://mycompany.nexus/") }
        maven { url = uri("https://repo.spring.io/release") }
        maven { url = uri("https://repo.spring.io/milestone") }
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
    }

    // FIXME: remove after: https://github.com/diffplug/spotless/issues/587
    buildscript {
        repositories {
            mavenLocal()
            mavenCentral()
        }
        dependencies {
//            classpath("org.eclipse.jgit:org.eclipse.jgit:5.13.1.202206130422-r")
            classpath("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")
            // For mssql, flyway gradle plugin need driver in `settings.gradle.kts` buildscript
            classpath("org.flywaydb:flyway-sqlserver:9.17.0")
        }
        configurations.classpath {
            resolutionStrategy {
//                force("org.eclipse.jgit:org.eclipse.jgit:5.13.1.202206130422-r")
                force("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")
            }
        }
    }

    enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
    // enableFeaturePreview("GRADLE_METADATA")
}

rootProject.name = "micro-apps"
include(
    ":services:account",
    ":services:keying",
    ":services:linking",
//    ":services:greeting",
//    ":services:person",
    ":services:chat",
//    ":services:entity",
//    ":services:redis",
//    ":services:webapp",
    ":services:streams",
    ":services:wordcount",
    ":services:spring-demo",
    ":services:spring-graphql-jpa",
    ":services:spring-graphql-r2dbc",
    ":services:spring-graphql-redis",
    ":services:spring-kafka-functions",

    ":pipelines:classifier",
    ":pipelines:ingestion",
    ":pipelines:wordcount",

    ":libs:core",
    ":libs:test",
    ":libs:proto",
    ":libs:kbeam",
    ":libs:spring",
    ":libs:graphql",
    ":libs:kstream",
    ":libs:model",
    ":libs:grpc",
    ":libs:pipeline",
    ":libs:crypto",
    ":libs:avro"
)
