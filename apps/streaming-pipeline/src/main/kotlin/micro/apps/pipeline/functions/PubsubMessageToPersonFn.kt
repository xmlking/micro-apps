package micro.apps.pipeline.functions

import com.google.common.collect.ImmutableMap
import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import java.io.ByteArrayOutputStream
import micro.apps.model.Person
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.SimpleFunction

class PubsubMessageToPersonFn() : SimpleFunction<PubsubMessage, Person>() {
    override fun apply(input: PubsubMessage): Person {
        return Avro.default.openInputStream(Person.serializer()) {
            format = AvroFormat.BinaryFormat
            writerSchema = Avro.default.schema(Person.serializer())
        }.from(input.payload).nextOrThrow()
    }
}

class PersonToPubsubMessageFn(val attributes: Map<String, String> = ImmutableMap.of()) : SimpleFunction<Person, PubsubMessage>() {
    private val serializer = Person.serializer()
    private val personSchema = Avro.default.schema(Person.serializer())

    override fun apply(input: Person): PubsubMessage {
        val baos = ByteArrayOutputStream()
        Avro.default.openOutputStream(serializer) {
            format = AvroFormat.BinaryFormat
            this.schema = personSchema
        }.to(baos).write(input).close()
        val data = baos.toByteArray()
        return PubsubMessage(data, attributes)
    }
}
