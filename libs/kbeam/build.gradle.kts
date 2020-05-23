val beamVersion: String by project
val junitVersion: String by project
val hamcrestVersion: String by project

dependencies {
    // Use Apache Beam
    implementation("org.apache.beam:beam-sdks-java-core:$beamVersion")
    implementation("org.apache.beam:beam-runners-direct-java:$beamVersion")
    implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:$beamVersion")

    // Use the Kotlin test library.
    testImplementation(kotlin("test"))
    // Use the Kotlin JUnit integration.
    testImplementation(kotlin("test-junit"))
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation("org.apache.beam:beam-sdks-java-extensions-json-jackson:$beamVersion")
}
