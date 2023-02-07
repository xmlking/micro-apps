@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.spring)

    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)

    `java-test-fixtures`
}

dependencies {
    implementation(project(":libs:core"))
    // Spring
    implementation(libs.bundles.spring.graphql)
    // Test
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.boot.reactor.test)
    testImplementation(libs.spring.boot.mockk.test)
    testImplementation(libs.kotest.assertions.json.jvm)
    testImplementation(libs.kotest.extensions.spring)
}

tasks {
    bootJar {
        enabled = false
    }

    jar {
        enabled = true
    }
}

affectedTestConfiguration { jvmTestTask = "check" }
