@file:JvmName("RecordTraverser")

package micro.libs.avro.avpath

import org.apache.avro.Schema
import org.apache.avro.generic.GenericArray
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericRecord

// https://github.com/dcaoyuan/avpath
// https://github.com/AurityLab/kotlin-object-path

/**
 * Apply provided transformer function on each matching sub-record for `avpath`
 * record: source record
 * avpath: e.g., `addresses.city` `altNames.title`
 */
typealias Transformer = (parent: GenericRecord, suffix: String) -> Unit

fun traverseRecord(data: GenericRecord, avpath: String, transformer: Transformer) {
    val pathStack = ArrayDeque(avpath.split(".").filter { it.isNotEmpty() })
    require(pathStack.isNotEmpty()) { "avpath: '$avpath' is empty" }
    traverseRecord(data,pathStack, transformer)
}

private fun traverseRecord(data: GenericRecord, pathStack: ArrayDeque<String>, transformer: Transformer) {
    when (pathStack.size) {
        // HINT: won't check if parent is null
        1 -> transformer(data, pathStack.last())
        else -> {
            val path = pathStack.removeFirst()
            val fieldData = data.get(path)
            fieldData?.let {
                val fieldSchema = data.schema.getField(path).schema()
                traverseAny(it, fieldSchema, pathStack, transformer)
            }
        }
    }
}

private fun traverseAny(data: Any, schema: Schema, pathStack: ArrayDeque<String>, transformer: Transformer) {
    when (schema.type) {
        Schema.Type.RECORD -> {
            return traverseRecord(data as GenericRecord, pathStack, transformer)
        }
        Schema.Type.ARRAY -> {
            // TODO if path is like: `altNames[8]` filter array by index i.e., `8`
            val itemType = schema.elementType
            for (item in data as GenericArray<*>) {
                item?.let {
                    traverseAny(item, itemType, pathStack, transformer)
                }
            }
        }
        Schema.Type.MAP -> {
            // TODO if path is like: `family["spouse"]` filter map by key i.e., `spouse`
            val valueType = schema.valueType
            for ((_, value) in data as Map<*, *>) {
                value?.let {
                    traverseAny(value, valueType, pathStack, transformer)
                }
            }
        }
        Schema.Type.UNION -> {
            val type = GenericData().resolveUnion(schema, data)
            traverseAny(data, schema.types[type], pathStack, transformer)
        }
        else -> throw IllegalArgumentException("Field type: ${schema.type} is not supported by traverseRecord(...)")
    }
}
