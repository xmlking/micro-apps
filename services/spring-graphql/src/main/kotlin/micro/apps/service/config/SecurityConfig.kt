package micro.apps.service.config

import micro.apps.service.config.Authorities.SCOPE_ACTUATOR
import micro.apps.service.config.Authorities.SCOPE_API
import micro.apps.service.config.Authorities.SCOPE_H2
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurityDsl
import org.springframework.security.config.web.server.invoke
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.MappedJwtClaimSetConverter
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher
import java.util.Collections
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val logger = KotlinLogging.logger {}

@Profile("!test")
@Configuration
@EnableWebFluxSecurity
// OR @EnableRSocketSecurity
@EnableReactiveMethodSecurity
class SecurityConfig(
    @Value("\${security.jwt.signing-key}") private val secretKey: String
) {
    @Bean
    @Order(101)
    fun apiSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            securityMatcher(
                PathPatternParserServerWebExchangeMatcher("/api/**")
            )
            defaults()
            authorizeExchange {
                authorize("/api/**", hasAuthority(SCOPE_API))
            }
        }
    }

    @Bean
    @Order(102)
    fun actuatorSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            securityMatcher(EndpointRequest.toAnyEndpoint())
            defaults()
            authorizeExchange {
                authorize(EndpointRequest.to(InfoEndpoint::class.java), permitAll)
                authorize(EndpointRequest.to(HealthEndpoint::class.java), permitAll)
                authorize(EndpointRequest.toAnyEndpoint(), hasAuthority(SCOPE_ACTUATOR))
            }
        }
    }

    @Bean
    @Order(199)
    fun generalSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            securityMatcher(
                PathPatternParserServerWebExchangeMatcher("/**")
            )
            defaults()
            authorizeExchange {
                authorize("/favicon.ico", permitAll)
                authorize("/docs/**", permitAll)
                authorize("/error", permitAll)
                authorize("/graphiql", permitAll)
                // authorize("/graphiql", hasAuthority(SCOPE_GRAPHIQL))
                authorize("/h2-console", hasAuthority(SCOPE_H2))
                // authorize("/**", denyAll)
                authorize(anyExchange, authenticated)
            }
        }
    }

    private fun ServerHttpSecurityDsl.defaults() {
        cors { disable() }
        csrf { disable() }
        requestCache {
            requestCache = NoOpServerRequestCache.getInstance()
        }
        formLogin {
            disable()
        }
        logout {
            disable()
        }
        oauth2ResourceServer {
            jwt {
                // jwtAuthenticationConverter = jwtAuthenticationConverter()
                // jwtDecoder = reactiveJwtDecoder()
            }
        }
    }

    /**
     * Claims Mapping:
     * Use either jwtAuthenticationConverter() or reactiveJwtDecoder's MappedJwtClaimSetConverter
     * for claims mapping. Don't use both.
     */

    /*
    @Bean
    fun jwtAuthenticationConverter(): ReactiveJwtAuthenticationConverter {
        return ReactiveJwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter { jwt: Jwt ->
                println("${jwt.tokenValue}\nwith claims: ${jwt.claims}")
                Flux.concat(
                    Flux.fromIterable(
                        ((jwt.claims["realm_access"] as Map<*, *>)["roles"] as List<*>)
                    ).map { SimpleGrantedAuthority("ROLE_$it") },
                    Flux.fromIterable(
                        ((jwt.claims["scope"] as String).split(" "))
                    ).map { SimpleGrantedAuthority("SCOPE_$it") },
                )
            }
        }
    }
    */

    @Bean
    fun reactiveJwtDecoder(): ReactiveJwtDecoder {
        // val delegatingOAuth2TokenValidator = DelegatingOAuth2TokenValidator(JwtTimestampValidator())
        val delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap())

        return NimbusReactiveJwtDecoder.withSecretKey(
            SecretKeySpec(
                secretKey.encodeToByteArray(),
                Mac.getInstance("HmacSHA256").algorithm
            )
        ).macAlgorithm(MacAlgorithm.HS256).build()
            .also { decoder ->
                // decoder.setJwtValidator(delegatingOAuth2TokenValidator)
                decoder.setClaimSetConverter {
                    val convertedClaims = delegate.convert(it)
                    convertedClaims?.let {
                        it["https://hasura.io/jwt/claims"]?.let {
                            it as MutableMap<*, *>
                            convertedClaims.getOrPut("sub") { it["x-hasura-user-id"] }
                            convertedClaims.getOrPut("scp") { (it["x-hasura-allowed-roles"] as ArrayList<*>).joinToString(" ") }
                        }
                    }
                    logger.atDebug().log("convertedClaims {}", convertedClaims)
                    convertedClaims
                }
            }
    }

    @Bean
    fun securityContextRepository(): NoOpServerSecurityContextRepository {
        return NoOpServerSecurityContextRepository.getInstance()
    }
}
