
val kafkaStreamsVersion = libs.versions.kafkaStreams.get()
val avro4kSerdeVersion = libs.versions.avro4kSerde.get()
val confluentVersion = libs.versions.confluent.get()
val kotlinRetryVersion = libs.versions.kotlinRetry.get()

dependencies {
    implementation(project(":libs:crypto"))
    implementation("org.apache.kafka:kafka-streams:$kafkaStreamsVersion")
    implementation("com.github.thake.avro4k:avro4k-kafka-serializer:$avro4kSerdeVersion")
    implementation("io.confluent:kafka-streams-avro-serde:$confluentVersion") // ??? should be loaded as transient dep

    // TODO: Remove
    implementation(libs.bundles.kotlinx.serialization.all) // JSON, ProtoBuf, Avro serialization
    implementation("com.michael-bull.kotlin-retry:kotlin-retry:$kotlinRetryVersion")
    implementation(libs.bundles.kotlinx.coroutines)

    // Testing
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }
