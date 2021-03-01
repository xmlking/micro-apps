package micro.apps.pipeline.coders

import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import kotlinx.serialization.KSerializer
import org.apache.beam.sdk.coders.CoderException
import org.apache.beam.sdk.coders.CustomCoder
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

interface KSerializable<T> {
    fun serializer(): KSerializer<T>
}

class KotlinAvroCoder<T> : CustomCoder<T>() where T : KSerializable<T> {

    @Throws(CoderException::class, IOException::class)
    override fun encode(value: T, outStream: OutputStream) {
        val serializer = value.serializer()
        Avro.default.openOutputStream(serializer) {
            format = AvroFormat.DataFormat
        }.to(outStream).write(value).close()
    }

    @Throws(CoderException::class, IOException::class)
    override fun decode(value: InputStream): T {
        TODO()
//        val  deserializer: KSerializer<T> = T::class.serializer()
//        return Avro.default.openInputStream(deserializer) {
//            format = AvroFormat.DataFormat
//        }.from(value).nextOrThrow()
    }
}

/*
// usage:  .withCoder(new MyCoder<>())

class MyCoder<T> : CustomCoder<T>() {
    @Throws(CoderException::class, IOException::class)
    override fun encode(value: T, outStream: OutputStream) {
        val oos = ObjectOutputStream(outStream)
        oos.writeObject(value)
    }

    @Throws(CoderException::class, IOException::class)
    override fun decode(inStream: InputStream): T? {
        val bis = ObjectInputStream(inStream)
        try {
            return bis.readObject() as T
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }
}
*/
