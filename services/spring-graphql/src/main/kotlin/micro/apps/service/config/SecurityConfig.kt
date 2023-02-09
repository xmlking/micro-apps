package micro.apps.service.config

import micro.apps.service.config.Authorities.SCOPE_ACTUATOR
import micro.apps.service.config.Authorities.SCOPE_GRAPHIQL
import micro.apps.service.config.Authorities.SCOPE_GRAPHQL
import micro.apps.service.config.Authorities.SCOPE_H2
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurityDsl
import org.springframework.security.config.web.server.invoke
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder
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
    private val passwordEncoder = createDelegatingPasswordEncoder()

    @Bean
    @Order(101)
    fun graphqlSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http {
        securityMatcher(
            PathPatternParserServerWebExchangeMatcher("/graphql")
        )
        defaults()
        httpBasic {}
        oauth2ResourceServer {
            jwt {
                // jwtAuthenticationConverter = jwtAuthenticationConverter()
                // jwtDecoder = reactiveJwtDecoder()
            }
        }
        authorizeExchange {
            authorize("/graphql", hasAuthority(SCOPE_GRAPHQL))
        }
    }

    @Bean
    @Order(102)
    fun actuatorSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http {
        // securityMatcher(EndpointRequest.toAnyEndpoint())
        securityMatcher(
            PathPatternParserServerWebExchangeMatcher("/actuator/**")
        )
        defaults()
        httpBasic {}
        authorizeExchange {
            authorize("/actuator/info", permitAll)
            authorize("/actuator/health", permitAll)
            // FIXME: https://github.com/spring-projects/spring-boot/issues/13826
            // authorize(EndpointRequest.to(InfoEndpoint::class.java), permitAll)
            // authorize(EndpointRequest.to(HealthEndpoint::class.java), permitAll)
            // authorize(EndpointRequest.toAnyEndpoint(), hasAuthority(SCOPE_ACTUATOR))
            authorize("/actuator/**", hasAuthority(SCOPE_ACTUATOR))
        }
    }

    @Bean
    @Order(103)
    fun graphiqlSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http {
        securityMatcher(
            PathPatternParserServerWebExchangeMatcher("/graphiql")
        )
        defaults()
        httpBasic {}
        authorizeExchange {
            authorize("/graphiql", hasAuthority(SCOPE_GRAPHIQL))
        }
    }

    @Bean
    @Order(199)
    fun generalSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http {
        securityMatcher(
            PathPatternParserServerWebExchangeMatcher("/**")
        )
        defaults()
        authorizeExchange {
            authorize("/favicon.ico", permitAll)
            authorize("/docs/**", permitAll)
            authorize("/error", permitAll)
            authorize("/h2-console", hasAuthority(SCOPE_H2))
            authorize(anyExchange, denyAll)
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
        val delegate = MappedJwtClaimSetConverter.withDefaults(Collections.emptyMap())

        return NimbusReactiveJwtDecoder.withSecretKey(
            SecretKeySpec(
                secretKey.encodeToByteArray(),
                Mac.getInstance("HmacSHA256").algorithm
            )
        ).macAlgorithm(MacAlgorithm.HS256).build()
            .also { decoder ->
                decoder.setClaimSetConverter {
                    val convertedClaims = delegate.convert(it)
                    convertedClaims?.let {
                        it["https://hasura.io/jwt/claims"]?.let {
                            it as MutableMap<*, *>
                            convertedClaims.putIfAbsent("sub", it["x-hasura-user-id"])
                            convertedClaims.putIfAbsent("scp", (it["x-hasura-allowed-roles"] as ArrayList<*>).joinToString(" "))
                        }
                        convertedClaims["scp"] = convertedClaims["scp"] as String + " GRAPHQL"
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

    /**
     * In Memory UserDetailsManager for DevOps
     */
    @Bean
    fun userDetailsRepository(properties: UsersProperties): MapReactiveUserDetailsService {
        val users = properties.basicAuth.map { user ->
            User.builder()
                .username(user.username)
                .password(passwordEncoder.encode(user.password))
                .authorities(user.authorities.map(::SimpleGrantedAuthority))
                .build()
        }
        return MapReactiveUserDetailsService(users)
    }
}
