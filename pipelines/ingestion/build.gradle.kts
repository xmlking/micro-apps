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
    implementation(project(":libs:core"))
    implementation(project(":libs:model"))
    implementation(project(":libs:kbeam"))
    implementation(project(":libs:pipeline"))

    // Use Apache Beam
    implementation(libs.bundles.beam)

    // Use Kotlin Serialization
    implementation(libs.bundles.kotlinx.serialization.all) // JSON, ProtoBuf, Avro serialization
    // implementation(libs.beam.sdks.java.extensions.kryo) // kryo serialization
    // implementation(libs.beam.sdks.java.extensions.euphoria)
    // implementation(libs.beam.sdks.java.extensions.protobuf)

    // Kotlin Config
    implementation(libs.bundles.konf)

    // gRPC deps: for calling API
    implementation(libs.grpc.kotlin.stub)
    // implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation(libs.bundles.kotlinx.coroutines)
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")

    api(libs.guava) // Force `-jre` version instead of `-android`

    // Test with JUnit4 & JUnit5
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation(libs.hamcrest.all.test)
    testImplementation(libs.google.cloud.pubsub)
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(testFixtures(project(":libs:model")))
    testImplementation(testFixtures(project(":libs:proto")))
    testImplementation(testFixtures(project(":libs:pipeline")))
}

affectedTestConfiguration { jvmTestTask = "check" }

// gradle test -Dkotest.tags.include=Beam -Dkotest.tags.exclude=E2E
tasks {
    test {
        systemProperty("kotest.tags.exclude", System.getProperty("E2E"))
    }
}

application {
    mainClass.set("micro.apps.pipeline.IngestionPipeline")
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs =
        listOf("-Djava.util.logging.config.file=src/main/resources/logging.properties", "-Dmicro.apps.level=FINE")
}
