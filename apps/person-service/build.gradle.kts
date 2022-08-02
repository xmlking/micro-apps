plugins {
    kotlin("plugin.allopen")
    id("io.quarkus")
}

dependencies {
    implementation(enforcedPlatform(libs.quarkus.bom.get()))
    implementation(libs.quarkus.smallrye.graphql)
    implementation(libs.quarkus.arc)
    implementation(libs.quarkus.resteasy.core)

    implementation(libs.bundles.quarkus.hibernate)

    testImplementation(libs.quarkus.junit5.test)
    // testImplementation(libs.quarkus.jacoco.test)
    testImplementation(libs.rest.assured.test)
}

affectedTestConfiguration { jvmTestTask = "check" }

quarkus {
//    setOutputDirectory("$projectDir/build/classes/kotlin/main")
}

allOpen {
    // NOTE: add all classes' annotations that need need hot-reload
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
    annotation("org.eclipse.microprofile.graphql.GraphQLApi")
    // annotation("javax.persistence.Entity")
}

tasks {
    test {
        systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    }

    quarkusDev {
//        setSourceDir("$projectDir/src/main/kotlin")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
