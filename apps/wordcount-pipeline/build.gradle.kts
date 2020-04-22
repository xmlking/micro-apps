val kotlinVersion: String by project
val beamVersion: String by project
val csvVersion: String by project
val hamcrestVersion: String by project
val floggerVersion: String by project

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

    runtimeOnly("com.google.flogger:flogger-slf4j-backend:$floggerVersion")
    testImplementation("org.hamcrest:hamcrest-all:$hamcrestVersion")
}

java {
    // Java 8 needed as Beam doesn't yet support 11
    // FIXME
    // sourceCompatibility = JavaVersion.VERSION_1_8
    // targetCompatibility = JavaVersion.VERSION_1_8
}

application {
    mainClassName = "micro.apps.pipeline.WordCountPipeline"
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs = listOf("-Dflogger.level=INFO")
}
