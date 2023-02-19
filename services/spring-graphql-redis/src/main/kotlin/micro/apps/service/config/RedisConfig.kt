package micro.apps.service.config

import kotlinx.serialization.ExperimentalSerializationApi
import org.springframework.context.annotation.Configuration

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
