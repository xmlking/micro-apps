import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.PASSED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT
// https://docs.gradle.org/current/userguide/jvm_test_suite_plugin.html
// https://docs.gradle.org/current/samples/sample_incubating_jvm_multi_project_with_additional_test_types.html
plugins {
    java
}

repositories {
    mavenCentral()
}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
            targets {
                all {
                    testTask.configure {
                        // select test framework before configuring options
                        useJUnitPlatform {
                            excludeTags("slow", "integration")
                        }
                        filter {
                            isFailOnNoMatchingTests = false
                        }
                        // We configure the logging for our tests.
                        // maxParallelForks = Runtime.getRuntime().availableProcessors() // FIXME: port conflict for quarkus
                        testLogging {
                            exceptionFormat = FULL
                            showExceptions = true
                            showStandardStreams = true
                            events(PASSED, FAILED, SKIPPED, STANDARD_OUT, STANDARD_ERROR)
                        }
                    }
                }
            }

        }

        val integrationTest by registering(JvmTestSuite::class) {
            dependencies {
                implementation(project())
            }
            targets {
                all {
                    testTask.configure {
                        // select test framework before configuring options
                        useJUnitPlatform {
                            includeTags("integration", "e2e")
                        }
                        filter {
                            isFailOnNoMatchingTests = false
                        }
                        // We configure the logging for our tests.
                        // maxParallelForks = Runtime.getRuntime().availableProcessors() // FIXME: port conflict for quarkus
                        testLogging {
                            exceptionFormat = FULL
                            showExceptions = true
                            showStandardStreams = true
                            events(PASSED, FAILED, SKIPPED, STANDARD_OUT, STANDARD_ERROR)
                        }
                        shouldRunAfter(test)
                    }
                }
            }
        }
    }
}

tasks.named("check") {
    dependsOn(testing.suites.named("integrationTest"))
}
