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
    implementation("com.uchuhimo:konf:$konfigVersion")

    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testImplementation("com.google.cloud:google-cloud-pubsub:1.105.0")
}

java {
    // Java 8 needed as Beam doesn't yet support 11
    // FIXME
    // sourceCompatibility = JavaVersion.VERSION_1_8
    // targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "micro.apps.pipeline.ClassifierPipeline"
    // mainClassName = "micro.apps.pipeline.EnricherPipeline"
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
}
