package micro.libs.avro

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.core.Predicate
import micro.apps.core.and
import org.apache.avro.Schema
import java.util.concurrent.atomic.AtomicInteger

class FieldExtractorSpec : FunSpec({
    val SCHEMA: Schema = Schema.Parser().parse(javaClass.getResourceAsStream("/account.avsc"))

    test("extractFields examples") {
        val isString: Predicate<Schema.Field> = { field ->
            when (field.schema().type) {
                Schema.Type.STRING -> true
                Schema.Type.UNION -> field.schema().extractNonNullType() == Schema.Type.STRING
                else -> false
            }
        }
        val isLeafField: Predicate<Schema.Field> = { field ->
            val schema = field.schema()
            when (if (schema.type == Schema.Type.UNION) schema.extractNonNullType() else schema.type) {
                Schema.Type.MAP, Schema.Type.ARRAY, Schema.Type.RECORD, Schema.Type.NULL -> false
                else -> true
            }
        }
        val hasDefault: Predicate<Schema.Field> = { field -> field.hasDefaultValue() }
        val hasProps: Predicate<Schema.Field> = { field -> field.hasProps() }
        val isConfidential: Predicate<Schema.Field> = { field -> field.getProp("confidential")?.let { true } ?: false }
        val combo = (hasDefault and hasProps)

        println("--------root-schema, no predicate---------")
        extractFields(SCHEMA).forEach { println(it) }
        println("--------root-schema, inline predicate---------")
        extractFields(SCHEMA) { field -> field.hasDefaultValue() }.forEach { println(it) }
        println("-------root-schema, isString predicate----------")
        extractFields(SCHEMA, isString).forEach { println(it) }
        println("-------root-schema, isConfidential predicate----------")
        extractFields(SCHEMA, isConfidential).forEach { println(it) }
        println("-------root-schema,  isLeafField predicate----------")
        extractFields(SCHEMA, isLeafField).forEach { println(it) }
        println("--------root-schema, combo: isString and hasProps and isConfidential predicates---------")
        extractFields(SCHEMA, isString and hasProps and isConfidential).forEach { println(it) }
        println("--------root-schema, combo: isLeafField and isConfidential predicates---------")
        extractFields(SCHEMA, isLeafField and isConfidential).forEach { println(it) }
        println("-------sub-schema,  no predicate----------")
        extractFields(SCHEMA.getField("addresses").schema()).forEach { println(it) }
    }

    test("memorizedExtractFields with isLeafConfidential should be call once") {
        val expected = listOf("id", "name.first", "addresses.street", "gender", "age", "dob", "altNames.first", "family.first")
        val counter = AtomicInteger(0)
        val isLeafConfidentialTest = isLeafConfidential.also { counter.incrementAndGet() }

        val actual = memorizedExtractFields(SCHEMA, isLeafConfidentialTest).map { it.first }
        actual shouldBe expected

        var memorized: List<String>
        repeat(30) { index ->
            memorized = memorizedExtractFields(SCHEMA, isLeafConfidentialTest).map { it.first }
            println("turn: #$index, result: $memorized")
        }
        memorized = memorizedExtractFields(SCHEMA, isLeafConfidentialTest).map { it.first }

        memorized shouldBe expected
        counter.get() shouldBe 1
    }

})
