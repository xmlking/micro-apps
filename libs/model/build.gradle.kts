plugins {
    kotlin("plugin.serialization")
    kotlin("plugin.noarg")
    // which produces test fixtures
    `java-test-fixtures`
}

dependencies {
    // Use Kotlin Serialization
    implementation(libs.bundles.kotlinx.serialization.all) // JSON, ProtoBuf, Avro serialization
    // implementation(libs.kotlinx.serialization.yaml) // YAML serialization

    // for validation annotations
    implementation(libs.javax.validation)

    // Testing
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }

noArg {
    invokeInitializers = true
    annotation("micro.apps.model.NoArg")
}
