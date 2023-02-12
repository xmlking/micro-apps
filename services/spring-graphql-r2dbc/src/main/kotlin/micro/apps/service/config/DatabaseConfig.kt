package micro.apps.service.config

// @Configuration
// @EnableR2dbcRepositories
// @EnableR2dbcAuditing
// @EnableTransactionManagement
class DatabaseConfig {
//    @Bean
//    fun transactionManager(connectionFactory: ConnectionFactory):
//        ReactiveTransactionManager {
//        return R2dbcTransactionManager(connectionFactory)
//    }

//    @Bean
//    fun initializer(connectionFactory: ConnectionFactory): ConnectionFactoryInitializer {
//        val initializer = ConnectionFactoryInitializer()
//        initializer.setConnectionFactory(connectionFactory)
//        val populator = CompositeDatabasePopulator()
//        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("schema.sql")))
//        populator.addPopulators(ResourceDatabasePopulator(ClassPathResource("data.sql")))
//        initializer.setDatabasePopulator(populator)
//        return initializer
//    }
}
