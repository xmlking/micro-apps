@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.gradle.flyway)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(project(":libs:core"))
    implementation(project(":libs:graphql"))

    // Spring
    implementation(libs.bundles.spring.graphql)
    // implementation(libs.spring.boot.starter.rsocket) // rsocket-starter is optional
    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.spring.boot.starter.oauth2.resource.server)

    // Database Drivers
    implementation(enforcedPlatform(libs.database.r2dbc.bom.get().toString()))
    runtimeOnly(libs.database.r2dbc.pool)
    runtimeOnly(libs.database.r2dbc.h2)
    // (or) runtimeOnly(libs.database.r2dbc.postgresql)
    // (or) runtimeOnly(libs.database.r2dbc.mssql)
    implementation(libs.flyway.core)
    // (and) implementation(libs.flyway.sqlserver)
    implementation(libs.jakarta.persistence)

    implementation(libs.arrow.core)
    implementation(libs.uuid)

    // DevTools
    annotationProcessor(libs.spring.boot.configuration.processor)
    annotationProcessor(libs.spring.boot.autoconfigure.processor)
    developmentOnly(libs.spring.boot.devtools)

    // TODO: add openTelemetry
    // micrometer for openTelemetry

    // Test
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.boot.reactor.test)
    testImplementation(libs.spring.boot.mockk.test)
    testImplementation(libs.kotest.assertions.json.jvm)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.spring.boot.graphql.test)
    testImplementation(libs.spring.boot.security.test)
}

affectedTestConfiguration { jvmTestTask = "check" }

flyway {
    cleanDisabled = false
    url = "jdbc:h2:./services/spring-graphql/build/database/testdb;AUTO_SERVER=TRUE;USER=sa;PASSWORD=password"
    user = "sa"
    password = "password"
    schemas = arrayOf("PUBLIC")
    defaultSchema = "PUBLIC"
}

noArg {
    invokeInitializers = true
    annotation("micro.apps.model.NoArg")
    annotation("com.redis.om.spring.annotations.Document")
    annotation("org.springframework.data.redis.core.RedisHash")
    annotation("org.springframework.data.relational.core.mapping.Table")
}
