plugins {
    kotlin("plugin.allopen")
    id("io.quarkus")
}
val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("$quarkusPlatformGroupId:$quarkusPlatformArtifactId:$quarkusPlatformVersion"))
    implementation("io.quarkus:quarkus-smallrye-graphql")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-resteasy")

    implementation("io.quarkus:quarkus-hibernate-orm")
    implementation("io.quarkus:quarkus-jdbc-h2")
    implementation("io.quarkus:quarkus-hibernate-validator")

    testImplementation("io.quarkus:quarkus-junit5")
    // testImplementation("io.quarkus:quarkus-jacoco")
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
    annotation("org.eclipse.microprofile.graphql.GraphQLApi")
    // annotation("javax.persistence.Entity")
}

tasks {
    test {
        systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
    }

    quarkusDev {
        setSourceDir("$projectDir/src/main/kotlin")
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
