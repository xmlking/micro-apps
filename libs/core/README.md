# Core

Core Lib module.

Convention is to keep `core` module not to depend on 3rd party dependencies. contains classes with common logic, util
functions and pure functions developed by project developers.

## Usage

### Compose

```kitlin
val hasDefault: Predicate<Schema.Field> = { field -> field.hasDefaultValue() }
val hasProps: Predicate<Schema.Field> = { field -> field.hasProps() }
extractFields(SCHEMA, hasDefault and hasProps).forEach { println(it) }
```

### Memorize

#### Important

**Functions must be pure** for the caching technique to work. A pure function is one that has no side effects: it references no other mutable class fields, doesn't set any values other than the return value, and relies only on the parameters for input.

Also, when passing or returning Objects, make sure to implement both **equals** and **hashcode** for the cache to work properly!

Having a function like:

```kotlin
fun myExpensiveFun(someArg: path, someOtherArg: Scheam): String = { ... }
```

You can create a memoized version of it by just calling an extension function over it like this:
```kotlin
val memorized = ::myExpensiveFun.memorize()
```

Now _memorized_ is the same function as _myExpensiveFun_ but is wrapped in a Closure that contains an internal cache, meaning that the first call to:
```kotlin
memorized("address.city", SCHEMA)
```

Will just execute the function. But the second call with the same arguments will retrieve the already calculated value from cache.

## Developer 

### Run

```bash
gradle libs:core:clean
```

### Test

```bash
gradle libs:core:test
```

### Build

```bash
gradle libs:core:build
```
