val junitVersion = libs.versions.junit.get()
val slf4jVersion = libs.versions.slf4j.get()

plugins {
    `java-test-fixtures`
}

dependencies {
    // Use Apache Beam
    implementation(libs.bundles.beam)

    // Test with JUnit4 & JUnit5
    testImplementation(kotlin("test"))

    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:$junitVersion") {
        because("allows JUnit 4 tests run along with JUnit 5")
    }
    testImplementation(libs.hamcrest.all.test)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(testFixtures(project(":libs:test")))
    testFixturesImplementation(libs.google.cloud.pubsub)
}

affectedTestConfiguration { jvmTestTask = "check" }
