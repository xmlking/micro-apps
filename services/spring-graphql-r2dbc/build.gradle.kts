@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.gradle.flyway)
    // TODO: enable when 2.x is released.
    // https://github.com/graphql-java-generator/graphql-maven-plugin-project/issues/170
    // alias(libs.plugins.gradle.graphql)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(projects.libs.core)
    implementation(projects.libs.graphql)

    // Spring
    implementation(libs.bundles.spring.graphql)
    // implementation(libs.spring.boot.starter.rsocket) // rsocket-starter is optional
    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.spring.boot.starter.oauth2.resource.server)

    // TODO: enable when 2.x is released.
    // implementation(libs.graphql.java.client.runtime)

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
    testImplementation(testFixtures(projects.libs.test))
    testImplementation(libs.spring.boot.starter.test) {
        exclude(module = "mockito-core")
    }
    testImplementation(libs.spring.boot.reactor.test)
    testImplementation(libs.spring.boot.mockk.test)
    testImplementation(libs.kotest.assertions.json.jvm)
    testImplementation(libs.kotest.extensions.spring)
    testImplementation(libs.spring.boot.graphql.test)
    testImplementation(libs.spring.boot.security.test)
    testImplementation(libs.spring.boot.flyway.test)
}

affectedTestConfiguration { jvmTestTask = "check" }

flyway {
    cleanDisabled = false
    url = env.DB_FLYWAY_URL.value
    user = env.DB_USER.value
    password = env.DB_PASSWORD.value
    schemas = arrayOf("PUBLIC")
    defaultSchema = "PUBLIC"
    placeholders = mapOf("type_serial" to "SERIAL")
    locations = arrayOf("classpath:db/migration/common", "classpath:/db/migration/${env.DB_FLYWAY_VENDOR.value}", "classpath:/db/testdata")
}

noArg {
    invokeInitializers = true
    annotation("micro.apps.model.NoArg")
    annotation("com.redis.om.spring.annotations.Document")
    annotation("org.springframework.data.redis.core.RedisHash")
    annotation("org.springframework.data.relational.core.mapping.Table")
}

// TODO: enable when 2.x is released.
/*
generatePojoConf {
    packageName = "$group.graphql"
    setSchemaFileFolder("$projectDir/src/main/resources/graphql")
    mode = com.graphql_java_generator.plugin.conf.PluginMode.server

    customScalars.push(
        com.graphql_java_generator.plugin.conf.CustomScalarDefinition(
            "Long",
            "java.lang.Long",
            null,
            "graphql.scalars.ExtendedScalars.GraphQLLong",
            null
        )
    )
}
*/
