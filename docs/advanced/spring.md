# Spring

## Spring Cloud

## Spring Cloud Functions

Ref: https://github.com/LeapAheadWithRedis6-2/redis-whats-new-functions

```gradle
implementation(enforcedPlatform(libs.spring.cloud.gcp.bom.get().toString()))
implementation(libs.bundles.spring.cloud.functions)
```

## Spring Cloud GCP

```gradle
implementation(enforcedPlatform(libs.spring.cloud.gcp.bom.get().toString()))
runtimeOnly(libs.bundles.spring.cloud.gcp.basic)
```
