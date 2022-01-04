package micro.libs.avro.schema

import io.kotest.core.spec.style.FunSpec
import org.apache.avro.Schema

class SchemaTraverserSpec : FunSpec({
    val SCHEMA: Schema = Schema.Parser().parse(javaClass.getResourceAsStream("/account.avsc"))

    test("Test SchemaTraverser") {
        traverseSchema(SCHEMA, visitor =
        object : SchemaVisitor {
            override fun visitSchema(schema: Schema) {}

            override fun visitField(parent: Schema, field: Schema.Field, path: String) {
                println("Field:: path: $path, attrib: ${field.getProp("confidential")}, fullName: ${parent.fullName}.${field.name()}")
            }
        }
        )
    }

    test("Test SchemaTraverser excludeVisited") {
        traverseSchema(
            SCHEMA, visitor =
            object : SchemaVisitor {
                override fun visitSchema(schema: Schema) {}

                override fun visitField(parent: Schema, field: Schema.Field, path: String) {
                    println("Field:: path: $path, attrib: ${field.getProp("confidential")}, fullName: ${parent.fullName}.${field.name()}")
                }
            }, true
        )
    }

})
