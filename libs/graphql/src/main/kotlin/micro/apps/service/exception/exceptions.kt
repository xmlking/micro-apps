package micro.apps.service.exception

import org.springframework.graphql.execution.ErrorType

sealed class ServerException(
    val code: Int,
    override val message: String,
    open val errorType: ErrorType
) : RuntimeException()

data class NotFoundException(
    override val message: String = "The requested information does not exist.",
    override val errorType: ErrorType = ErrorType.NOT_FOUND
) : ServerException(404, message, errorType)

data class UnAuthorizedException(
    override val message: String = "Invalid authentication information.",
    override val errorType: ErrorType = ErrorType.UNAUTHORIZED
) : ServerException(401, message, errorType)

open class GraphqlException(message: String?, var args: Array<Any>? = null) : RuntimeException(message)
open class ValidationException(message: String?, args: Array<Any>? = null) : GraphqlException(message, args)
class DuplicateEntryException(message: String?, args: Array<Any>? = null) : GraphqlException(message, args)
class ResourceNotFoundException(message: String?, args: Array<Any>? = null) : GraphqlException(message, args)
