# Spring

## Spring Cloud

## Spring Cloud Functions

Ref: https://github.com/LeapAheadWithRedis6-2/redis-whats-new-functions

```gradle
implementation(enforcedPlatform(libs.spring.cloud.gcp.bom.get()))
implementation(libs.bundles.spring.cloud.functions)
```

## Spring Cloud GCP

```gradle
implementation(enforcedPlatform(libs.spring.cloud.gcp.bom.get()))
runtimeOnly(libs.bundles.spring.cloud.gcp.basic)
```
