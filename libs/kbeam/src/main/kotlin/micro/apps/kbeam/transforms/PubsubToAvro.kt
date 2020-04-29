package micro.apps.kbeam.transforms

/* ktlint-disable no-wildcard-imports */
import com.google.common.collect.ImmutableMap
import java.io.ByteArrayOutputStream
import org.apache.avro.Schema
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.GenericRecord
import org.apache.avro.generic.IndexedRecord
import org.apache.avro.io.*
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.SimpleFunction

/* ktlint-enable no-wildcard-imports */

class PubsubToAvro(private val schema: Schema) : SimpleFunction<PubsubMessage, GenericRecord>() {
    override fun apply(input: PubsubMessage): GenericRecord {
        val datumReader: DatumReader<GenericRecord> = GenericDatumReader(schema)
        val decoder = DecoderFactory.get().binaryDecoder(input.payload, null)
        return datumReader.read(null, decoder)
    }
}

class PubsubToSpecific<T>() : SimpleFunction<PubsubMessage, T>() {
    override fun apply(input: PubsubMessage): T {
        TODO()
    }
}

class AvroToPubsub(private val attributes: Map<String, String> = ImmutableMap.of()) : SimpleFunction<IndexedRecord, PubsubMessage>() {
    override fun apply(input: IndexedRecord): PubsubMessage {
        return ByteArrayOutputStream()
            .use {
                val datumWriter: DatumWriter<IndexedRecord> = GenericDatumWriter(input.schema)
                val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(it, null)
                datumWriter.write(input, encoder)
                encoder.flush()
                PubsubMessage(it.toByteArray(), attributes)
            }
    }
}
