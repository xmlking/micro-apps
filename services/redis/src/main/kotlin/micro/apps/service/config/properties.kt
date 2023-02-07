package micro.apps.service.config

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated

/*
// Sample nested Properties
@ConstructorBinding
@ConfigurationProperties("blog")
@Validated
data class BlogProperties(var title: String, val banner: Banner) {
    data class Banner(val title: String? = null, val content: String)
}
*/

@ConstructorBinding
@ConfigurationProperties(prefix = "features.change-events")
@Validated
data class ChangeEventProperties(
    val enabled: Boolean = false,
    @field:NotBlank val keyspace: String = "events"
)

@ConstructorBinding
@ConfigurationProperties(prefix = "features.metrics")
@Validated
data class MetricsProperties(
    val enabled: Boolean = true,
    val collectors: String = "",
    val host: String = "",
    val tags: String = "",
    @field:NotBlank val prefix: String = "p8e_api"
)

/*
@ConstructorBinding
@ConfigurationProperties(prefix = "event.stream")
@Validated
data class EventStreamProperties(
    @field:NotNull val id: String,
    @field:NotNull val websocketUri: String,
    @field:NotNull val rpcUri: String,
    @field:NotNull val epoch: String,
    @field:NotNull @field:Length(max = 4, min = 1) val key: String,
)

@ConstructorBinding
@ConfigurationProperties(prefix = "elasticsearch")
@Validated
data class ElasticSearchProperties(
    @field:NotNull @field:Pattern(regexp = "\\d+") val host: String,
    @field:NotNull val port: String,
    @field:NotNull val prefix: String,
    @field:NotNull val username: String,
    @field:NotNull val password: String,
)
*/
