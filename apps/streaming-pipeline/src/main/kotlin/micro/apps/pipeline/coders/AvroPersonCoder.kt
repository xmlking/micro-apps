package micro.apps.pipeline

import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import micro.apps.model.Person
import org.apache.beam.sdk.coders.CustomCoder
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

/*
interface KSerializable<T> {
    fun serializer(): KSerializer<T>
}

class Avro4Coder<T> : CustomCoder<T>() where T : KSerializable<T> {
    @Throws(IOException::class)
    override fun encode(value: T, outStream: OutputStream) {
       val  serializer = value.serializer() // value::class.java.serializer()
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
    private val  serializer = Person.serializer()

    @Throws(IOException::class)
    override fun encode(value: Person, outStream: OutputStream) {
        Avro.default.openOutputStream(serializer) {
            format = AvroFormat.DataFormat
        }.to(outStream).write(value).close()
    }

    @Throws(IOException::class)
    override fun decode(inStream: InputStream): Person {
        return Avro.default.openInputStream(serializer) {
            format = AvroFormat.DataFormat
        }.from(inStream).nextOrThrow()
    }
}
