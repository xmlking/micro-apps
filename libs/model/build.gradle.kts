plugins {
    kotlin("plugin.serialization")
}

val kotlinSerializationVersion: String by project
val avro4kVersion: String by project
val kamlVersion: String by project

dependencies {
    // Use Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:$kotlinSerializationVersion") // JSON serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:$kotlinSerializationVersion") // ProtoBuf serialization
    implementation("com.sksamuel.avro4k:avro4k-core:$avro4kVersion") // Avro serialization
    // implementation("com.charleskorn.kaml:kaml:$kamlVersion") // YAML serialization
}
