@file:UseSerializers(DateAsLongSerializer::class, LocalDateTimeSerializer::class)

package micro.apps.service

import com.redis.om.spring.annotations.AutoComplete
import com.redis.om.spring.annotations.Bloom
import com.redis.om.spring.annotations.Document
import com.redis.om.spring.annotations.DocumentScore
import com.redis.om.spring.annotations.Indexed
import com.redis.om.spring.annotations.Searchable
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.UseSerializers
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import micro.apps.model.DateAsLongSerializer
import micro.apps.model.Gender
import micro.apps.model.LocalDateTimeSerializer
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.geo.Point
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

// import org.springframework.data.annotation.Transient
// import org.springframework.data.redis.core.index.GeoIndexed
// import kotlinx.serialization.Transient as STransient

@Document("person")
data class Person(
    @Id val id: String? = null,
    @DocumentScore
    val score: Double,
    @Indexed
    val name: Name,
    val addresses: Set<Address> = setOf(),
    @Indexed
    val address: Address?,
    val gender: Gender?,
    // @Serializable(with = DateAsLongSerializer::class) // @Polymorphic
    @Indexed
    val dob: Date?,
    @Indexed
    @Bloom(name = "bf_person_email", capacity = 100000, errorRate = 0.001)
    var email: String,

    val phone: String? = null,
    val avatar: String = "https://www.gravatar.com/avatarr",

    @CreatedDate
    val createdDate: Date? = null,
    @LastModifiedDate
    val lastModifiedDate: Date? = null
) {
    fun addAddress(address: Address) {
        (this.addresses as HashSet).add(address)
    }
}

// @Serializable
// @ExperimentalSerializationApi
data class Name(
    @Searchable
    @field:NotNull
    @field:Pattern(regexp = "[A-Za-z0-9_]+", message = "FirstName must contain only letters and numbers")
    @field:Size(min = 4, max = 26, message = "FirstName must be between {min} and {max} characters")
    val first: String?,

    @Searchable
    @field:NotBlank
    @field:Pattern(regexp = "[A-Za-z0-9_]+", message = "LastName must contain only letters and numbers")
    val last: String?,
    val title: String? = null
)

// HINT: spring-data need no-arg constructor or all properties nullable
// @Serializable
// @ExperimentalSerializationApi
data class Address(
    @Id val id: String? = null,
    val suite: String? = null,
    @Searchable(sortable = true, nostem = true, weight = 20.0)
    val street: String,
    @Indexed
    val city: String,
    @AutoComplete
    val state: String,
    @AutoComplete
    val code: String,
    @AutoComplete
    val country: String,
    @Indexed
    @Serializable(with = PointSerializer::class)
    val location: Point? = null
)

// @Serializable
// @ExperimentalSerializationApi
// data class PersonDto(
//    @field:Valid
//    val name: Name?,
//    @field:Valid val addresses: Set<AddressDto>? = setOf(),
//    val gender: Gender?,
//    @field:Past(message = "invalid DOB: {value}")
//    val dob: Date?,
//    @field:Email(message = "Email should be valid")
//    val email: String? = null,
//    val phone: String? = null,
//    val avatar: String? = "https://www.gravatar.com/avatar", // Optional
//    @Transient val valid: Boolean = false // not serialized: explicitly transient
// )

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
    @field:NotBlank
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
