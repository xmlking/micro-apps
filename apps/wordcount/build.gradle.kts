plugins {
//    id("kotlinx-serialization")
}

val kotlinVersion: String by project
val beamVersion: String by project
val csvVersion: String by project

dependencies {
    implementation(project(":libs:core"))
    implementation(project(":libs:shared"))

    // Use Apache Beam
    implementation("org.apache.beam:beam-sdks-java-core:$beamVersion")
    implementation("org.apache.beam:beam-runners-direct-java:$beamVersion")
    implementation("org.apache.beam:beam-runners-google-cloud-dataflow-java:$beamVersion")
    implementation("org.apache.beam:beam-sdks-java-io-google-cloud-platform:$beamVersion")
    implementation("org.apache.commons:commons-csv:$csvVersion")
}

application {
    mainClassName = "micro.apps.pipeline.WordCountPipeline"
//    applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
}
