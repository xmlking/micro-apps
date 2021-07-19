package micro.apps.service.domain.account

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

private val logger = KotlinLogging.logger {}

@ExperimentalSerializationApi
@CrossOrigin
@RestController
@RequestMapping("/account")
class AccountController(private val accountService: AccountService) {
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun createPerson(@Valid @RequestBody person: PersonDto): PersonEntity = accountService.createPerson(person)

    @GetMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun getPersonById(@PathVariable id: String): PersonEntity = accountService.getPerson(id)

    @PutMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun updatePerson(@PathVariable id: String, @Valid @RequestBody person: PersonDto): PersonEntity {
        logger.atDebug().addKeyValue("id", id).log("controller updatePerson")
        return accountService.updatePerson(id, person)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    suspend fun getPeople(): Flow<PersonEntity> = accountService.getAllPeople()

    @DeleteMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deletePerson(@PathVariable id: String) = accountService.deletePerson(id)

    @PatchMapping("/{addressId}/link/{personId}")
    suspend fun addAddressToPerson(@PathVariable addressId: String, @PathVariable personId: String): PersonEntity = accountService.addAddressToPerson(addressId, personId)
}
