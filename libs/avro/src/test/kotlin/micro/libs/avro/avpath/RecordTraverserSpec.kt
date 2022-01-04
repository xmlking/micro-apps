package micro.libs.avro.avpath

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import org.apache.avro.Schema
import org.apache.avro.file.DataFileReader
import org.apache.avro.generic.GenericArray
import org.apache.avro.generic.GenericDatumReader
import org.apache.avro.generic.GenericEnumSymbol
import org.apache.avro.generic.GenericFixed
import org.apache.avro.generic.GenericRecord
import org.apache.avro.util.Utf8
import java.io.File
import java.util.concurrent.atomic.AtomicInteger


class RecordTraverserSpec : FunSpec({
    lateinit var records: List<GenericRecord>

    beforeSpec {
        val genericDatumReader = GenericDatumReader<GenericRecord>()
        val dataFileReader = DataFileReader(File("./src/test/resources/account.avro"), genericDatumReader)
        val SCHEMA: Schema = Schema.Parser().parse(javaClass.getResourceAsStream("/account.avsc"))
        dataFileReader.schema shouldBe SCHEMA
        dataFileReader.use { reader ->
            // HINT: Recommended for avro file with large number of records.
            // Reuse user object by passing it to next(). This saves us from allocating and
            // garbage collecting many objects for files with many items.
            //val tmpRecord: GenericRecord? = null
            //val seq = generateSequence {
            //    if (reader.hasNext()) reader.next(tmpRecord) else null
            //}
            val seq = (reader as Iterable<GenericRecord>).asSequence()
            records = seq.toList()
        }
    }

    test("read account.avro file with GenericDatumReader example") {
        records.forEach {
            println(it)

            val name = it.get("name") as GenericRecord
            println(name.schema)
            println(name::class.qualifiedName)
            println(name)

            val first = name.get(0)
            println(first::class.qualifiedName)
            println(first)

            val email = it.get("email")
            println(email::class.qualifiedName)
            println(email)

            val phone = it.get("phone") as GenericFixed
            println(phone::class.qualifiedName)
            println(phone)

            val addresses = it.get("addresses") as GenericArray<*>
            println(addresses::class.qualifiedName)
            println(addresses[0])

            val gender = it.get("gender") as GenericEnumSymbol<*>
            println(gender::class.qualifiedName)
            println(gender)

            val family = it.get("family") as Map<*, *>
            println(family::class.qualifiedName)
            println(family)
        }
    }

    test(" traverseRecord should throw error for invalid avpath") {
        val exception = shouldThrow<IllegalArgumentException> {
            records[0].let {
                traverseRecord(it, "..") { parent, suffix ->
                    println(parent)
                    println(suffix)
                }
            }
        }
        exception.message shouldContain "avpath: '..' is empty"
    }

    test("verify traverseRecord with in-place-edit transformer for name.title") {
        records.forEach {
            traverseRecord(it, "name.title") { parent, suffix ->
                parent.get(suffix)?.let { original ->
                    parent.put(suffix, "newValue for $suffix is $original ++")
                }
            }

            val name = it.get("name")
            name.shouldBeInstanceOf<GenericRecord>()
            name.get("title") shouldBe "newValue for title is Mr ++"
        }
    }

    context("traverseRecord should avoid traversing avpaths that has null data") {
        test("when parent GenericRecord is null for avpath: name.first") {
            val counter = AtomicInteger(0)
            records.forEach {
                it.put("name", null)
                traverseRecord(it, "name.first") { parent, suffix ->
                    counter.incrementAndGet()
                    parent.get(suffix)?.let { original ->
                        println(original)
                        (original as? GenericRecord)?.put("last", "new-last ${original.get("last")}")
                    }
                }

                it.get("name") shouldBe null
            }
            counter.get() shouldBe 0
        }

        test("when target GenericRecord is null for avpath: name") {
            val counter = AtomicInteger(0)
            records.forEach {
                it.put("name", null)
                traverseRecord(it, "name") { parent, suffix ->
                    counter.incrementAndGet()
                    parent.get(suffix)?.let { original ->
                        println(original)
                        (original as? GenericRecord)?.put("last", "new-last ${original.get("last")}")
                    }
                }

                it.get("name") shouldBe null
            }
            counter.get() shouldBe 2
        }

        test("when map has a null value") {
            val counter = AtomicInteger(0)
            records[0].let {
                val family = it.get("family") as HashMap<Utf8, Any?>
                family.replace(Utf8("spouse"), null)

                traverseRecord(it, "family.last") { parent, suffix ->
                    counter.incrementAndGet()
                    parent.get(suffix)?.let {
                        parent.put(suffix, "newValue for $suffix")
                    }
                }

                counter.get() shouldBe 2
                println("Transferred Record")
                println(it)
            }
        }

        test("when list has a null element") {
            val counter = AtomicInteger(0)
            records[1].let {
                val addresses = it.get("addresses") as GenericArray<GenericRecord?>
                addresses[0] = null
                traverseRecord(it, "addresses.street") { parent, suffix ->
                    counter.incrementAndGet()
                    parent.get(suffix)?.let {
                        parent.put(suffix, "newValue for $suffix")
                    }
                }

                counter.get() shouldBe 0
                println("Transferred Record")
                println(it)
            }
        }
    }

})
