package micro.libs.avro

/**
 * kotlin Extension functions for Schema.
 * Source: https://github.com/avro-kotlin/avro4k/blob/master/src/main/kotlin/com/github/avrokotlin/avro4k/schema/schemas.kt
 * Reason: don't want to add full dependency on avro4k to this module.
 */
import org.apache.avro.Schema

fun Schema.containsNull(): Boolean =
    type == Schema.Type.UNION && types.any { it.type == Schema.Type.NULL }

fun Schema.extractNonNull(): Schema = when (this.type) {
    Schema.Type.UNION -> this.types.filter { it.type != Schema.Type.NULL }.let { if (it.size > 1) Schema.createUnion(it) else it[0] }
    else -> this
}

/**
 * given a union type schema, returns the (only) non-null branch's type of the union
 */
@Throws(IllegalArgumentException::class)
fun Schema.extractNonNullType(): Schema.Type = when (this.type) {
    Schema.Type.UNION -> this.types.filter { it.type != Schema.Type.NULL }
        .let {
            if (it.size > 1) {
                throw IllegalArgumentException("Schema ${this.name} has ${it.size} non-null union branches, where exactly 1 is expected")
            } else it[0].type
        }
    else -> this.type
}
