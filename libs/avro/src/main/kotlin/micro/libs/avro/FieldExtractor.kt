@file:JvmName("FieldExtractor")

package micro.libs.avro

import micro.apps.core.Predicate
import micro.apps.core.and
import micro.apps.core.memorize
import micro.libs.avro.schema.SchemaVisitor
import micro.libs.avro.schema.traverseSchema
import org.apache.avro.Schema

/**
 * traverse the given `Schema` and return all the fields that sanctify provided `Predicate`
 */
fun extractFields(
    schema: Schema,
    predicate: Predicate<Schema.Field> = { _ -> true }
): List<Pair<String, Schema.Field>> {
    val infos = mutableListOf<Pair<String, Schema.Field>>()
    traverseSchema(
        schema,
        object : SchemaVisitor {
            override fun visitSchema(schema: Schema) {}

            override fun visitField(parent: Schema, field: Schema.Field, path: String) {
                if (predicate(field)) {
                    infos.add(path to field)
                }
            }
        }
    )
    return infos
}

/**
 * Memorized extractFields function
 * traverse the given `Schema` and return all the fields that sanctify provided `Predicate`
 */
val memorizedExtractFields = ::extractFields.memorize()

/**
 * Predicates: which are serializable and works with `memorize()`
 */
val isLeafField: Predicate<Schema.Field> = { field ->
    val schema = field.schema()
    when (if (schema.type == Schema.Type.UNION) schema.extractNonNullType() else schema.type) {
        // when (if (field.schema().type == Schema.Type.UNION) unwrapUnionType(field) else field.schema().type) {
        Schema.Type.MAP, Schema.Type.ARRAY, Schema.Type.RECORD, Schema.Type.NULL -> false
        else -> true
    }
}
val isConfidential: Predicate<Schema.Field> = { field -> field.getProp("confidential")?.let { true } ?: false }
val isLeafConfidential = (isLeafField and isConfidential)
