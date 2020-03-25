package micro.apps.greeting
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.servers.Server
import org.eclipse.microprofile.openapi.annotations.tags.Tag

@OpenAPIDefinition(
    tags = [
        Tag(name = "greeting", description = "greeting operations."),
        Tag(name = "fruits", description = "Operations related to fruits")
    ],
    info = Info(title = "Greeting API",
        description = "This API allows CRUD operations on Fruit data",
        version = "1.0.0",
        contact = Contact(name = "Sumo Demo")),
    servers = [
        Server(url = "http://localhost:8080")
    ]
)
@ApplicationScoped
@ApplicationPath("/api")
class GreetingApplication : Application()
