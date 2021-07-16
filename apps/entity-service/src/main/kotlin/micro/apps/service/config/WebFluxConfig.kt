package micro.apps.service.config

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebFluxConfig : WebFluxConfigurer {
    val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        // configurer.registerDefaults(false)

        // configurer.defaultCodecs().kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(json))
        // configurer.defaultCodecs().kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(json))
        configurer.customCodecs().registerWithDefaultConfig(KotlinSerializationJsonEncoder(json))
        configurer.customCodecs().registerWithDefaultConfig(KotlinSerializationJsonDecoder(json))
    }

}
