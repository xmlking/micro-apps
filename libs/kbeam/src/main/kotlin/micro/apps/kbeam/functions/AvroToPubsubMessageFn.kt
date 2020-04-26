package micro.apps.kbeam.functions

import com.google.common.collect.ImmutableMap
import java.io.ByteArrayOutputStream
import java.io.IOException
import org.apache.avro.generic.GenericDatumWriter
import org.apache.avro.generic.IndexedRecord
import org.apache.avro.io.BinaryEncoder
import org.apache.avro.io.DatumWriter
import org.apache.avro.io.EncoderFactory
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.SimpleFunction

class AvroToPubsubMessageFn(val attributes: Map<String, String> = ImmutableMap.of()) : SimpleFunction<IndexedRecord, PubsubMessage>() {
    override fun apply(input: IndexedRecord): PubsubMessage {
        return try {
            val datumWriter: DatumWriter<IndexedRecord> = GenericDatumWriter(input.getSchema())
            val out = ByteArrayOutputStream()
            val encoder: BinaryEncoder = EncoderFactory.get().binaryEncoder(out, null)
            datumWriter.write(input, encoder)
            encoder.flush()
            val result: ByteArray = out.toByteArray()
            out.close()
            PubsubMessage(result, attributes)
        } catch (e: IOException) {
            throw e
        }
    }
}
class PubsubMessageToSpecificFn<T>() : SimpleFunction<PubsubMessage, T>() {
    override fun apply(input: PubsubMessage): T {
        TODO()
    }
}
