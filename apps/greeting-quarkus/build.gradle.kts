plugins {
    kotlin("plugin.allopen") version "1.3.71"
    id("io.quarkus") version "1.3.1.Final"
}

val quarkusPlatformVersion: String by project
val restAssuredVersion: String by project

dependencies {
    implementation(project(":libs:core"))
    // kotlin
    implementation(enforcedPlatform("io.quarkus:quarkus-bom:$quarkusPlatformVersion"))
    implementation("io.quarkus:quarkus-kotlin")
    // rest
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-resteasy-jsonb")
    // tooling
    implementation("io.quarkus:quarkus-smallrye-health")
    implementation("io.quarkus:quarkus-smallrye-metrics")
    implementation("io.quarkus:quarkus-smallrye-openapi")
    // deployment FIXME
    // implementation("io.quarkus:quarkus-container-image-jib")
    // implementation("io.quarkus:quarkus-kubernetes")
    // testing
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
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
        useJUnitPlatform()
    }

    quarkusDev {
        setSourceDir("$projectDir/src/main/kotlin")
    }

    quarkusBuild {
        isUberJar = true
    }

    buildNative {
        isEnableHttpUrlHandler = true
        // isEnableHttpsUrlHandler = true
        // dockerBuild = "true"
    }
}
