package micro.apps.greeting.domain.fruit

import java.util.Collections
import java.util.function.Predicate
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import kotlin.collections.LinkedHashMap
import micro.apps.model.Fruit
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.eclipse.microprofile.openapi.annotations.tags.Tags

@Path("/fruits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tags(
    Tag(name = "FruitResource", description = "Fruit API")
)
class FruitResource {
    private val fruits: MutableSet<Fruit> =
        Collections.newSetFromMap(Collections.synchronizedMap(LinkedHashMap<Fruit, Boolean>()))

    init {
        fruits.add(Fruit("Apple", "Winter fruit"))
        fruits.add(Fruit("Pineapple", "Tropical fruit"))
    }

    @GET
    fun list(): Set<Fruit> {
        return fruits
    }

    @POST
    fun add(fruit: Fruit): Set<Fruit> {
        fruits.add(fruit)
        return fruits
    }

    @DELETE
    fun delete(fruit: Fruit): Set<Fruit> {
        fruits.removeIf(Predicate<Fruit> { existingFruit: Fruit -> existingFruit.name.contentEquals(fruit.name) })
        return fruits
    }
}
