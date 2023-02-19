plugins {
    kotlin("plugin.serialization")
}

val grpcVersion = libs.versions.grpc.get()
val junitVersion = libs.versions.junit.get()
val slf4jVersion = libs.versions.slf4j.get()

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "io.grpc" && requested.name != "grpc-kotlin-stub") {
            useVersion(grpcVersion)
            because("fix grpc version to latest")
        }
    }
}

dependencies {
    implementation(projects.libs.core)
    implementation(projects.libs.model)
    implementation(projects.libs.kbeam)
    implementation(projects.libs.pipeline)

    // Use Apache Beam
    implementation(libs.bundles.beam)

    // Use Kotlin Serialization
    implementation(libs.bundles.kotlinx.serialization.all) // JSON, ProtoBuf, Avro serialization
    // implementation(libs.beam.sdks.java.extensions.kryo) // kryo serialization
    // implementation(libs.beam.sdks.java.extensions.euphoria)

    // Test with JUnit4 & JUnit5
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation(libs.hamcrest.all.test)
    testImplementation(libs.google.cloud.pubsub)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(testFixtures(projects.libs.test))
    testImplementation(testFixtures(projects.libs.model))
    testImplementation(testFixtures(projects.libs.proto))
    testImplementation(testFixtures(projects.libs.pipeline))
}

affectedTestConfiguration { jvmTestTask = "check" }

// gradle test -Dkotest.tags.include=Beam -Dkotest.tags.exclude=E2E
tasks {
    test {
        systemProperty("kotest.tags.exclude", System.getProperty("E2E"))
    }
}

application {
    mainClass.set("micro.apps.pipeline.ClassifierPipeline")
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs =
        listOf("-Djava.util.logging.config.file=src/main/resources/logging.properties", "-Dmicro.apps.level=FINE")
}
