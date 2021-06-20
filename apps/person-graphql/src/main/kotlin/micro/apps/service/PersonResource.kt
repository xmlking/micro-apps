package micro.apps.service

import graphql.GraphQL
import graphql.schema.GraphQLSchema
import org.eclipse.microprofile.graphql.DefaultValue
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query
import org.eclipse.microprofile.graphql.Source
import org.jboss.logging.Logger
import java.util.Random
import java.util.stream.Collectors
import javax.enterprise.event.Observes
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.validation.constraints.Min

@GraphQLApi
class PersonResource {
    @Inject
    @field: Default
    lateinit var log: Logger

    @Inject
    @field: Default
    lateinit var scoreService: ScoreService

    @Inject
    @field: Default
    lateinit var personService: PersonService

    @Query // @Timed(name = "personTimer", description = "How long does it take to get a Person.", unit = MetricUnits.NANOSECONDS)
    // @Counted(name = "personCount", description = "How many times did we ask for Person.")
    fun getPerson(id: Long): Person {
        log.error(id)
        return personService.getPerson(id)
    }

    @get:Query
    val people: List<Person>
        get() = personService.people

    // @RolesAllowed("admin")
    @Throws(ScoresNotAvailableException::class)
    fun getScores(@Source person: Person): List<Score> {
        return scoreService.getScores(person.idNumber!!)
        // throw new ScoresNotAvailableException("Scores for person [" + person.getId() + "] not avaialble");
    }

    // Batch
    @Throws(ScoresNotAvailableException::class)
    fun getScores(@Source people: List<Person>): List<List<Score>> {
        val idNumbers = people.stream().map { p: Person -> p.idNumber }.collect(Collectors.toList())
        return scoreService.getScores(idNumbers as List<String>)
        // throw new ScoresNotAvailableException("Scores for person [" + p.getId() + "] not avaialble");
    }

    @Query
    fun getRandomNumber(seed: @Min(10) Long): Int {
        val random = Random(seed)
        return random.nextInt()
    }

    // Mutations
    @Mutation
    fun updatePerson(person: Person): Person {
        return personService.updateOrCreate(person)
    }

    @Mutation
    fun deletePerson(id: Long): Person? {
        return personService.delete(id)
    }

    // Default values
    @Query
    fun getPersonsWithSurname(
        @DefaultValue("Doyle") surname: String?
    ): List<Person> {
        return personService.getPeopleWithSurname(surname)
    }

    fun leakyAbstraction(@Observes builder: GraphQLSchema.Builder): GraphQLSchema.Builder {
        System.err.println(">>>>>>> Here we leak while building the schema")
        // Do what you have to do
        return builder
    }

    fun leakyAbstraction(@Observes builder: GraphQL.Builder): GraphQL.Builder {
        System.err.println(">>>>>>> Here we leak while building graphQL")
        // Do what you have to do
        return builder
    }
}
