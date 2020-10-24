package micro.apps.greeting.config

import io.quarkus.arc.config.ConfigProperties
import org.eclipse.microprofile.config.inject.ConfigProperty

@ConfigProperties(prefix = "greeting")
interface AppConfig {
    @ConfigProperty(name = "application.name")
    fun applicationName(): String?
}
