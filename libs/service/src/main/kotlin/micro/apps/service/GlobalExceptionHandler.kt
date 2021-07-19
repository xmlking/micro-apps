package micro.apps.service

// import org.springframework.boot.web.error.ErrorAttributeOptions
// import org.springframework.boot.web.reactive.error.DefaultErrorAttributes
// import org.springframework.boot.web.reactive.error.ErrorAttributes
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import mu.KotlinLogging
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange

// TODO: https://github.com/Juraji/albums-neo4j/blob/master/src/main/kotlin/nl/juraji/albums/api/exceptions/ControllerExceptionHandler.kt

private val logger = KotlinLogging.logger {}

@OptIn(ExperimentalSerializationApi::class)
@RestControllerAdvice
class GlobalExceptionHandler(private val messageSource: MessageSource /* private val errorAttributes: ErrorAttributes*/) {

    /*
    @Bean
    fun errorAttributes(): ErrorAttributes = DefaultErrorAttributes()

    private fun getErrorAttributes(request: ServerRequest): MutableMap<String, Any> {
        return errorAttributes.getErrorAttributes(request, ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION))
    }

    @ExceptionHandler(value = [DuplicateKeyException::class])
    protected fun handleDuplicateException(ex: DuplicateKeyException, exchange: ServerWebExchange): ResponseEntity<Any> {
        val errorAttributes = getErrorAttributes(exchange.request)
        val duplicateKey = "Duplicated key: "
        errorAttributes["status"] = CONFLICT.value()
        errorAttributes["message"] = duplicateKey + ex.message
        errorAttributes["error"] = CONFLICT.reasonPhrase
        return ResponseEntity.status(CONFLICT).body(errorAttributes)
    }
    */

    @ExceptionHandler(value = [RecordNotFoundException::class])
    fun handleNotFoundException(ex: RecordNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(NOT_FOUND).body(ErrorResponse(NOT_FOUND, ex.message.orEmpty()))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleBindingError(ex: HttpMessageNotReadableException, exchange: ServerWebExchange): ResponseEntity<ErrorResponse> {
        logger.atError().addArgument(ex).log("got HttpMessageNotReadableException:")
        val request = exchange.request
        val details = mapOf("path" to request.path.value())
        return ResponseEntity.badRequest().body(ErrorResponse(BAD_REQUEST, ex.message.orEmpty(), details))
    }

    @ExceptionHandler(SerializationException::class)
    fun handleSerializationError(ex: SerializationException, exchange: ServerWebExchange): ResponseEntity<ErrorResponse> {
        logger.atError().addArgument(ex).log("got serialization error:")
        val request = exchange.request
        val details = mapOf("path" to request.path.value())
        return ResponseEntity.internalServerError()
            .body(ErrorResponse(INTERNAL_SERVER_ERROR, ex.message.orEmpty(), details))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationError(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        logger.atError().addArgument(ex).log("got MethodArgumentNotValidException:")
        val vErrors = ex.bindingResult.fieldErrors.map {
            ValidationError(it.field, it.defaultMessage, it.rejectedValue)
        }.associateBy({ it.field }, { it })
        return ResponseEntity.badRequest().body(ErrorResponse(BAD_REQUEST, "Validation failed with ${ex.bindingResult.errorCount} error(s)", vErrors))
    }

    @ExceptionHandler(WebExchangeBindException::class)
    fun handleBindingError(
        ex: WebExchangeBindException,
        exchange: ServerWebExchange
    ): ResponseEntity<ErrorResponse> {
        logger.atError().addArgument(ex).log("got WebExchangeBindException:")
        val vErrors = ex.bindingResult.fieldErrors.map {
            ValidationError(it.field, it.defaultMessage, it.rejectedValue)
        }.associateBy({ it.field }, { it })
        return ResponseEntity.badRequest().body(ErrorResponse(ex.status, "Validation failed with ${ex.bindingResult.errorCount} error(s)", vErrors))
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException, exchange: ServerWebExchange): ResponseEntity<ErrorResponse> {
        logger.atError().addArgument(ex).log("got RuntimeException (TODO: Handle this error properly):")
        val request = exchange.request
        val details = mapOf("path" to request.path.value())
        return ResponseEntity.badRequest().body(ErrorResponse(INTERNAL_SERVER_ERROR, ex.message.orEmpty(), details))
    }
}

@ExperimentalSerializationApi
@Serializable
data class ValidationError(
    val field: String,
    val message: String?,
    val value: @Contextual @Serializable(with = DynamicLookupSerializer::class) Any?
)

@ExperimentalSerializationApi
@Serializable
data class ErrorResponse(
    val status: HttpStatus,
    val message: String,
    val details: Map<String, @Contextual @Serializable(with = DynamicLookupSerializer::class) Any>? = null
)
