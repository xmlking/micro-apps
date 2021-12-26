package micro.apps.pipeline

import com.github.avrokotlin.avro4k.Avro
import com.github.avrokotlin.avro4k.io.AvroDecodeFormat
import com.github.avrokotlin.avro4k.io.AvroEncodeFormat
import io.kotest.core.spec.style.FunSpec
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Person
import micro.apps.model.fixtures.mockPersonList
import kotlin.time.ExperimentalTime

@ExperimentalTime
@OptIn(ExperimentalSerializationApi::class)
class SerializationTest : FunSpec({

    val persons = mockPersonList()

    test("test avro serialization WriteData").config(enabled = false) {
        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)
        println(schema)
        val output = Avro.default.openOutputStream(serializer) {
            encodeFormat = AvroEncodeFormat.Data() // Other Options: AvroEncodeFormat.Binary(), AvroEncodeFormat.Json()
            this.schema = schema
        }.to("./apps/classifier-pipeline/src/test/resources/data/person.avro")
        output.write(persons)
        output.close()
    }

    test("test avro serialization ReadData") {
        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)
        val input = Avro.default.openInputStream(serializer) {
            decodeFormat = AvroDecodeFormat.Data(schema) // Other Options: AvroDecodeFormat.Binary(), AvroDecodeFormat.Json()
        }.from(javaClass.getResourceAsStream("/data/person.avro"))
        input.iterator().forEach { println(it) }
        input.close()
        println(schema)
    }
})
