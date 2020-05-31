val kotlinCoroutinesVersion: String by project
val guavaVersion: String by project
val grpcVersion: String by project
val grpcKotlinVersion: String by project
val protobufVersion: String by project
val arrowVersion: String by project
val sentinelVersion: String by project

plugins {
    `java-test-fixtures`
}

dependencies {
    // gRPC
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$kotlinCoroutinesVersion")

    // Protobuf
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")
    implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")

    // Google
    implementation("com.google.guava:guava:$guavaVersion")

    // Resilience frameworks
    implementation("com.alibaba.csp:sentinel-grpc-adapter:$sentinelVersion")
    // implementation("com.netflix.concurrency-limits:concurrency-limits-grpc:$netflixConcurrencyVersion")

    // Test
    testImplementation(testFixtures(project(":libs:test")))
    // testFixturesImplementation("io.kotest:kotest-runner-junit5-jvm:$kotestVersion")
    // testFixturesImplementation(kotlin("stdlib-jdk8"))
}
