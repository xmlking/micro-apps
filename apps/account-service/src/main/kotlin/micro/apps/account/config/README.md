# Config

## Usage

```kotlin
    println(config[TLS.caCert])
    println(config[TLS.serverCert])
    println(config[TLS.serverKey])
    println(config<String>("endpoints.account"))
    println("server.host" in config)
    println(config.containsRequired())
```
