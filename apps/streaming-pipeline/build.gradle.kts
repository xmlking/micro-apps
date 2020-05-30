plugins {
    kotlin("plugin.serialization")
}

val kotlinVersion: String by project
val beamVersion: String by project
val csvVersion: String by project
val junitVersion: String by project
val hamcrestVersion: String by project
val kotlinSerializationVersion: String by project
val avro4kVersion: String by project
val konfigVersion: String by project
val guavaVersion: String by project
val grpcKotlinVersion: String by project
val kotlinCoroutinesVersion: String by project
val slf4jVersion: String by project
val grpcVersion: String by project

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

    // Use Apache Beam
    implementation("org.apache.beam:beam-sdks-java-core:$beamVersion")
    implementation("org.apache.beam:beam-runners-direct-java:$beamVersion")
    implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java:$beamVersion")
    implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:$beamVersion")

    // Use Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinSerializationVersion") // JSON serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$kotlinSerializationVersion") // ProtoBuf serialization
    implementation("com.sksamuel.avro4k:avro4k-core:$avro4kVersion") // Avro serialization
    // implementation("org.apache.beam:beam-sdks-java-extensions-kryo:$beamVersion") // kryo serialization
    // implementation("org.apache.beam:beam-sdks-java-extensions-euphoria:$beamVersion")

    // Kotlin Config
    implementation("com.uchuhimo:konf-core:$konfigVersion")
    implementation("com.uchuhimo:konf-yaml:$konfigVersion")

    // gRPC deps: for calling API
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinCoroutinesVersion")
    api("com.google.guava:guava:$guavaVersion") // Force `-jre` version instead of `-android`

    // Test
    // Use the Kotlin test library.
    testImplementation(kotlin("test"))
    // Use the Kotlin JUnit integration.
    testImplementation(kotlin("test-junit"))
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testImplementation("com.google.cloud:google-cloud-pubsub:1.106.0")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(testFixtures(project(":libs:model")))
    testImplementation(testFixtures(project(":libs:proto")))
}

// gradle test -Dkotest.tags.include=Beam -Dkotest.tags.exclude=E2E
tasks {
    test {
        systemProperty("kotest.tags.exclude", System.getProperty("E2E"))
    }
}

application {
    // mainClassName = "micro.apps.pipeline.ClassifierPipeline"
    mainClassName = "micro.apps.pipeline.EnricherPipeline"
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs = listOf("-Djava.util.logging.config.file=src/main/resources/logging.properties", "-Dmicro.apps.level=FINE")
}

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}
