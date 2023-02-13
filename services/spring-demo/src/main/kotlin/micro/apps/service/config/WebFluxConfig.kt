package micro.apps.service.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
class WebFluxConfig : WebFluxConfigurer {
//    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
//        // configurer.registerDefaults(false)
//
//        // configurer.defaultCodecs().kotlinSerializationJsonEncoder(KotlinSerializationJsonEncoder(jsonCodecConfig))
//        // configurer.defaultCodecs().kotlinSerializationJsonDecoder(KotlinSerializationJsonDecoder(jsonCodecConfig))
//        configurer.customCodecs().registerWithDefaultConfig(KotlinSerializationJsonEncoder(jsonCodecConfig))
//        configurer.customCodecs().registerWithDefaultConfig(KotlinSerializationJsonDecoder(jsonCodecConfig))
//    }

//    override fun extendMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
//        val converter = KotlinSerializationJsonHttpMessageConverter(Json {
//            serializersModule = modelSerializersModule
//            ignoreUnknownKeys = true
//        })
//        converters.forEachIndexed { index, httpMessageConverter ->
//            if (httpMessageConverter is KotlinSerializationJsonHttpMessageConverter) {
//                converters[index] = converter
//                return
//            }
//        }
//    }

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
