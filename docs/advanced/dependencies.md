# dependencies

## Gradle dependencies

### Spring Boot basic

```gradle
    implementation(libs.bundles.spring.basic)
```

### Spring Boot + gRPC

```gradle
    implementation(libs.bundles.spring.basic)
    
    implementation(projects.libs.proto)
    implementation(libs.bundles.spring.grpc)
```

if you are using `org.springframework.experimental.aot` plugin

```gradle
    implementation(projects.libs.proto)
    implementation(libs.bundles.spring.grpc) {
        exclude( group = "io.netty", module ="netty-tcnative-boringssl-static")
        exclude( group = "io.grpc", module ="grpc-netty-shaded")
    }
    implementation(libs.grpc.netty)
```
