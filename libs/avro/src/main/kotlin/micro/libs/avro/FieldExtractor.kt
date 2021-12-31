@file:JvmName("FieldExtractor")

package micro.libs.avro


import micro.apps.core.Predicate
import micro.apps.core.memorize
import micro.libs.avro.traversal.SchemaVisitor
import micro.libs.avro.traversal.traverseSchema
import org.apache.avro.Schema
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

/**
 * traverse the given `Schema` and return all the fields that sanctify provided `Predicate`
 */
fun extractFields(
    schema: Schema,
    predicate: Predicate<Schema.Field> = { _ -> true }
): List<Pair<String, Schema.Field>> {
    val infos = mutableListOf<Pair<String, Schema.Field>>()
    traverseSchema(schema,
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
 * Cached extractFields function
 * traverse the given `Schema` and return all the fields that sanctify provided `Predicate`
 * cache results only based on **Schema**. won't respect filterFn changes
 */
private val CACHE = ConcurrentHashMap<Schema, List<Pair<String, Schema.Field>>>()
fun cachedExtractFields(schema: Schema, predicate: Predicate<Schema.Field> = { _ -> true }): List<Pair<String, Schema.Field>> {
    return CACHE.computeIfAbsent(schema) { _ -> extractFields(schema, predicate) }
}


/**
 * given a union type field, returns the (only) non-null branch's type of the union
 */
@Throws(IllegalArgumentException::class)
fun unwrapUnionType(field: Schema.Field): Schema.Type {
    val fieldSchema = field.schema()
    if (Schema.Type.UNION != fieldSchema.type) {
        return fieldSchema.type //field is not a union
    }

    val nonNullBranches = fieldSchema.types.stream().filter { schema: Schema -> Schema.Type.NULL != schema.type }
        .collect(Collectors.toList())
    require(nonNullBranches.size == 1) {
        "field ${field.name()} has ${nonNullBranches.size} non-null union branches, where exactly 1 is expected"
    }
    return nonNullBranches[0].type
}
