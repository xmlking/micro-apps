package micro.apps.service.config

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.validation.rules.OnValidationErrorStrategy
import graphql.validation.rules.ValidationRules
import graphql.validation.schemawiring.ValidationSchemaWiring
import micro.apps.service.directive.CapitalizeDirective
import micro.apps.service.directive.LowercaseDirective
import micro.apps.service.directive.SchemaDirective
import micro.apps.service.directive.TrimDirective
import micro.apps.service.directive.UppercaseDirective
import micro.apps.service.validation.EmailRule
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

@Configuration(proxyBeanMethods = false)
class GraphqlConfig {

    /**
     * GraphQLScalarTypes
     */
    @Bean
    fun dateTimeScalarType(): GraphQLScalarType {
        return ExtendedScalars.DateTime
    }

    @Bean
    fun dateScalarType(): GraphQLScalarType {
        return ExtendedScalars.Date
    }

    @Bean
    fun jsonScalarType(): GraphQLScalarType {
        return ExtendedScalars.Json
    }

    @Bean
    fun objectScalarType(): GraphQLScalarType {
        return ExtendedScalars.Object
    }

    @Bean
    fun bigDecimalScalarType(): GraphQLScalarType {
        return ExtendedScalars.GraphQLBigDecimal
    }

    @Bean
    fun urlScalarType(): GraphQLScalarType {
        return ExtendedScalars.Url
    }

    @Bean
    fun uuidScalarType(): GraphQLScalarType {
        return ExtendedScalars.UUID
    }

    @Bean
    fun localeScalarType(): GraphQLScalarType {
        return ExtendedScalars.Locale
    }

    /**
     * Directives
     */
    @Bean
    fun uppercaseDirective(): SchemaDirective {
        return SchemaDirective("uppercase", UppercaseDirective())
    }

    @Bean
    fun lowercaseDirective(): SchemaDirective {
        return SchemaDirective("lowercase", LowercaseDirective())
    }

    @Bean
    fun capitalizeDirective(): SchemaDirective {
        return SchemaDirective("capitalize", CapitalizeDirective())
    }

    @Bean
    fun trimDirective(): SchemaDirective {
        return SchemaDirective("trim", TrimDirective())
    }

    /**
     * Validation extensions
     */
    @Bean
    fun validationSchemaDirective(): SchemaDirectiveWiring {
        val validationRules = ValidationRules.newValidationRules()
            .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
            .addRule(EmailRule())
            .build()
        return ValidationSchemaWiring(validationRules)
    }

    @Bean
    fun graphqlConfigurer(
        graphQLScalarTypes: List<GraphQLScalarType>,
        validationSchemaDirective: SchemaDirectiveWiring,
        schemaDirectives: List<SchemaDirective>
    ): RuntimeWiringConfigurer {
        return RuntimeWiringConfigurer { builder ->
            graphQLScalarTypes.forEach { builder.scalar(it) }
            builder.directiveWiring(validationSchemaDirective)
            schemaDirectives.forEach { builder.directive(it.name, it.directive) }
        }
    }

    /**
     * Serializations
     */
    @Bean
    fun jsonCustomizer(): Jackson2ObjectMapperBuilderCustomizer {
        return Jackson2ObjectMapperBuilderCustomizer { builder: Jackson2ObjectMapperBuilder ->
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            builder.featuresToDisable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            builder.modules(JavaTimeModule())
        }
    }
}
