val junitVersion = libs.versions.junit.get()
val slf4jVersion = libs.versions.slf4j.get()

dependencies {
    implementation(project(":libs:core"))
    implementation(project(":libs:kbeam"))

    // Use Apache Beam
    implementation(libs.bundles.beam)
    // implementation(libs.beam.sdks.java.extensions.kryo)
    // implementation(libs.beam.sdks.java.extensions.euphoria)
    api(libs.guava) // Force `-jre` version instead of `-android`

    // Test with JUnit4 & JUnit5
    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.hamcrest.all.test)
}

affectedTestConfiguration { jvmTestTask = "check" }

application {
    mainClass.set("micro.apps.pipeline.WordCountPipeline")
    // applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs =
        listOf("-Djava.util.logging.config.file=src/main/resources/logging.properties", "-Dmicro.apps.level=FINE")
}

loggingCapabilities {
    selectSlf4JBinding("org.slf4j:slf4j-jdk14:$slf4jVersion")
}
