// Test
dependencies {
    // Testing
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }
