@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    maven("https://packages.confluent.io/maven/")
}

dependencies {
    implementation(projects.libs.model)
    implementation(projects.libs.spring)
    implementation(libs.bundles.spring.basic)
    developmentOnly(libs.spring.boot.devtools)

    // projectreactor
    implementation(libs.spring.boot.reactor.kotlin.extensions)
    testImplementation(libs.spring.boot.reactor.test)

    // spring-cloud bom
    implementation(enforcedPlatform(libs.spring.cloud.bom.get().toString()))
    // we need add `kafka` binder for `Supplier` functions
    // We can only use `Consumer` and `Function` functions with KStream binder.
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")
    // FIXME: Kotlin Lambda support https://github.com/spring-cloud/spring-cloud-function/issues/780
    implementation("org.springframework.cloud:spring-cloud-function-kotlin")

    // kafka serializers
    implementation(libs.kafka.avro.serializer)
    implementation(libs.kafka.avro4k.serializer)

    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.boot.reactor.test)
    testImplementation(libs.spring.boot.mockk.test)
    testImplementation(libs.kotest.assertions.json.jvm)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation("org.springframework.kafka:spring-kafka-test")
}

affectedTestConfiguration { jvmTestTask = "check" }

tasks {
    bootBuildImage {
        verboseLogging.set(true)
    }
}

noArg {
    invokeInitializers = true
    annotation("micro.apps.model.NoArg")
    annotation("com.redis.om.spring.annotations.Document")
    annotation("org.springframework.data.redis.core.RedisHash")
    annotation("org.springframework.data.relational.core.mapping.Table")
}
