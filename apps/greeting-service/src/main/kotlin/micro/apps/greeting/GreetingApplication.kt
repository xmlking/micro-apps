package micro.apps.greeting

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application
import org.eclipse.microprofile.openapi.annotations.Components
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme
import org.eclipse.microprofile.openapi.annotations.servers.Server
import org.eclipse.microprofile.openapi.annotations.tags.Tag

fun main(vararg args: String) {
    println("ARGS: ${args.size}")
    Quarkus.run(GreetingApplication::class.java, *args)
}

@QuarkusMain
@OpenAPIDefinition(
    tags = [
        Tag(name = "greeting", description = "greeting operations."),
        Tag(name = "fruits", description = "Operations related to fruits")
    ],
    info = Info(
        title = "Greeting API",
        description = "This API allows CRUD operations on Fruit data",
        version = "1.0.0",
        contact = Contact(name = "Sumo Demo", email = "sumo@demo.com")
    ),
    servers = [
        Server(url = "http://localhost:8080")
    ],
    components = Components(
        securitySchemes = [
            SecurityScheme(
                securitySchemeName = "bearerAuth",
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT"
            )
        ]
    ),
    security = [
        SecurityRequirement(name = "bearerAuth", scopes = [])
    ]
)
@ApplicationScoped
@ApplicationPath("/api")
class GreetingApplication : QuarkusApplication, Application() {
    override fun run(vararg args: String?): Int {
        Quarkus.waitForExit()
        return 0
    }
}
