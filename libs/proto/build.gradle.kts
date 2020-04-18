/* ktlint-disable no-wildcard-imports */
import com.google.protobuf.gradle.*

val grpcVersion: String by project
val protobufVersion: String by project
val pgvVersion: String by project

plugins {
    id("com.google.protobuf") version "0.8.12"
}

dependencies {
    implementation("io.grpc:grpc-netty-shaded:$grpcVersion")
    implementation("io.grpc:grpc-protobuf:$grpcVersion")
    implementation("io.grpc:grpc-stub:$grpcVersion")
    // You need to depend on the lite runtime library, not protobuf-java
    implementation("com.google.protobuf:protobuf-javalite:$protobufVersion")
    if (JavaVersion.current().isJava9Compatible) {
        // Workaround for @javax.annotation.Generated
        // see: https://github.com/grpc/grpc-java/issues/3633
        implementation("javax.annotation:javax.annotation-api:1.3.2")
    }
    implementation("io.envoyproxy.protoc-gen-validate:pgv-java-stub:$pgvVersion")
    testImplementation("io.grpc:grpc-testing:$grpcVersion")
}

sourceSets {
    main {
        proto {
            // In addition to the default 'src/main/proto'
            srcDir("third_party_proto")
        }
    }
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:$protobufVersion"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
        }
        id("javapgv") {
            artifact = "io.envoyproxy.protoc-gen-validate:protoc-gen-validate:$pgvVersion"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
                id("javapgv") {
                    option("lang=java")
                }
            }
            it.generateDescriptorSet = true
        }
    }
}
