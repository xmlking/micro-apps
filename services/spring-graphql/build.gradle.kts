@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
    alias(libs.plugins.gradle.flyway)
    alias(libs.plugins.gradle.lombok)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    // alias(libs.plugins.kotlin.kapt)
    id(libs.plugins.kotlin.kapt.get().pluginId)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

/*
idea {
    module {
        val kaptMain = file("$buildDir/generated/source/kapt/main")
        sourceDirs.plusAssign(kaptMain)
        generatedSourceDirs.plusAssign(kaptMain)
    }
}
*/

dependencies {
    implementation(project(":libs:core"))
    implementation(project(":libs:graphql"))
    // Spring
    implementation(libs.bundles.spring.graphql)
    // rsocket-starter is optional
    // implementation(libs.spring.boot.starter.rsocket)
    implementation(libs.spring.boot.starter.data.jpa)
//    implementation(libs.spring.boot.starter.data.r2dbc)
    implementation(libs.spring.boot.starter.oauth2.resource.server)
    implementation(libs.jackson.module.kotlin)

    // Query SQL
    implementation(libs.querydsl.jpa) { artifact { classifier = "jakarta" } }
    kapt(group = "com.querydsl", name = "querydsl-apt", classifier = "jakarta")
    // FIXME: https://github.com/querydsl/querydsl/discussions/3036
    // kapt("com.querydsl:querydsl-kotlin-codegen")
    implementation(libs.arrow.core)
    implementation(libs.uuid)

//    runtimeOnly(libs.database.h2.r2dbc)
    runtimeOnly(libs.database.h2)
    implementation(libs.flyway.core)

    // DevTools
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    developmentOnly(libs.spring.boot.devtools)
    annotationProcessor(libs.spring.boot.configuration.processor)
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

// 	testCompileOnly("com.querydsl:querydsl-jpa")
//  testAnnotationProcessor(group = "com.querydsl", name = "querydsl-apt", classifier = "jakarta")
// 	testImplementation("org.projectlombok:lombok")
// 	testAnnotationProcessor("org.projectlombok:lombok")
// 	testCompileOnly("org.projectlombok:lombok")
//  kaptTest(group = "com.querydsl", name = "querydsl-apt", classifier = "jakarta")
}

flyway {
    url = "jdbc:h2:./build/database/db;AUTO_SERVER=TRUE"
    user = "user"
    password = "password"
}

kapt {
    keepJavacAnnotationProcessors = true
}
