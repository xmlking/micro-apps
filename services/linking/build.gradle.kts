@Suppress("DSL_SCOPE_VIOLATION") // TODO remove when https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.gradle.native)
    alias(libs.plugins.gradle.jib)
    application
}

dependencies {
    implementation(projects.libs.proto)
    implementation(projects.libs.grpc)

    // Grpc `io.grpc:grpc-all` has grpc-auth, grpc-alts, grpc-protobuf, grpc-xds ...
    runtimeOnly(libs.grpc.netty)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.services) // Optional. includes grpc-protobuf
    implementation(libs.grpc.xds) // Optional. includes grpc-services, grpc-auth,  grpc-alts
    implementation(libs.bundles.kotlinx.coroutines)

    // Protobuf - If you want to use features like protobuf JsonFormat, `protobuf-java-util` instead of `protobuf-java`
    implementation(libs.protobuf.java)
    implementation(libs.protobuf.kotlin)

    // Google
    implementation(libs.guava)

    // Kotlin Config
    implementation(libs.bundles.konf)

    // Resilience frameworks
    implementation(libs.sentinel.grpc.adapter)
    // implementation(libs.concurrency.limits.grpc)

    // Test
    // testImplementation("io.kotest.extensions:kotest-extensions-koin:{version}")
    testImplementation(testFixtures(projects.libs.test))
    testImplementation(testFixtures(projects.libs.model))
    testImplementation(testFixtures(projects.libs.proto))
    // grpc testing TODO: https://github.com/grpc/grpc-java/issues/5331
    testImplementation(libs.grpc.test)
    // testImplementation("io.grpc:grpc-testing:$grpcVersion")
}

affectedTestConfiguration { jvmTestTask = "check" }

application {
    mainClass.set("micro.apps.service.LinkingApplicationKt")
    //    applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs = listOf("-Dorg.slf4j.simpleLogger.log.micro.apps=debug")
}

tasks {
    register<JavaExec>("runLinkingClient") {
        mainClass.set("micro.apps.service.LinkingClientKt")
        classpath = sourceSets["main"].runtimeClasspath
        workingDir = rootDir
        jvmArgs = listOf("-Dorg.slf4j.simpleLogger.log.micro.apps=debug")
    }
    run.configure {
        // HINT: config/certs are at project root
        workingDir = rootDir
    }
    test {
        workingDir = rootDir
    }
}

jib {
    containerizingMode = "packaged"
    container {
        // since we have many main classes, it cannot be Inferred. lets help
        mainClass = application.mainClass.get()
    }
    // extraDirectories {
    //     setPaths("src/main/custom-extra-dir,/home/user/jib-extras")
    //     permissions = mapOf("/work" to "775", "/distr" to "775")
    // }
}
