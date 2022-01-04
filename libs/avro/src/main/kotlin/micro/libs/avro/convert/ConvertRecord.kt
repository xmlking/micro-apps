@file:JvmName("ConvertRecord")

package micro.libs.avro.convert

import org.apache.avro.Schema
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericFixed
import org.apache.avro.generic.GenericRecord
import java.nio.ByteBuffer

/**
 *  convert `GenericRecord` to `Map` for easy access by key.
 */
fun convertRecord(record: GenericRecord): Map<String, Any?> {
    val map = HashMap<String, Any?>()
    val schema = record.schema
    for (field in schema.fields) {
        map[field.name()] = convertAvro(record.get(field.pos()), field.schema())
    }
    return map
}

private fun convertAvro(data: Any?, schema: Schema): Any? {
    when (schema.type) {
        Schema.Type.RECORD -> return convertRecord(data as GenericRecord)
        Schema.Type.MAP -> {
            val value = HashMap<String, Any?>()
            val valueType = schema.valueType
            for ((key, value1) in data as Map<*, *>) {
                value[key.toString()] = convertAvro(value1, valueType)
            }
            return value
        }
        Schema.Type.ARRAY -> {
            val origList = data as List<*>
            val itemType = schema.elementType
            val list = ArrayList<Any?>(origList.size)
            for (orig in origList) {
                list.add(convertAvro(orig, itemType))
            }
            return list
        }
        Schema.Type.UNION -> {
            val type = GenericData().resolveUnion(schema, data)
            return convertAvro(data, schema.types[type])
        }
        Schema.Type.BYTES -> return (data as ByteBuffer).array()
        Schema.Type.FIXED -> return (data as GenericFixed).bytes()
        Schema.Type.ENUM, Schema.Type.STRING -> return data.toString()
        Schema.Type.INT, Schema.Type.LONG, Schema.Type.DOUBLE, Schema.Type.FLOAT, Schema.Type.BOOLEAN, Schema.Type.NULL -> return data
        else -> throw IllegalArgumentException("Cannot parse field type " + schema.type)
    }
}
