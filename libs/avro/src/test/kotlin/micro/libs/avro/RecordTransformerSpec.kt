package micro.libs.avro

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.apache.avro.Schema
import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericRecord
import org.apache.avro.util.Utf8
import java.io.File


class RecordTransformerSpec : FunSpec({
    lateinit var records: List<GenericRecord>
    lateinit var avpaths: List<String>

    beforeSpec {
        val genericDatumReader = GenericDatumReader<GenericRecord>()
        val dataFileReader = DataFileReader(File("./src/test/resources/account.avro"), genericDatumReader)
        val SCHEMA: Schema = Schema.Parser().parse(javaClass.getResourceAsStream("/account.avsc"))
        dataFileReader.schema shouldBe SCHEMA
        dataFileReader.use { reader ->
            val seq = (reader as Iterable<GenericRecord>).asSequence()
            records = seq.toList()
        }

        avpaths = memorizedExtractFields(SCHEMA, isLeafConfidential).map { it.first }
        println("avpaths: $avpaths")
    }

    test("transform fields with inline transformer should work") {
        val encryptTransformer: ValueTransformer<String> = { it.reversed() }

        records[0].let {
            println("Before: $it")
            transform(it, avpaths) { "---MASKED---" }
            print("After: $it")
        }
    }

    test("transform confidential fields with a transformer should work") {
        val encryptTransformer: ValueTransformer<String> = { it.reversed() }

        records[0].let {
            println("Before: $it")
            transform(it, avpaths, encryptTransformer)
            print("After: $it")
        }
    }

    test("transform confidential fields with a transformer should work with null fields") {
        val encryptTransformer: ValueTransformer<String> = { it.reversed() }

        records[1].let {
            val family = it.get("family") as HashMap<Utf8, Any?>
            // set spouse as null
            family.replace(Utf8("spouse"), null)
            // set son's first name as null
            val sun = family[Utf8("son")] as GenericRecord
            sun.put("first", null)
            // set id as null
            it.put("id", null)

            println("Before: $it")
            transform(it, avpaths, encryptTransformer)
            print("After: $it")
        }
    }
})
