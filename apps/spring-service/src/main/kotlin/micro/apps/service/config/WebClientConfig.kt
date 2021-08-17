package micro.apps.service.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean(name = ["stockApiClient"])
    fun stockApiClient(webClientBuilder: WebClient.Builder): WebClient {
        return webClientBuilder.baseUrl("https://stock.api").build()
    }

    @Bean
    @Primary
    fun randomApiClient(webClientBuilder: WebClient.Builder): WebClient {
        return webClientBuilder.baseUrl("https://random.api").build()
    }

    @Bean
    @ConditionalOnMissingBean
    fun webClient(builder: WebClient.Builder): WebClient {
        return builder.build()
    }

    @Bean
    fun increaseMemorySizeOfWebClient() = WebClientCustomizer { wcb ->
        wcb.exchangeStrategies(
            ExchangeStrategies.builder().codecs { it.defaultCodecs().maxInMemorySize(1024 * 10 * 10 * 10 * 10) }.build()
        )
    }

    /*
    @Bean
    fun webClientCustomizer(
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager,
        @Value("\${gateway.url}") gatewayUrl: String
    ): WebClientCustomizer {
        val oAuth2Filter = ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
        oAuth2Filter.setDefaultClientRegistrationId("peacetrue")
        oAuth2Filter.setDefaultOAuth2AuthorizedClient(false)
        return WebClientCustomizer { webClientBuilder ->
            webClientBuilder
                .baseUrl(gatewayUrl)
                .filter(oAuth2Filter)
        }
    }
    */
}
