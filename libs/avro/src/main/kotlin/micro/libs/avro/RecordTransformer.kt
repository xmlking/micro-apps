@file:JvmName("RecordTransformer")

package micro.libs.avro

import micro.libs.avro.avpath.traverseRecord
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.avro.util.Utf8

typealias ValueTransformer<T> = (input: T) -> T

/**
 * traverse given `GenericRecord` along `avpath` and apply provided `ValueTransformer` to all matching fields.
 * transform only string fields.
 */
fun transform(source: GenericRecord, avpath: String, transformer: ValueTransformer<String>) {
    traverseRecord(source, avpath) { parent, suffix ->
        parent.get(suffix)?.let { original ->
            val fieldSchema = parent.schema.getField(suffix).schema()
            when (if (fieldSchema.type == Schema.Type.UNION) fieldSchema.extractNonNullType() else fieldSchema.type) {
                Schema.Type.STRING -> {
                    parent.put(suffix, Utf8(transformer(original.toString())))
                }
                else -> {
                    // TODO: error?
                    println("'$suffix' field is not String type")
                }
            }
        }
    }
}

fun transform(source: GenericRecord, avpaths: List<String>, transformer: ValueTransformer<String>) {
    avpaths.forEach {
        transform(source, it, transformer)
    }
}
