package micro.apps.service.domain.account

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class PersonDto(
    @Pattern(regexp = "[A-Za-z0-9_]+", message = "Username must contain only letters and numbers")
    @Size(min = 4, max = 16, message = "Username must be between 4 and 16 characters")
    val username: String,

    @Email(message = "Email should be valid")
    val email: String,

    @NotBlank
    @Size(min = 4, max = 16, message = "Password must be between 4 and 16 characters")
    val password: String
)
