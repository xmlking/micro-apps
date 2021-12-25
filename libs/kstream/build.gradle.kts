
val kafkaStreamsVersion = libs.versions.kafkaStreams.get()

dependencies {
    implementation("org.apache.kafka:kafka-streams:$kafkaStreamsVersion")
    // Testing
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }
