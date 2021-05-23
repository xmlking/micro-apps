val coroutinesVersion: String by project
val grpcVersion: String by project
val grpcKotlinVersion: String by project
val protobufVersion: String by project
val konfigVersion: String by project
val arrowVersion: String by project
val sentinelVersion: String by project

dependencies {
    implementation(project(":libs:proto"))
    implementation(project(":libs:μservice"))

    // Grpc `io.grpc:grpc-all` has grpc-auth, grpc-alts, grpc-protobuf, grpc-xds ...
    runtimeOnly("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-services:$grpcVersion") // Optional. includes grpc-protobuf
    implementation("io.grpc:grpc-xds:$grpcVersion") // Optional. includes grpc-services, grpc-auth,  grpc-alts
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")

    // Protobuf - If you want to use features like protobuf JsonFormat, `protobuf-java-util` instead of `protobuf-java`
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")

    // Google
    implementation(libs.guava)

    // Kotlin Config
    implementation("com.uchuhimo:konf-core:$konfigVersion")
    implementation("com.uchuhimo:konf-yaml:$konfigVersion")

    // Resilience frameworks
    implementation("com.alibaba.csp:sentinel-grpc-adapter:$sentinelVersion")
    // implementation("com.netflix.concurrency-limits:concurrency-limits-grpc:$netflixConcurrencyVersion")

    // Arrow, TODO: planing to use n the future
    // implementation("io.arrow-kt:arrow-core:$arrowVersion")
    // implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
    // implementation("io.arrow-kt:arrow-fx:$arrowVersion")

    // Test
    // testImplementation("io.kotest.extensions:kotest-extensions-koin:{version}")
    testImplementation(testFixtures(project(":libs:test")))
    testImplementation(testFixtures(project(":libs:model")))
    testImplementation(testFixtures(project(":libs:proto")))
    // grpc testing TODO: https://github.com/grpc/grpc-java/issues/5331
    testImplementation("io.grpc:grpc-testing:$grpcVersion")
}

application {
    mainClass.set("micro.apps.account.AccountServerKt")
    //    applicationDefaultJvmArgs = listOf("-noverify", "-XX:TieredStopAtLevel=1")
    applicationDefaultJvmArgs = listOf("-Dorg.slf4j.simpleLogger.log.micro.apps=debug")
}

tasks {
    register<JavaExec>("runClient") {
        mainClass.set("micro.apps.account.AccountClientKt")
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
