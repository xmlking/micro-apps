# Config

## Usage

```kotlin
    println(config[TLS.caCert])
    println(config[TLS.serverCert])
    println(config[TLS.serverKey])
    println(config<String>("endpoints.account"))
    println("server.host" in config)
    println(config.containsRequired())

    var maxRetry by config.property(Account.maxRetry)
    println(maxRetry)

    // check if a property is set
    if (TLS.caCert in config) {
        // do something
    }

    // monitor config changes
    val handler = Account.maxRetry.onSet { value -> println("the maxRetry has changed to $value") }
    val handler2 = Account.maxRetry.beforeSet { config, value -> println("the maxRetry will change to $value") }
    val handler3 = config.beforeSet { item, value -> println("${item.name} will change to $value") }

    val maxRetry by config.property(Account.maxRetry)
    println(maxRetry)
    config.lazySet(Account.maxRetry) { 4 }
    println(maxRetry)
    config[Account.maxRetry] = 6
    println(maxRetry)

    private fun stop() {
        handler.cancel()
        handler2.cancel()
        handler3.cancel()
    }
```
