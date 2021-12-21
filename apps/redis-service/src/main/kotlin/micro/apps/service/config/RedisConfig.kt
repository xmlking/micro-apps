package micro.apps.service.config

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories
import com.redis.om.spring.annotations.EnableRedisEnhancedRepositories
import kotlinx.serialization.ExperimentalSerializationApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate


@Configuration
@OptIn(ExperimentalSerializationApi::class)
class RedisConfig {

//    @Bean
//    fun redisTemplate(connectionFactory: RedisConnectionFactory): RedisTemplate<*, *> {
//        val template: RedisTemplate<*, *> = RedisTemplate<Any, Any>()
//        template.setConnectionFactory(connectionFactory)
//        return template
//    }
}
