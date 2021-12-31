# Crypto

Encryption and decryption library using [AEAD](https://cloud.google.com/bigquery/docs/reference/standard-sql/aead-encryption-concepts) via Google's [tink](https://developers.google.com/tink)

## Prerequisites

1. [Tinkey](https://developers.google.com/tink/install-tinkey)
    ```bash
    brew tap google/tink https://github.com/google/tink
    brew install tinkey
    ```
2. [Generate](https://developers.google.com/tink/generate-encrypted-keyset) an encrypted keyset
    ```bash
    tinkey create-keyset --key-template AES128_GCM \
    --out-format json --out encrypted_aead_keyset.json \
    --master-key-uri gcp-kms://projects/tink-examples/locations/global/keyRings/foo/cryptoKeys/bar \
    --credentials gcp_credentials.json
    ```
    > *Since these keys are encrypted, you can store them anywhere you like.*
3. [Generate](https://developers.google.com/tink/generate-plaintext-keyset) a plaintext keyset
    ```bash
    tinkey create-keyset --key-template AES128_GCM  --out-format json --out aead_keyset.json
    # if you want to add more keys:
    tinkey add-key  --in aead_keyset.json  --in-format json --out aead_keyset2.json --out-format json --key-template AES128_GCM
    tinkey add-key  --in aead_keyset2.json --in-format json --out aead_keyset3.json --out-format json --key-template AES128_GCM
it
    ```
   > **Caution:** *We don’t recommend generating plaintext keysets, as it’s easy for keys to leak. You should only use plaintext key-generation for testing purposes.*

## Usage

Add Gradle/Maven dependencies

https://github.com/xmlking/micro-apps/packages/1168119

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
    implementation("micro.libs:crypto:1.6.5-SNAPSHOT")
}
```

Example 
```kotlin
    test("Test Cryptor bootstrapped with plaintext keySet") {
    val cryptor: Cryptor = CryptorImpl("src/test/resources/aead_keyset.json")

    val plaintext = "hi! this is sumo".toByteArray()
    val associatedData = "test123".toByteArray()

    val ciphertext = cryptor.encrypt(plaintext, associatedData)
    val plaintext2 = cryptor.decrypt(ciphertext, associatedData)
    plaintext2 shouldBe plaintext
}
```

### Run

```bash

```

### Test

```bash
gradle libs:crypto:test
```

### Build

```bash
gradle libs:crypto:clean
gradle libs:crypto:build
```

### Publish

```bash
CI=true gradle libs:crypto:publish
```

## Operations
- key creation
- key rotation


## Referemce 

- [Authenticated Encryption with Associated Data (AEAD)](https://developers.google.com/tink/aead)
- [Managing key rotation](https://developers.google.com/tink/managing-key-rotation)
- [Create a new key for each purpose](https://developers.google.com/tink/create-new-key-for-each-purpose)
- [I want to protect structured data](https://developers.google.com/tink/encrypt-structured-data)
- [I want to bind ciphertext to its context](https://developers.google.com/tink/bind-ciphertext)
- [Tink API Guide](https://github.com/google/tink/blob/master/docs/JAVA-HOWTO.md)
