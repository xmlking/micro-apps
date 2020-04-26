package micro.apps.pipeline.coders

import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import micro.apps.model.Address
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.model.Person
import org.apache.beam.sdk.coders.CustomCoder
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.SimpleFunction

/*
interface KSerializable<T> {
    fun serializer(): KSerializer<T>
}

class Avro4Coder<T> : CustomCoder<T>() where T : KSerializable<T> {
    @Throws(IOException::class)
    override fun encode(value: T, outStream: OutputStream) {
       @Suppress("UNCHECKED_CAST")
       // val  serializer = value::class.serializer() as KSerializer<Any>
       val  serializer = value.serializer()
        Avro.default.openOutputStream(serializer) {
            format = AvroFormat.DataFormat
        }.to(outStream).write(value).close()
    }

    @Throws(IOException::class)
    override fun decode(inStream: InputStream): T {
        val  serializer = value.serializer() // value::class.java.serializer()
        Avro.default.openInputStream(deserializer) {
            format = AvroFormat.DataFormat
        }.from(inStream).nextOrThrow()
    }
}
*/

class AvroPersonCoder : CustomCoder<Person>() {
    private val serializer = Person.serializer()
    private val personSchema = Avro.default.schema(Person.serializer())

    @Throws(IOException::class)
    override fun encode(value: Person, outStream: OutputStream) {
        Avro.default.openOutputStream(serializer) {
            format = AvroFormat.BinaryFormat
            this.schema = personSchema
        }.to(outStream).write(value).close()
    }

    @Throws(IOException::class)
    override fun decode(inStream: InputStream): Person {
        return Avro.default.openInputStream(serializer) {
            format = AvroFormat.BinaryFormat
            writerSchema = personSchema
        }.from(inStream).nextOrThrow()
    }
}

class PubsubMessageToPersonFn() : SimpleFunction<PubsubMessage, Person>() {
    override fun apply(input: PubsubMessage): Person {

        return Person(
            name = Name(first = "sumo1", last = "demo1"),
            address = Address(suite = "1234", street = "Wood Road", city = "Riverside", state = "California", code = "92505", country = "CA"),
            gender = Gender.MALE, age = 99,
            email = "sumo1@demo.com", phone = "0000000000")
    }
}
