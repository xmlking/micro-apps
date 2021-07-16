package micro.apps.service.domain.account

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import micro.apps.model.Gender
import micro.apps.model.Name
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Reference
import org.springframework.data.geo.Point
import org.springframework.data.redis.core.RedisHash
import javax.validation.constraints.Email
import javax.validation.constraints.Min
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
 * age := 34
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
    val name: Name,
    @Reference
    val addresses: Set<AddressEntity>? = setOf(),
    val gender: Gender,
    @Min(value = 18, message = "age must be at least {value}")
    val age: Int,
    @Email(message = "Email should be valid")
    val email: String?,
    val phone: String?,
    val avatar: String? = "https://www.gravatar.com/avatarr"
)


// HINT: spring-data need no-arg constructor or all properties nullable
@ExperimentalSerializationApi
@Serializable
@RedisHash("address")
data class AddressEntity(
    @Id val id: String? = null,
    val suite: String?,
    val street: String?,
    val city: String?,
    val state: String?,
    @Size(min = 5, max = 15)
    val code: String?,
    val country: String?,
    // @GeoIndexed
    @Serializable(with = PointSerializer::class) @Contextual val location: Point?
)

// TODO
@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Point::class)
object PointSerializer : KSerializer<Point> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Point", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: Point) = encoder.encodeString("${value.x}-${value.y}")
    override fun deserialize(decoder: Decoder): Point = Point(1.1,1.1)
}
