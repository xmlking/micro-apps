package micro.libs.avro

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import micro.apps.core.Predicate
import micro.apps.core.and
import micro.apps.core.memorize
import org.apache.avro.Schema

class FieldExtractorTest : FunSpec({
    val SCHEMA: Schema = Schema.Parser().parse(javaClass.getResourceAsStream("/account.avsc"))

    test("extractFields use-cases") {
        val isString: Predicate<Schema.Field> = { field ->
            when (field.schema().type) {
                Schema.Type.STRING -> true
                Schema.Type.UNION -> unwrapUnionType(field) == Schema.Type.STRING
                else -> false
            }
        }
        val isLeafField: Predicate<Schema.Field> = { field ->
            when (if (field.schema().type == Schema.Type.UNION) unwrapUnionType(field) else field.schema().type) {
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

    test("extractFields with DefaultValues should match") {
        val expected = listOf<String>("gender", "dob")
        val actual = extractFields(SCHEMA) { field -> field.hasDefaultValue() }.map { it.first }
        actual shouldBe expected
    }

    // TODO: Test
    test("memorizedExtractFields with isLeafField should only call once") {
        val expected = listOf<String>("gender", "dob")
        val actual = memorizedExtractFields(SCHEMA) { field -> field.hasDefaultValue() }.map { it.first }
        actual shouldBe expected

        var memorized =  listOf<String>()
        repeat(30) {
            memorized = memorizedExtractFields(SCHEMA) { field -> field.hasDefaultValue() }.map { it.first }
            println("turn: #$it, result: $memorized")
        }

        memorized shouldBe expected
    }

})
