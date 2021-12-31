@file:JvmName("RecordTraverser")
package micro.libs.avro.avpath

import org.apache.avro.generic.GenericRecord

// https://github.com/dcaoyuan/avpath
// https://github.com/AurityLab/kotlin-object-path

/**
 * mutate selected `field` using provided `mutator`
 * record: source record
 * selector: avpath e.g., `addresses.city`
 */
fun traverseRecord(record: GenericRecord, selector: String, mutator: (field: GenericRecord) -> Unit) {
    TODO()
}

fun select(record: GenericRecord, selector: String, mutator: (field: GenericRecord) -> Unit) {
    TODO()
}

fun update(record: GenericRecord, selector: String, mutator: (field: GenericRecord) -> Unit) {
    TODO()
}

fun delete(record: GenericRecord, selector: String, mutator: (field: GenericRecord) -> Unit) {
    TODO()
}

fun insert(record: GenericRecord, selector: String, mutator: (field: GenericRecord) -> Unit) {
    TODO()
}
