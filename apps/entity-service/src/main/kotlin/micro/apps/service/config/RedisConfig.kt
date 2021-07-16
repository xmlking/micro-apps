package micro.apps.service.config

import org.springframework.context.annotation.Configuration


//import org.springframework.data.redis.repository.configuration.EnableRedisRepositories


@Configuration
//@EnableRedisRepositories
class RedisConfig {

/*
@Bean
fun redisConnectionFactory(): RedisConnectionFactory? {
    return LettuceConnectionFactory()
}

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        return template;
    }

 */

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

/*
// https://todd.ginsberg.com/post/springboot-reactive-kotlin-coroutines/
@Bean
fun reactiveRedisTemplate(
connectionFactory: ReactiveRedisConnectionFactory,
objectMapper: ObjectMapper
): ReactiveRedisTemplate<String, Person> {

val valueSerializer = Jackson2JsonRedisSerializer(CounterEvent::class.java).apply {
setObjectMapper(objectMapper)
}

return ReactiveRedisTemplate(
connectionFactory,
newSerializationContext<String, CounterEvent>(StringRedisSerializer())
    .value(valueSerializer)
    .build()
)
}
*/

}
