val kotlinVersion: String by project
val beamVersion: String by project
val csvVersion: String by project
val junitVersion: String by project
val hamcrestVersion: String by project

dependencies {
    implementation(project(":libs:core"))
    implementation(project(":libs:kbeam"))

    // Use Apache Beam
    implementation("org.apache.beam:beam-sdks-java-core:$beamVersion")
    implementation("org.apache.beam:beam-runners-direct-java:$beamVersion")
    implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java:$beamVersion")
    implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:$beamVersion")
    // implementation("org.apache.beam:beam-sdks-java-extensions-kryo:$beamVersion")
    // implementation("org.apache.beam:beam-sdks-java-extensions-euphoria:$beamVersion")

    // Use the Kotlin test library.
    testImplementation(kotlin("test"))
    // Use the Kotlin JUnit integration.
    testImplementation(kotlin("test-junit"))
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
}

application {
    mainClassName = "micro.apps.pipeline.WordCountPipeline"
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
}
