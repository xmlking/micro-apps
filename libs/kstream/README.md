# Kafka Crypto SerDe

This package includes:
- **CryptoAvro4kSerde:** `SerDe` for _encrypt/decrypt_ payload and serialize using avro format.  
- Kotlin Extensions for **Kafka Streams DSL**

## Usage

Dependencies 

Add Gradle/Maven dependencies

https://github.com/xmlking/micro-apps/packages/1168256

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/xmlking/micro-apps")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    implementation("micro.libs:kstream:1.6.5-SNAPSHOT")
}
```

### Crypto

Generate `keyFile` using `tinkey` CLI. See the [docs](../crypto)

```bash
tinkey create-keyset --key-template AES128_GCM  --out-format json --out aead_keyset.json
```

### Configuration

- **crypto.keyFile** (required)
  - e.g., `crypto.keyFile: src/main/resources/aead_keyset.json`
- **crypto.associatedData** (optional)
    - e.g., `crypto.associatedData: customerId`
- **crypto.useKms** (optional, default: false)
  - e.g., `crypto.useKms: true`
- **crypto.kmsUri** (optional, default: all KMS keys)
  - e.g., `crypto.kmsUri: gcp-kms://projects/*/locations/*/keyRings/*/cryptoKeys/*`
- **crypto.credentialFile** (optional)
  - e.g., `crypto.credentialFile: /etc/config/gcp_credential.json`
- **crypto.ignoreDecryptFailures** (optional, default:false)
    - e.g., `crypto.ignoreDecryptFailures: true`

### Sample spring-boot configuration:

```yaml
# ======================== management ========================
management.endpoints.web.exposure.include: prometheus,health,info,metrics,bindings,kafkastreamstopology

# ========================== spring ==========================
spring:

  lifecycle:
    timeout-per-shutdown-phase: "10s"

# ========================== kafka ===========================
  kafka:
    properties:
      # Broker
      bootstrap.servers: localhost:9092
      # Schema Registry
      schema.registry.url: http://localhost:8081

# ================= spring-cloud-functions ===================
spring.cloud.function:
  definition: generate;city;state;print

# ================= spring-cloud-stream ======================
spring.cloud.stream:
  bindings:
    generate-out-0:
      destination: all-in-topic
      producer.useNativeEncoding: true
    city-in-0.destination: all-in-topic
    city-out-0.destination: city-out-topic
    state-in-0.destination: all-in-topic
    state-out-0.destination: state-out-topic
    print-in-0.destination: city-out-topic,state-out-topic

# =========== spring-cloud-stream kafka-binder================
# default producer config for all kafka binder bindings.
  kafka.binder:
    producer-properties:
      key.serializer: org.apache.kafka.common.serialization.StringSerializer
      record.packages: micro.apps.service
      value.serializer: micro.apps.kstream.serializer.CryptoKafkaAvro4kSerializer
      crypto.keyFile: src/main/resources/aead_keyset.json
      crypto.associatedData: name

# ============ spring-cloud-stream kstreams-binder============
  kafka.streams.binder:
    deserialization-exception-handler: logAndContinue
    configuration:
      record.packages: micro.apps.service
      default.key.serde: org.apache.kafka.common.serialization.Serdes$StringSerde
      default.value.serde: micro.apps.kstream.serializer.CryptoAvro4kSerde
      crypto.keyFile: src/main/resources/aead_keyset.json
      crypto.associatedData: name
```

Example App [streams-service](../../apps/streams-service)

### Assumptions

- **CryptoAvro4kSerde** need **Schema Registry** enabled Kafka runtime environment.
- use Avro for serialization

### Limitations

- External CLI tools like `kafka-avro-console-consumer`, `kafka-avro-console-producer` may not work as data is encrypted. 
- Not all features are implemented yet.

## Development

### Test

```bash
gradle libs:kstream:test
```

### Build

```bash
gradle libs:kstream:spotlessApply
gradle libs:kstream:build
```

### Publish

```bash
CI=true gradle libs:kstream:publish
```
