@file:JvmName("SchemaTraverser")

package micro.libs.avro.traversal

import org.apache.avro.Schema
import java.util.*
import kotlin.collections.ArrayDeque
import kotlin.collections.joinToString


interface SchemaVisitor {
    fun visitSchema(schema: Schema) {}
    fun visitField(parent: Schema, field: Schema.Field, path: String) {}
}

fun traverseSchema(schema: Schema, visitor: SchemaVisitor, excludeVisited: Boolean = false) {
    val visited = if (excludeVisited) IdentityHashMap<Any, Boolean>() else null
    traverseSchema(schema, visitor, visited)
}

private fun traverseSchema(
    schema: Schema,
    visitor: SchemaVisitor,
    visited: IdentityHashMap<Any, Boolean>? = null,
    pathStack: ArrayDeque<String> = ArrayDeque()
) {
    visited?.let {
        if (visited.put(schema, java.lang.Boolean.TRUE) != null) {
            return  //been there, done that
        }
    }

    visitor.visitSchema(schema)
    when (schema.type) {
        Schema.Type.UNION -> {
            for (unionBranch in schema.types) {
                traverseSchema(unionBranch, visitor, visited, pathStack)
            }
            return
        }
        Schema.Type.ARRAY -> {
            traverseSchema(schema.elementType, visitor, visited, pathStack)
            return
        }
        Schema.Type.MAP -> {
            traverseSchema(schema.valueType, visitor, visited, pathStack)
            return
        }
        Schema.Type.RECORD -> for (field in schema.fields) {
            pathStack.addLast(field.name())
            visitor.visitField(schema, field, pathStack.joinToString("."))
            traverseSchema(field.schema(), visitor, visited, pathStack)
            pathStack.removeLast()
        }
        else -> {}
    }
}

