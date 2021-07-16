package micro.apps.service.domain.account

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.ExperimentalSerializationApi
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

private val logger = KotlinLogging.logger {}

@ExperimentalSerializationApi
@CrossOrigin
@RestController
@RequestMapping("/account")
class AccountController(private val accountService: AccountService) {

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    suspend fun createPerson(@Valid person: PersonDto): PersonEntity = accountService.createPerson(person)

    @GetMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun getPersonById(@PathVariable id: String): PersonEntity = accountService.getPerson(id)

    @PutMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.OK)
    suspend fun updatePerson(@PathVariable id: String, @Validated person: PersonDto): PersonEntity {
        logger.atDebug().addKeyValue("id", id).log("controller updatePerson")

        return accountService.updatePerson(id, person)
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    suspend fun getPeople(): Flow<PersonEntity> = accountService.getAllPeople()

    @DeleteMapping(value = ["/{id}"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deletePerson(id: String) = accountService.deletePerson(id)

}

