
val kafkaStreamsVersion = libs.versions.kafkaStreams.get()
val avro4kSerdeVersion = libs.versions.avro4kSerde.get()
val confluentVersion = libs.versions.confluent.get()

dependencies {
    implementation("org.apache.kafka:kafka-streams:$kafkaStreamsVersion")
    implementation("com.github.thake.avro4k:avro4k-kafka-serializer:$avro4kSerdeVersion")
    implementation("io.confluent:kafka-streams-avro-serde:$confluentVersion") // ??? should be loaded as transient dep

    // Testing
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }
