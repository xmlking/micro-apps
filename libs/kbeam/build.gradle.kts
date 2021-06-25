val junitVersion = libs.versions.junit.get()

dependencies {
    // Use Apache Beam
    implementation(libs.beam.sdks.java.core)
    implementation(libs.beam.runners.direct.java)
    implementation(libs.beam.sdks.java.io.google.cloud.platform)

    // Test with JUnit4 & JUnit5
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation(libs.hamcrest.all.test)
    testImplementation(libs.beam.sdks.java.extensions.json.jackson)
}

affectedTestConfiguration { jvmTestTask = "check" }
