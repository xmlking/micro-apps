package micro.apps.service

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.web.bind.annotation.ResponseStatus

/*** Exceptions ***/

@ResponseStatus(value = NOT_FOUND, reason = "No such Record")
class RecordNotFoundException(override val message: String) : RuntimeException(message)

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Duplicate Id")
// based on import org.springframework.dao.DuplicateKeyException
class DuplicateKeyException(id: String, type: String?) : RuntimeException("A ${type ?: "record"} with id $id already exist")

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS, reason = "Rate limit exceeded")
class RateLimitExceededException : RuntimeException("Rate limit exceeded")
