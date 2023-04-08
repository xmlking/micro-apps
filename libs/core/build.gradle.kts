description = "Core Lib module"

// Test
dependencies {
    // Testing
    testImplementation(testFixtures(projects.libs.test))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }
