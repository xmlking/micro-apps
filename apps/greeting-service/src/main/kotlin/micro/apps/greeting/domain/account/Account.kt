package micro.apps.greeting.domain.account

import java.time.LocalDate

// @Entity
data class Account(var firstName: String?, var lastName: String? = null, var phoneNumber: String?, var birthdate: LocalDate? = null)
