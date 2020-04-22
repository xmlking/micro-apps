plugins {
    kotlin("plugin.serialization")
}

val floggerVersion: String by project
val kotlinSerializationVersion: String by project
val avro4kVersion: String by project

dependencies {
    // Use Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinSerializationVersion") // JVM serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$kotlinSerializationVersion") // protobuf serialization
    implementation("com.sksamuel.avro4k:avro4k-core:$avro4kVersion") // avro serialization

    // Testing
    testImplementation("com.google.flogger:flogger-testing:$floggerVersion")
}
