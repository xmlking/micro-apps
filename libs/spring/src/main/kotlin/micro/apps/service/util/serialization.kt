package micro.apps.service.util

import kotlinx.serialization.json.Json
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.web.reactive.function.client.ExchangeStrategies

/* Usage:
    private val client = WebTestClient.bindToServer()
        .baseUrl("http://localhost:8181")
        .exchangeStrategies(strategies)
        .responseTimeout(Duration.ofSeconds(30000))
        .build()
 */

val jsonCodecConfig: Json by lazy {
    Json {
        prettyPrint = true
        isLenient = true
        ignoreUnknownKeys = true
    }
}

// Polymorphic serialization
var strategies = ExchangeStrategies.builder()
    .codecs { clientDefaultCodecsConfigurer: ClientCodecConfigurer ->
        clientDefaultCodecsConfigurer.defaultCodecs()
            .kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(jsonCodecConfig))
        clientDefaultCodecsConfigurer.defaultCodecs()
            .kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(jsonCodecConfig))
    }
    .build()
