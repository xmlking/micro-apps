# Kafka Streams Extensions

Kotlin Extensions for **Kafka Streams**

## Usage

Dependencies 

```kotlin
dependencies {
    implementation("micro.apps:kstream:1.6.1-SNAPSHOT")
}
```

```xml
<dependency>
  <groupId>micro.apps</groupId>
  <artifactId>greeting-quarkus</artifactId>
  <version>1.6.1-SNAPSHOT</version>
</dependency>
```

### Configuration

- **crypto.additionalData** (optional)
  - e.g., `crypto.additionalData: record.customer_id`
- **crypto.keyFile** (required)
  - e.g., `crypto.keyFile: /etc/config/aead_keyset.json`
- **crypto.useKms** (optional, default: false)
  - e.g., `crypto.useKms: true`
- **crypto.kmsUri** (optional, default: all KMS keys)
  - e.g., `crypto.kmsUri: true`
- **crypto.credentialFile** (optional)
  - e.g., `crypto.credentialFile: /etc/config/gcp_credential.json`

### Assumptions

- **CryptoAvro4kSerde** need **Schema Registry** enabled Kafka runtime environment.
- use Avro for serialization

### Limitations

-  external CLI tools like `kafka-avro-console-consumer`, `kafka-avro-console-producer` may not work as data is encrypted. 


## Development

### Test

```bash
gradle libs:kstream:test
```

### Build

```bash
gradle libs:kstream:build
```

### Publish

```bash
gradle libs:kstream:publish
```
