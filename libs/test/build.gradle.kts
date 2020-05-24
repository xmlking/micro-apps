val kotestVersion: String by project

plugins {
    // which produces test fixtures
    `java-test-fixtures`
}

dependencies {
    testFixturesImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
}
