package micro.apps.service.config

import micro.apps.service.util.jsonCodecConfig
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.http.codec.json.KotlinSerializationJsonEncoder
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebFluxConfig : WebFluxConfigurer {
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        // configurer.registerDefaults(false)

        // configurer.defaultCodecs().kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(jsonCodecConfig))
        // configurer.defaultCodecs().kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(jsonCodecConfig))
        configurer.customCodecs().registerWithDefaultConfig(KotlinSerializationJsonEncoder(jsonCodecConfig))
        configurer.customCodecs().registerWithDefaultConfig(KotlinSerializationJsonDecoder(jsonCodecConfig))
    }

//    @Bean
//    @Primary
//    fun corsWebFilter(
//        appConfiguration: ApplicationCorsConfiguration
//    ): CorsWebFilter {
//        val configuration = CorsConfiguration()
//        configuration.maxAge = appConfiguration.maxAge
//        configuration.allowedOrigins = appConfiguration.allowedOrigins
//        configuration.allowedMethods = listOf(
//            HttpMethod.GET,
//            HttpMethod.POST,
//            HttpMethod.PUT,
//            HttpMethod.DELETE,
//        ).map(HttpMethod::name)
//        configuration.allowedHeaders = listOf(
//            HttpHeaders.CONTENT_TYPE
//        )
//
//        val source = UrlBasedCorsConfigurationSource()
//        source.registerCorsConfiguration("/**", configuration)
//
//        return CorsWebFilter(source)
//    }
}
