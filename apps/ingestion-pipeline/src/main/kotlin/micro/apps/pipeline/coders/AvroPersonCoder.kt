package micro.apps.pipeline.coders

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroDecodeFormat
import com.github.avrokotlin.avro4k.io.AvroEncodeFormat
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Person
import org.apache.beam.sdk.coders.CustomCoder
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

@ExperimentalSerializationApi
class AvroPersonCoder : CustomCoder<Person>() {
    @Transient
    private val serializer = Person.serializer()

    @Transient
    private val personSchema = Avro.default.schema(Person.serializer())

    @Throws(IOException::class)
    override fun encode(value: Person, outStream: OutputStream) {
        Avro.default.openOutputStream(serializer) {
            encodeFormat = AvroEncodeFormat.Data() // Other Options: AvroEncodeFormat.Binary(), AvroEncodeFormat.Json()
            this.schema = personSchema
        }.to(outStream).write(value).close()
    }

    @Throws(IOException::class)
    override fun decode(inStream: InputStream): Person {
        return Avro.default.openInputStream(serializer) {
            decodeFormat =
                AvroDecodeFormat.Data(personSchema) // Other Options: AvroDecodeFormat.Binary(), AvroDecodeFormat.Json()
        }.from(inStream).nextOrThrow()
    }
}
