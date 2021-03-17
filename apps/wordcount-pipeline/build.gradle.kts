val kotlinVersion: String by project
val beamVersion: String by project
val csvVersion: String by project
val junitVersion: String by project
val hamcrestVersion: String by project
val guavaVersion: String by project
val slf4jVersion: String by project

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
    api("com.google.guava:guava:$guavaVersion") // Force `-jre` version instead of `-android`

    // Test with JUnit4 & JUnit5
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
}

application {
    mainClass.set("micro.apps.pipeline.WordCountPipeline")
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs = listOf("-Djava.util.logging.config.file=src/main/resources/logging.properties", "-Dmicro.apps.level=FINE")
}

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}
