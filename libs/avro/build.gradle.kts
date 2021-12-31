val avroVersion = libs.versions.avro.get()

dependencies {
    implementation(project(":libs:core"))
    implementation("org.apache.avro:avro:$avroVersion")
    // Testing
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }
