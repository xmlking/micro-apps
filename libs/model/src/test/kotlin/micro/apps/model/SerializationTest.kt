package micro.apps.model

// import com.charleskorn.kaml.Yaml
// import com.charleskorn.kaml.YamlConfiguration
import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.ProtoId
import kotlinx.serialization.protobuf.ProtoNumberType
import kotlinx.serialization.protobuf.ProtoType
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

@Serializable
data class Ingredient(val name: String, val sugar: Double, val fat: Double)

@Serializable
data class Pizza(val name: String, val ingredients: List<Ingredient>, val vegetarian: Boolean, val kcals: Int)

@Serializable
data class ProtobufData(
    @ProtoId(1) @ProtoType(ProtoNumberType.SIGNED) val a: Int,
    @ProtoId(2) val b: Double = 42.88
)

class SerializationTest {

//    val JSON by lazy {
//        Json(JsonConfiguration.Stable.copy(isLenient = true, prettyPrint = true))
//    }

//    val YAML by lazy {
//        Yaml(configuration = YamlConfiguration(strictMode = false))
//    }

    private val veg = Pizza("veg", listOf(Ingredient("peppers", 0.1, 0.3), Ingredient("onion", 1.0, 0.4)), true, 265)
    private val hawaiian = Pizza("hawaiian", listOf(Ingredient("ham", 1.5, 5.6), Ingredient("pineapple", 5.2, 0.2)), false, 391)

    private val vegString = """{"name":"veg","ingredients":[{"name":"peppers","sugar":0.1,"fat":0.3},{"name":"onion","sugar":1.0,"fat":0.4}],"vegetarian":true,"kcals":265}"""
    private val hawaiianString = """{"name":"hawaiian","ingredients":[{"name":"ham","sugar":1.5,"fat":5.6},{"name":"pineapple","sugar":5.2,"fat":0.2}],"vegetarian":false,"kcals":391}"""

    private val pizzaString = """[{"name":"veg","ingredients":[{"name":"peppers","sugar":0.1,"fat":0.3},{"name":"onion","sugar":1.0,"fat":0.4}],"vegetarian":true,"kcals":265},{"name":"hawaiian","ingredients":[{"name":"ham","sugar":1.5,"fat":5.6},{"name":"pineapple","sugar":5.2,"fat":0.2}],"vegetarian":false,"kcals":391}]"""

    @kotlinx.serialization.UnstableDefault
    @Test
    fun testJsonSerialization() {
        val str = Json.stringify<Pizza>(Pizza.serializer(), veg)
        assertEquals(vegString, str)

        // parsing data back
        val obj = Json.parse<Pizza>(Pizza.serializer(), str)
        assertEquals(veg, obj)
    }

    @kotlinx.serialization.UnstableDefault
    @Test
    fun testJsonListSerialization() {
        val str = Json.stringify<List<Pizza>>(Pizza.serializer().list, listOf(veg, hawaiian))
        assertEquals(pizzaString, str)
        println(str)
    }

    @Test
    fun testProtobufSerialization() {
        // testing non-proto class
        val pizzaBytes = ProtoBuf.dump(Pizza.serializer(), hawaiian)
        val pizza = ProtoBuf.load<Pizza>(Pizza.serializer(), pizzaBytes) // parsing data back
        assertEquals(hawaiian, pizza)
        // testing ProtoId annotated class
        val originalData = ProtobufData(a = 5)
        val dump = ProtoBuf.dump<ProtobufData>(ProtobufData.serializer(), originalData)
        val data = ProtoBuf.load<ProtobufData>(ProtobufData.serializer(), dump) // parsing data back
        assertEquals(originalData, data)
    }

    @Test
    fun testAvroSerialization_WriteData() {
        val serializer = Pizza.serializer()
        val schema = Avro.default.schema(serializer)
        val output = Avro.default.openOutputStream(serializer) {
            format = AvroFormat.DataFormat // Other Options: AvroFormat.BinaryFormat, AvroFormat.JsonFormat
            this.schema = schema
        }.to("./src/test/resources/data/pizzas.avro")
        output.write(listOf(veg, hawaiian))
        output.close()
    }

    @Test
    fun testAvroSerialization_ReadData() {
        val serializer = Pizza.serializer()
        val schema = Avro.default.schema(serializer)
        val input = Avro.default.openInputStream(serializer) {
            format = AvroFormat.DataFormat // Other Options: AvroFormat.BinaryFormat, AvroFormat.JsonFormat
            writerSchema = schema
        }.from("./src/test/resources/data/pizzas.avro")
        println(input.nextOrThrow()) // get first only
        input.iterator().forEach { println(it) } // get all
        input.close()
    }

    @Test
    fun testAvroSerialization_ReadDataAsGenericRecord() {
        val serializer = Pizza.serializer()
        val schema = Avro.default.schema(serializer)
        val input = Avro.default.openInputStream() {
            format = AvroFormat.DataFormat
            readerSchema = schema
        }.from(javaClass.getResourceAsStream("/data/pizzas.avro"))
        input.iterator().forEach { println(it as GenericRecord) }
        input.close()
    }

    @Test
    fun testGeneratedSchema() {
        val expectedSchema = Schema.Parser().parse(javaClass.getResourceAsStream("/data/pizzas.avsc"))
        val generatedSchema = Avro.default.schema(Pizza.serializer())
        assertEquals(expectedSchema.toString(true), generatedSchema.toString(true))
    }
}
