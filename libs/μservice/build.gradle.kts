val coroutinesVersion: String by project
val grpcVersion: String by project
val grpcKotlinVersion: String by project
val protobufVersion: String by project
val arrowVersion: String by project
val sentinelVersion: String by project

plugins {
    `java-test-fixtures`
}

dependencies {
    // Grpc `io.grpc:grpc-all` has grpc-auth, grpc-alts, grpc-protobuf, grpc-xds ...
    runtimeOnly("io.grpc:grpc-netty:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")

    // Protobuf - If you want to use features like protobuf JsonFormat, `protobuf-java-util` instead of `protobuf-java`
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")

    // Google
    implementation(libs.guava)

    // Resilience frameworks
    implementation("com.alibaba.csp:sentinel-grpc-adapter:$sentinelVersion")
    // implementation("com.netflix.concurrency-limits:concurrency-limits-grpc:$netflixConcurrencyVersion")

    // Test
    testImplementation(testFixtures(project(":libs:test")))
    // testFixturesImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    // testFixturesImplementation(kotlin("stdlib-jdk8"))
}
