package micro.apps.pipeline

import org.apache.avro.generic.GenericRecord
import org.apache.beam.sdk.values.TupleTag

val successTag = object : TupleTag<GenericRecord>() {}
val errorTag = object : TupleTag<GenericRecord>() {}
