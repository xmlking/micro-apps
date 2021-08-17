@file:UseSerializers(DateAsLongSerializer::class, LocalDateTimeSerializer::class)

package micro.apps.service.domain.account

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.Transient
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import micro.apps.model.DateAsLongSerializer
import micro.apps.model.Gender
import micro.apps.model.LocalDateTimeSerializer
import micro.apps.model.Name
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.geo.Point
import org.springframework.data.redis.core.RedisHash
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import javax.validation.Valid
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Past
import javax.validation.constraints.Size

// import org.springframework.data.annotation.Transient
// import org.springframework.data.redis.core.index.GeoIndexed
// import kotlinx.serialization.Transient as STransient

/**
 * {@link PersonEntity} object stored inside a Redis {@literal HASH}. <br />
 * <br />
 * Sample (key = people:9b0ed8ee-14be-46ec-b5fa-79570aadb91d):
 *
 * <pre>
 * <code>
 * _class := micro.apps.service.domain.account.PersonEntity
 * id := 9b0ed8ee-14be-46ec-b5fa-79570aadb91d
 * dob := 343444333
 * avatar := https://www.gravatar.com/avatarr
 * email := sumo@demo.com
 * name.first := sumo
 * name.last := demo
 * gender := MALE
 * addresses.[0] := address:41436096-aabe-42fa-bd5a-9a517fbf0260
 * addresses.[1] := address:1973d8e7-fbd4-4f93-abab-a2e3a00b3f53
 * addresses.[2] := address:440b24c6-ede2-495a-b765-2d8b8d6e3995
 * </code>
 * </pre>
 *
 */
@ExperimentalSerializationApi
@Serializable
@RedisHash("people")
data class PersonEntity(
    @Id val id: String? = null,
    val name: Name?,
    @Reference
    val addresses: Set<AddressEntity>? = setOf(),
    val gender: Gender?,
    // @Serializable(with = DateAsLongSerializer::class) // @Polymorphic
    @field:Past(message = "invalid DOB: {value}")
    val dob: Date?,
    @field:Email(message = "Email should be valid")
    val email: String? = null,
    val phone: String? = null,
    val avatar: String? = "https://www.gravatar.com/avatarr"
) {
    fun addAddress(address: AddressEntity) {
        (this.addresses as HashSet).add(address)
    }
}

// HINT: spring-data need no-arg constructor or all properties nullable
@ExperimentalSerializationApi
@Serializable
@RedisHash("address")
data class AddressEntity(
    @Id val id: String? = null,
    val suite: String? = null,
    val street: String?,
    val city: String?,
    val state: String?,
    @field:Size(min = 5, max = 15)
    val code: String?,
    val country: String?,
    // @GeoIndexed
    @Serializable(with = PointSerializer::class) val location: Point? = null
)

@Serializable
@ExperimentalSerializationApi
data class PersonDto(
    @field:Valid
    val name: Name?,
    @field:Valid val addresses: Set<AddressDto>? = setOf(),
    val gender: Gender?,
    @field:Past(message = "invalid DOB")
    val dob: Date?,
    @field:Email(message = "Email should be valid")
    val email: String? = null,
    val phone: String? = null,
    val avatar: String? = "https://www.gravatar.com/avatar", // Optional
    @Transient val valid: Boolean = false // not serialized: explicitly transient
)

@ExperimentalSerializationApi
@Serializable
data class AddressDto(
    val suite: String? = null,
    @field:NotBlank
    val street: String?,
    @field:NotBlank
    val city: String?,
    @field:NotBlank
    val state: String?,
    @field:Size(min = 5, max = 16, message = "Postal Code must be between {min} and {max} characters")
    val code: String?,
    val country: String?,
    @Serializable(with = PointSerializer::class) val location: Point? = null
)

enum class Action { CREATED, UPDATED, DELETED }

@ExperimentalSerializationApi
@Serializable
class ChangeEvent(
    val id: String,
    val action: Action,
    // val at: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC"))
    val at: LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
)

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Point::class)
object PointSerializer : KSerializer<Point> {
    private val serializer = ListSerializer(Double.serializer())
    override val descriptor: SerialDescriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: Point) {
        encoder.encodeSerializableValue(serializer, listOf(value.x, value.y))
    }

    override fun deserialize(decoder: Decoder): Point {
        val (x, y) = decoder.decodeSerializableValue(serializer)
        return Point(x, y)
    }
}
