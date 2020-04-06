package micro.apps.greeting

import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import micro.apps.core.model.Greeting
import org.eclipse.microprofile.metrics.MetricUnits
import org.eclipse.microprofile.metrics.annotation.Counted
import org.eclipse.microprofile.metrics.annotation.Gauge
import org.eclipse.microprofile.metrics.annotation.Timed

@Path("/v1/greeting")
@Produces(MediaType.APPLICATION_JSON)
@Counted(name = "performedChecks", description = "How many primality checks have been performed.")
class GreetingResource {
    @Inject
    @field: Default
    lateinit var greetingService: GreetingService

    @GET
    @Path("")
    @Gauge(name = "hello", unit = MetricUnits.NONE, description = "How many anonymous")
    fun hello() = Greeting("hello")

    @GET
    @Path("{name}")
    @Timed(name = "checksTimer", description = "A measure of how long it takes to perform the primality test.", unit = MetricUnits.MILLISECONDS)
    fun greeting(@PathParam("name") name: String?): Greeting {
        return if (null == name || name.isEmpty()) hello() else greetingService.greeting(name)
    }
}
