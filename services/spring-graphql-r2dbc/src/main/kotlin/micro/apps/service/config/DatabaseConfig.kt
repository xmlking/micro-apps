package micro.apps.service.config

import kotlinx.coroutines.reactor.mono
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.ReactiveAuditorAware
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing
import org.springframework.security.core.context.ReactiveSecurityContextHolder

@Configuration
// @EnableR2dbcRepositories
// @EnableTransactionManagement
@EnableR2dbcAuditing
class DatabaseConfig {
    /*
    @Bean
    fun transactionManager(connectionFactory: ConnectionFactory):
        ReactiveTransactionManager {
        return R2dbcTransactionManager(connectionFactory)
    }
    */

    @Bean
    fun reactiveAuditorAware(): ReactiveAuditorAware<String> {
        return ReactiveAuditorAware<String> {
            ReactiveSecurityContextHolder.getContext()
                .map { it.authentication }
                .filter { it.isAuthenticated }
                .map { it.name }
                .switchIfEmpty(mono { "anonymous" })
        }
    }
}
