plugins {
    kotlin("kapt")
    kotlin("plugin.allopen")
    id("io.quarkus")
}
val quarkusPlatformGroupId: String by project
val quarkusPlatformVersion: String by project
val quarkusPlatformArtifactId: String by project
val restAssuredVersion: String by project
val slf4jVersion: String by project
val grpcVersion: String by project
val grpcKotlinVersion: String by project
val coroutinesVersion: String by project
val mapstructVersion: String by project

dependencies {
    implementation(project(":libs:core"))
    implementation(project(":libs:model"))
    implementation(project(":libs:proto"))

    // quarkus
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("io.quarkus:quarkus-kotlin")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-config-yaml")
    // implementation("io.quarkus:quarkus-logging-json")
    // rest
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jsonb")
    // mapper
    implementation("org.mapstruct:mapstruct:$mapstructVersion")
    kapt("org.mapstruct:mapstruct-processor:$mapstructVersion")
    // security
    implementation("io.quarkus:quarkus-oidc")
    // grpc
    implementation("io.quarkus:quarkus-grpc")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")
    // tooling
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-smallrye-metrics")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    // deployment
    implementation("io.quarkus:quarkus-container-image-jib")
    implementation("io.quarkus:quarkus-kubernetes")
    // testing
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:kotlin-extensions")
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
    }

    buildNative {
        // isEnableHttpUrlHandler = true
        // isEnableHttpsUrlHandler = true
        // dockerBuild = "true"
    }
}
