package micro.apps.greeting

import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

// http://0.0.0.0:8080/api/v1/greeting/sumo

@Path("/v1/greeting")
@Produces(MediaType.APPLICATION_JSON)
class GreetingResource {

    @Inject
    @field: Default
    lateinit var greetingService: GreetingService


    @GET
    @Path("")
    fun hello() = "hello"

    @GET
    @Path("{name}")
    fun greeting(@PathParam("name") name: String?): String {
        return  if (null == name || name.isEmpty())   hello() else greetingService.greeting(name)
    }

}
