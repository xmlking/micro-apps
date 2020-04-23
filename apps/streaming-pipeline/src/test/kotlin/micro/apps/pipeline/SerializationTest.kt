package micro.apps.pipeline

import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import kotlin.test.Test
import micro.apps.model.Person

class SerializationTest {
    val person1 = Person(firstName = "sumo1", lastName = "demo1", email = "sumo1@demo.com", phone = "0000000000", age = 99)
    val person2 = Person(firstName = "sumo2", lastName = "demo1", email = "sumo2@demo.com", phone = "1111111111", age = 99, valid = true)

    @Test
    fun testAvroSerialization_WriteData() {
        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)
        val output = Avro.default.openOutputStream(serializer) {
            format = AvroFormat.DataFormat // Other Options: AvroFormat.BinaryFormat, AvroFormat.JsonFormat
            this.schema = schema
        }.to("./src/test/resources/data/person.avro")
        output.write(listOf(person1, person2))
        output.close()
    }

    @Test
    fun testAvroSerialization_ReadData() {
        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)
        val input = Avro.default.openInputStream(serializer) {
            format = AvroFormat.DataFormat // Other Options: AvroFormat.BinaryFormat, AvroFormat.JsonFormat
            writerSchema = schema
        }.from("./src/test/resources/data/person.avro")

        input.iterator().forEach { println(it) }
        input.close()
        println(schema)
    }
}
