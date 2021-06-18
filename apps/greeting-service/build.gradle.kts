plugins {
    kotlin("kapt")
    kotlin("plugin.allopen")
    id("io.quarkus")
}

dependencies {
    // TODO
    // implementation(projects.libs.core)
    // implementation(projects.libs.model)
    // implementation(projects.libs.proto)
    implementation(project(":libs:core"))
    implementation(project(":libs:model"))
    implementation(project(":libs:proto"))

    // quarkus
    implementation(enforcedPlatform(libs.quarkus.bom.get()))
    implementation(libs.bundles.quarkus.core)

    // https://smallrye.io/smallrye-mutiny/guides/kotlin
    implementation(libs.quarkus.mutiny.kotlin)
    // rest
    implementation(libs.bundles.quarkus.resteasy)
    // mapper
    // FIXME: https://github.com/quarkusio/quarkus/issues/14012
    implementation(libs.mapstruct.core)
    kapt(libs.mapstruct.processor)
    // security
    implementation(libs.quarkus.oidc)
//    implementation("io.quarkus:quarkus-oidc")
    // grpc
    implementation(libs.quarkus.grpc)
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.bundles.kotlinx.coroutines)
    // tooling
    implementation(libs.bundles.quarkus.tools)
    // deployment
    implementation(libs.bundles.quarkus.deployment)
    // testing
    testImplementation(libs.quarkus.junit5.test)
    testImplementation(libs.rest.assured.kotlin.test)
    testImplementation(testFixtures(project(":libs:proto")))
}

quarkus {
    setOutputDirectory("$projectDir/build/classes/kotlin/main")
}

allOpen {
    // NOTE: add all classes' annotations that need need hot-reload
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
}

tasks {
    test {
        systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    }

    quarkusDev {
        setSourceDir("$projectDir/src/main/kotlin")
        // You can change the working directory
        // HINT: config/certs are at project root
        // workingDir = rootDir.absolutePath
    }

    buildNative {
        // isEnableHttpUrlHandler = true
        // isEnableHttpsUrlHandler = true
        // dockerBuild = "true"
    }
}
