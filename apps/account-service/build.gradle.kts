plugins {
    id("org.graalvm.buildtools.native")
}

dependencies {
    implementation(project(":libs:proto"))
    implementation(project(":libs:service"))

    // Grpc `io.grpc:grpc-all` has grpc-auth, grpc-alts, grpc-protobuf, grpc-xds ...
    runtimeOnly(libs.grpc.netty)
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.kotlin.stub)
    implementation(libs.grpc.services) // Optional. includes grpc-protobuf
    implementation(libs.grpc.xds) // Optional. includes grpc-services, grpc-auth,  grpc-alts
    implementation(libs.bundles.kotlinx.coroutines)

    // Protobuf - If you want to use features like protobuf JsonFormat, `protobuf-java-util` instead of `protobuf-java`
    implementation(libs.protobuf.java)
    // Google
    implementation(libs.guava)

    // Kotlin Config
    implementation(libs.bundles.konf)

    // Resilience frameworks
    implementation(libs.sentinel.grpc.adapter)
    // implementation(libs.concurrency.limits.grpc)

    // Arrow, TODO: planing to use n the future
    // implementation(libs.arrow.core)
    // implementation(libs.arrow.syntax)
    // implementation(libs.arrow.fx)

    // Test
    // testImplementation("io.kotest.extensions:kotest-extensions-koin:{version}")
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(testFixtures(project(":libs:model")))
    testImplementation(testFixtures(project(":libs:proto")))
    // grpc testing TODO: https://github.com/grpc/grpc-java/issues/5331
    testImplementation(libs.grpc.test)
    // testImplementation("io.grpc:grpc-testing:$grpcVersion")
}

affectedTestConfiguration { jvmTestTask = "check" }

application {
    mainClass.set("micro.apps.service.AccountApplicationKt")
    //    applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs = listOf("-Dorg.slf4j.simpleLogger.log.micro.apps=debug")
}

tasks {
    register<JavaExec>("runAccountClient") {
        mainClass.set("micro.apps.service.domain.account.AccountClientKt")
        classpath = sourceSets["main"].runtimeClasspath
        workingDir = rootDir
        jvmArgs = listOf("-Dorg.slf4j.simpleLogger.log.micro.apps=debug")
    }
    register<JavaExec>("runEchoClient") {
        mainClass.set("micro.apps.service.domain.echo.EchoClientKt")
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
/*
    nativeBuild {
        imageName.set("application")
        mainClass.set("micro.apps.service.AccountApplicationKt")
        buildArgs("--no-server")
        debug.set(false)
        verbose.set(false)
        fallback.set(false)
        jvmArgs("flag")
        runtimeArgs("--help")
    }

    nativeTest {
        verbose.set(true)
    }
*/
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
