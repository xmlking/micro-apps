val kotestVersion: String by project

plugins {
    // which produces test fixtures
    `java-test-fixtures`
}

dependencies {
    testFixturesImplementation(libs.kotest.runner.junit5.jvm)
}

affectedTestConfiguration { jvmTestTask = "check" }

sonarqube {
    isSkipProject = true
}
