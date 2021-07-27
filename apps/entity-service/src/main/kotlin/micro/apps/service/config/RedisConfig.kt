package micro.apps.service.config

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.service.domain.account.PersonEntity
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext.newSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableRedisRepositories
@EnableTransactionManagement
@OptIn(ExperimentalSerializationApi::class)
class RedisConfig {

    // TODO https://github.com/ptr-dorjin/reactive-sensor/blob/main/reactive-server-redis/src/main/kotlin/pd/sensor/reactive/server/repository/RedisConfig.kt

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

    /*
    @Bean
    fun reactiveRedisTemplate1(connectionFactory: ReactiveRedisConnectionFactory, resourceLoader: ResourceLoader): ReactiveRedisTemplate<String, Any> {
        val jdkSerializer = JdkSerializationRedisSerializer(resourceLoader.classLoader)
        val serializationContext =
            newSerializationContext<String, Any>().key(StringRedisSerializer()).value(jdkSerializer).hashKey(jdkSerializer)
                .hashValue(jdkSerializer).build()
        return ReactiveRedisTemplate(connectionFactory, serializationContext)
    }

    @Bean
    fun reactiveJsonPersonRedisTemplate(redisConnectionFactory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, PersonEntity> {
        val serializationContext = newSerializationContext<String, PersonEntity>(StringRedisSerializer())
                .value(GenericToStringSerializer(PersonEntity::class.java))
                .hashKey( StringRedisSerializer())
                .hashValue( GenericToStringSerializer(PersonEntity::class.java))
                .build()

        return  ReactiveRedisTemplate(redisConnectionFactory, serializationContext)
    }
    */

    // to EnableTransactionSupport
    @OptIn(ExperimentalSerializationApi::class)
    @Bean
    @Primary
    fun redisTemplate1(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, PersonEntity>? {
        val redisTemplate = RedisTemplate<String, PersonEntity>()
        redisTemplate.setConnectionFactory(redisConnectionFactory)
        redisTemplate.setEnableTransactionSupport(true)
        return redisTemplate
    }

/*
@Bean
fun keyValueMappingContext(): RedisMappingContext? {
return RedisMappingContext(
MappingConfiguration(IndexConfiguration(), MyKeyspaceConfiguration())
)
}

class MyKeyspaceConfiguration : KeyspaceConfiguration() {
override fun initialConfiguration(): Iterable<KeyspaceSettings> {
return listOf(KeyspaceSettings(Person::class.java, "people"))
}
}

class MyIndexConfiguration : IndexConfiguration() {
override fun initialConfiguration(): Iterable<IndexDefinition> {
return listOf(SimpleIndexDefinition("people", "firstname"))
}
}
*/
}
