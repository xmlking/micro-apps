package micro.apps.service.exception

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import jakarta.validation.ConstraintViolationException
import org.springframework.context.MessageSource
import org.springframework.graphql.client.FieldAccessException
import org.springframework.graphql.execution.DataFetcherExceptionResolverAdapter
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
// import jakarta.persistence.EntityNotFoundException
// import org.springframework.security.access.AccessDeniedException

@Component
class CustomGraphqlExceptionResolver(
    private val messageSource: MessageSource
) : DataFetcherExceptionResolverAdapter() {

    override fun resolveToSingleError(ex: Throwable, env: DataFetchingEnvironment): GraphQLError? {
        val locale = env.locale

        return when (ex) {
            is ServerException -> {
                GraphqlErrorBuilder.newError()
                    .errorType(ex.errorType)
                    .message(ex.message)
                    .path(env.executionStepInfo.path)
                    .location(env.field.sourceLocation)
                    .build()
            }

            is FieldAccessException -> {
                val responseError = ex.response.errors.first()
                val extensions = responseError.extensions
                val classification = extensions["classification"]
                val errorType = ErrorType.values()
                    .firstOrNull { it.name == classification } ?: ErrorType.INTERNAL_ERROR
                GraphqlErrorBuilder.newError(env)
                    .message(responseError.message).errorType(errorType).build()
            }

            is ResourceNotFoundException -> {
                val errorMessage = messageSource.getMessage(ex.message!!, ex.args, locale)
                GraphqlErrorBuilder.newError(env)
                    .message(errorMessage).errorType(ErrorType.NOT_FOUND).build()
            }

            is DuplicateEntryException -> {
                val errorMessage = messageSource.getMessage(ex.message!!, ex.args, locale)
                GraphqlErrorBuilder.newError(env)
                    .message(errorMessage).errorType(ErrorType.INTERNAL_ERROR).build()
            }

            is ValidationException -> {
                val errorMessage = messageSource.getMessage(ex.message!!, ex.args, locale)
                badRequest(env, errorMessage)
            }
            else -> super.resolveToSingleError(ex, env)
        }
    }

    override fun resolveToMultipleErrors(
        ex: Throwable,
        env: DataFetchingEnvironment
    ): List<GraphQLError>? {
        return when (ex) {
            is ConstraintViolationException -> {
                return ex.constraintViolations.map {
                    val validatedPath = it.propertyPath.map { node -> node.name }
                    GraphqlErrorBuilder.newError(env)
                        .message("${it.propertyPath}: ${it.message}")
                        .errorType(graphql.ErrorType.ValidationError)
                        .extensions(
                            mapOf(
                                "validatedPath" to validatedPath
                            )
                        )
                        .build()
                }
            }

            else -> super.resolveToMultipleErrors(ex, env)
        }
    }

    private fun badRequest(env: DataFetchingEnvironment, errorMessage: String): GraphQLError? {
        return GraphqlErrorBuilder.newError(env)
            .message(errorMessage).errorType(ErrorType.BAD_REQUEST).build()
    }
}
