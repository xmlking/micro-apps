package micro.apps.pipeline

import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import kotlin.test.Ignore
import kotlin.test.Test
import micro.apps.model.Address
import micro.apps.model.Gender
import micro.apps.model.Name
import micro.apps.model.Person

val persons = listOf(
    Person(
        name = Name(first = "sumo1", last = "demo1"),
        address = Address(suite = "1234", street = "Wood Road", city = "Riverside", state = "California", code = "92505", country = "CA"),
        gender = Gender.MALE, age = 99,
        email = "sumo1@demo.com", phone = "0000000000"),
    Person(
        name = Name(first = "sumo2", last = "demo2"),
        address = Address(suite = "4321", street = "Wood Road", city = "Riverside", state = "California", code = "92505", country = "CA"),
        gender = Gender.FEMALE, age = 99,
        email = "sumo2@demo.com", phone = "1111111111")
)

class SerializationTest {
    @Test @Ignore
    fun testAvroSerialization_WriteData() {
        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)
        println(schema)
        val output = Avro.default.openOutputStream(serializer) {
            format = AvroFormat.DataFormat // Other Options: AvroFormat.BinaryFormat, AvroFormat.JsonFormat
            this.schema = schema
        }.to("./src/test/resources/data/person.avro")
        output.write(persons)
        output.close()
    }

    @Test @Ignore
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
