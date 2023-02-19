val avroVersion = libs.versions.avro.get()

dependencies {
    implementation(projects.libs.core)
    implementation("org.apache.avro:avro:$avroVersion")
    // Testing
    testImplementation(testFixtures(projects.libs.test))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }
