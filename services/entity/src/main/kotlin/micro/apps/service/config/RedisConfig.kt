package micro.apps.service.config

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.service.domain.account.PersonEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
@EnableRedisRepositories
@OptIn(ExperimentalSerializationApi::class)
class RedisConfig {

    @Bean
    fun reactiveRedisTemplate(connectionFactory: ReactiveRedisConnectionFactory, objectMapper: ObjectMapper): ReactiveRedisTemplate<String, PersonEntity> {
        val valueSerializer = Jackson2JsonRedisSerializer(PersonEntity::class.java).apply {
            setObjectMapper(objectMapper)
        }
        return ReactiveRedisTemplate(
            connectionFactory,
            newSerializationContext<String, PersonEntity>(StringRedisSerializer())
                .value(valueSerializer)
                .build()
        )
    }
}
