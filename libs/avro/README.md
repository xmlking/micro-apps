# Avro

A set of **Avro** _data_ and _schema_ manipulation functions.

- **FieldExtractor**: Traverse/extract fields from schema
- **avpath**: XPath like DSL to query and manipulate entries of an Avro records
- **RecordTransformer**: Traverse **GenericRecord** along **avpath** and transform values with the given **ValueTransformer**

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
More [examples](./src/test/kotlin/micro/libs/avro/FieldExtractorSpec.kt)  

### avpath

```kotlin
RecordTraverser.traverseRecord(genericRecord, "name.title") { parent, suffix ->
    parent.get(suffix)?.let { original ->
        parent.put(suffix, "newValue for $suffix is $original ++")
    }
}
// Output
// {"id": "122-333-344-555", "name": {"first": "sumo", "last": "demo", "title": "newValue for title is Mr ++"}, ...}
```
More [examples](./src/test/kotlin/micro/libs/avro/avpath/RecordTraverserSpec.kt)

### RecordTransformer

```kotlin
val avpaths =   listOf("id", "name.first", "addresses.street", "altNames.first", "family.first")

println("Before: $it")
RecordTransformer.transform(it, avpaths) { "---MASKED---" }
print("After: $it")
// Output
// Before: {"id": "122-333-344-555", "name": {"first": "sumo", "last": "demo", "title": "Mr"}, 
// After: {"id": "---MASKED---", "name": {"first": "---MASKED---", "last": "demo", "title": "Mr"}, ...
```
More [examples](./src/test/kotlin/micro/libs/avro/RecordTransformerSpec.kt)

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
