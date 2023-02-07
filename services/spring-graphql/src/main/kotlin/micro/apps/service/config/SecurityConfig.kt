package micro.apps.service.config

import micro.apps.service.config.Authorities.SCOPE_ACTUATOR
import micro.apps.service.config.Authorities.SCOPE_API
import micro.apps.service.config.Authorities.SCOPE_GRAPHIQL
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest
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
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher

// https://github.com/test-automation-in-practice/showcase-skill-manager/blob/master/deployables/skill-manager-service/src/main/kotlin/skillmanagement/runtime/security/WebSecurityConfiguration.kt
// https://github.com/santelos/pstorganov-showroom/blob/main/services/account/user-auth-service/src/main/kotlin/ru/stroganov/oauth2/userauthservice/config/SecurityConfig.kt
// https://github.com/Tiscs/spring-boot-practices/blob/main/sbp-users/src/main/kotlin/io/github/tiscs/sbp/config/WebSecurityConfig.kt
// https://github.com/rsTopStar/react-spring-project/blob/master/docs/security/method.md
// https://github.com/zzq1027/springsecurity5.8x/blob/main/docs/modules/ROOT/pages/migration/reactive.adoc
// https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/jwt.html
// only have spring-security-oauth2-resource-server ???
// https://github.com/saint-rivers/orderup-docker-cleanup/tree/master/user-service

@Profile("!test")
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class SecurityConfig {
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
//        return http.build()
    }

    @Bean
    @Order(102)
    fun actuatorSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
            securityMatcher(EndpointRequest.toAnyEndpoint())

            defaults()

            authorizeExchange {
                authorize(EndpointRequest.to(InfoEndpoint::class.java), permitAll)
                authorize(EndpointRequest.toAnyEndpoint(), hasAuthority(SCOPE_ACTUATOR))
            }
        }
    }

    @Bean
    @Order(103)
    fun graphiqlSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
//            securityMatcher("/graphiql")
            securityMatcher(
                PathPatternParserServerWebExchangeMatcher("/graphiql")
            )

            defaults()

            authorizeExchange {
                authorize("/graphiql", hasAuthority(SCOPE_GRAPHIQL))
            }
        }
    }

    @Bean
    @Order(199)
    fun generalSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http {
//            securityMatcher("/**")
            securityMatcher(
                PathPatternParserServerWebExchangeMatcher("/**")
            )

            defaults()

            authorizeExchange {
                authorize("/docs/**", permitAll)
                authorize("/error", permitAll)
                authorize("/**", denyAll)
            }
        }
    }

    private fun ServerHttpSecurityDsl.defaults() {
        cors { disable() }
        csrf { disable() }
//        csrf {
//            csrfTokenRequestHandler = XorServerCsrfTokenRequestAttributeHandler()
//        }
        requestCache {
            requestCache = NoOpServerRequestCache.getInstance()
        }
        formLogin {
            disable()
        }
        logout {
            disable()
        }
        // httpBasic {}
        oauth2ResourceServer {
            jwt {
                // jwtAuthenticationConverter = myConverter()
                // jwkSetUri = "https://idp.example.com/.well-known/jwks.json"
                // jwtDecoder = myCustomDecoder()
            }
        }
    }

    @Bean
    fun securityContextRepository(): NoOpServerSecurityContextRepository {
        return NoOpServerSecurityContextRepository.getInstance()
    }
}
