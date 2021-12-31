# Avro

A set of **Avro** _data_ and _schema_ manipulation functions.

- **FieldExtractor**: Traverse/extract fields from schema
- **avpath**: XPath like DSL to query and manipulate entries of an Avro records

## Usage

### FieldExtractor

```kotlin
val schema = Schema.Parser().parse(javaClass.getResourceAsStream("/account.avsc"))
val hasDefault: Predicate<Schema.Field> = { field -> field.hasDefaultValue() }
val isConfidential: Predicate<Schema.Field> = { field -> field.getProp("confidential")?.let { true } ?: false }

var sensitiveFields = memorizedExtractFields(SCHEMA, isConfidential).map { it.first }
// Output
// [id, name.first, addresses.street, gender, age, dob, altNames.first, family.first]
```

### avpath

```kotlin
val schema =  Schema.parse("account.avsc")
val account = GenericData.Record(schema)

traverseRecord(record, "altNames.first") { record: GenericData -> 
    println(record)
}
```

```kotlin
val schema =  Schema.parse("account.avsc")
val account = GenericData.Record(schema)

var sensitiveFields = memorizedExtractFields(schema, isConfidential).map { it.first }

sensitiveFields.forEach {
    traverseRecord(record, it) { record: GenericData ->
        println(record)
    }
}
```


## Developer
### Run

```bash
gradle libs:avro:clean
```

### Test

```bash
gradle libs:avro:test
```

### Build

```bash
gradle libs:avro:build
```
