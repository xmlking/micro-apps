package micro.apps.pipeline.transforms

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroDecodeFormat
import com.github.avrokotlin.avro4k.io.AvroEncodeFormat
import com.google.common.collect.ImmutableMap
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Person
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.SimpleFunction
import java.io.ByteArrayOutputStream

@ExperimentalSerializationApi
class PubsubToPerson : SimpleFunction<PubsubMessage, Person>() {
    override fun apply(input: PubsubMessage): Person {
        return Avro.default.openInputStream(Person.serializer()) {
            decodeFormat =
                AvroDecodeFormat.Data(Avro.default.schema(Person.serializer())) // Other Options: AvroDecodeFormat.Binary(), AvroDecodeFormat.Json()
        }.from(input.payload).nextOrThrow()
    }
}

@ExperimentalSerializationApi
class PersonToPubsub(private val attributes: Map<String, String> = ImmutableMap.of()) :
    SimpleFunction<Person, PubsubMessage>() {
    private val serializer = Person.serializer()
    private val personSchema = Avro.default.schema(Person.serializer())

    override fun apply(input: Person): PubsubMessage {
        val baos = ByteArrayOutputStream()
        Avro.default.openOutputStream(serializer) {
            encodeFormat = AvroEncodeFormat.Data() // Other Options: AvroEncodeFormat.Binary(), AvroEncodeFormat.Json()
            this.schema = personSchema
        }.to(baos).write(input).close()
        val data = baos.toByteArray()
        return PubsubMessage(data, attributes)
    }
}
