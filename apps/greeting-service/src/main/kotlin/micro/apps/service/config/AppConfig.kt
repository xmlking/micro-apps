package micro.apps.service.config

import io.smallrye.config.ConfigMapping
import org.eclipse.microprofile.config.inject.ConfigProperty

@ConfigMapping(prefix = "greeting")
interface AppConfig {
    @ConfigProperty(name = "application.name")
    fun applicationName(): String?
}
