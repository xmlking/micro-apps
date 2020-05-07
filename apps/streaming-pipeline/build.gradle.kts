plugins {
    kotlin("plugin.serialization")
}

val kotlinVersion: String by project
val beamVersion: String by project
val csvVersion: String by project
val hamcrestVersion: String by project
val kotlinSerializationVersion: String by project
val avro4kVersion: String by project
val konfigVersion: String by project
val guavaVersion: String by project
val grpcKotlinVersion: String by project
val kotlinCoroutinesVersion: String by project

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

    // Use the Kotlin test library.
    testImplementation(kotlin("test"))
    // Use the Kotlin JUnit integration.
    testImplementation(kotlin("test-junit"))
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testImplementation("com.google.cloud:google-cloud-pubsub:1.105.1")
}

java {
    // Java 8 needed as Beam doesn't yet support 11
    // FIXME
    // sourceCompatibility = JavaVersion.VERSION_1_8
    // targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    // mainClassName = "micro.apps.pipeline.ClassifierPipeline"
    mainClassName = "micro.apps.pipeline.EnricherPipeline"
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs = listOf("-Dorg.slf4j.simpleLogger.log.micro.apps=debug")
}

// gradle test -DKotest.tags.include=Beam -DKotest.tags.exclude=E2E
tasks {
    test {
        systemProperty("Kotest.tags.exclude", System.getProperty("E2E"))
    }
}
