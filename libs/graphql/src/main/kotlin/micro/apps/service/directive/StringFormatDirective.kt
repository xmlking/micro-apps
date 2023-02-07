package micro.apps.service.directive

import graphql.schema.DataFetcherFactories
import graphql.schema.DataFetchingEnvironment
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment

abstract class StringFormatDirective : SchemaDirectiveWiring {

    abstract fun format(env: DataFetchingEnvironment, value: String): String

    override fun onField(
        env: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>
    ): GraphQLFieldDefinition {
        val field = env.element
        val parentType = env.fieldsContainer

        // build a data fetcher that transforms the given value to uppercase
        val originalFetcher = env.codeRegistry.getDataFetcher(parentType, field)
        val dataFetcher = DataFetcherFactories.wrapDataFetcher(
            originalFetcher
        ) { dfe, value ->
            if (value is String) {
                format(dfe, value)
            } else {
                value
            }
        }

        // now change the field definition to use the new uppercase data fetcher
        env.codeRegistry.dataFetcher(parentType, field, dataFetcher)
        return field
    }
}
