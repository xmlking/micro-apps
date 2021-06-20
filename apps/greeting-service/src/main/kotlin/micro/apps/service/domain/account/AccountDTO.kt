package micro.apps.service.domain.account

import java.time.LocalDate

data class AccountDTO(var firstName: String?, var lastName: String? = null, var phone: String?, var birthdate: LocalDate? = null)
