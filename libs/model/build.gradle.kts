@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
    // which produces test fixtures
    `java-test-fixtures`
}

dependencies {
    // Use Kotlin Serialization
    implementation(libs.bundles.kotlinx.serialization.all) // JSON, ProtoBuf, Avro serialization
    // implementation(libs.kotlinx.serialization.yaml) // YAML serialization

    // for validation annotations
    implementation(libs.jakarta.validation)

    // Testing
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.kotlinx.coroutines.test)
}

affectedTestConfiguration { jvmTestTask = "check" }

noArg {
    invokeInitializers = true
    annotation("micro.apps.model.NoArg")
    annotation("com.redis.om.spring.annotations.Document")
    annotation("org.springframework.data.redis.core.RedisHash")
    annotation("org.springframework.data.relational.core.mapping.Table")
}
