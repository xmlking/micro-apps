package micro.apps.pipeline

import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import io.kotest.core.spec.style.FunSpec
import kotlin.time.ExperimentalTime
import micro.apps.model.Person
import micro.apps.model.fixtures.mockPersonList

@ExperimentalTime
@kotlinx.serialization.UnstableDefault
class SerializationTest : FunSpec({

    val persons = mockPersonList()

    test("testAvroSerialization_WriteData").config(enabled = false) {
        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)
        println(schema)
        val output = Avro.default.openOutputStream(serializer) {
            format = AvroFormat.DataFormat // Other Options: AvroFormat.BinaryFormat, AvroFormat.JsonFormat
            this.schema = schema
        }.to("./apps/streaming-pipeline/src/test/resources/data/pizzas.avro")
        output.write(persons)
        output.close()
    }

    test("testAvroSerialization_ReadData") {
        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)
        val input = Avro.default.openInputStream(serializer) {
            format = AvroFormat.DataFormat // Other Options: AvroFormat.BinaryFormat, AvroFormat.JsonFormat
            writerSchema = schema
        }.from(javaClass.getResourceAsStream("/data/person.avro"))
        input.iterator().forEach { println(it) }
        input.close()
        println(schema)
    }
})
