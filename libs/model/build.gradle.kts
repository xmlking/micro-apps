plugins {
    kotlin("plugin.serialization")
    // which produces test fixtures
    `java-test-fixtures`
}

dependencies {
    // Use Kotlin Serialization
    implementation(libs.bundles.kotlinx.serialization) // JSON, ProtoBuf, Avro serialization
    // implementation(libs.kotlinx.serialization.yaml) // YAML serialization

    // Testing
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.kotlinx.coroutines.test)
}
