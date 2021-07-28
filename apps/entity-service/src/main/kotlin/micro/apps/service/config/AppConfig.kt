package micro.apps.service.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        ChangeEventProperties::class,
        MetricsProperties::class,
    ]
)
class AppConfig
