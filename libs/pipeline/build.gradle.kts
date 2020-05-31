val beamVersion: String by project
val junitVersion: String by project
val hamcrestVersion: String by project
val slf4jVersion: String by project
val googlePubsubVersion: String by project

plugins {
    `java-test-fixtures`
}

dependencies {
    // Use Apache Beam
    implementation("org.apache.beam:beam-sdks-java-core:$beamVersion")
    implementation("org.apache.beam:beam-runners-direct-java:$beamVersion")
    implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java:$beamVersion")
    implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:$beamVersion")

    // Test with JUnit4 & JUnit5
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testImplementation(testFixtures(project(":libs:test")))
    // testFixturesImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    testFixturesImplementation(kotlin("stdlib-jdk8"))
    testFixturesImplementation("com.google.cloud:google-cloud-pubsub:$googlePubsubVersion")
}

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}
