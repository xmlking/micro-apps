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
    // implementation("io.grpc:grpc-stub:$grpcKotlinVersion") // For Java
    implementation("io.grpc:grpc-kotlin-stub:$grpcKotlinVersion") // For Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion") // For Kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion") // For Kotlin

    // Protobuf - If you want to use features like protobuf JsonFormat, `protobuf-java-util` instead of `protobuf-java`
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")

    // Google
    implementation(libs.guava)

    // Resilience frameworks
    implementation("com.alibaba.csp:sentinel-grpc-adapter:$sentinelVersion")
    // implementation("com.netflix.concurrency-limits:concurrency-limits-grpc:$netflixConcurrencyVersion")

    // Test
    testImplementation(testFixtures(project(":libs:test")))
}
