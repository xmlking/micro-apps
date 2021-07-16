package micro.apps.service.domain.account

import micro.apps.model.AddressNotFoundException
import micro.apps.model.PersonNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AccountExceptionHandler {

    @ExceptionHandler(value = [PersonNotFoundException::class, AddressNotFoundException::class])
    fun handleNotFoundExceptions(ex: Exception): ResponseEntity<Body> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Body(ex.message.orEmpty()))
    }

    data class Body(val message: String)
}
