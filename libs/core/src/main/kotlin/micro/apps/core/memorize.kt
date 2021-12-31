package micro.apps.core

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

private const val DEFAULT_CAPACITY = 256

fun <A, R> ((A) -> R).memorize(
    initialCapacity: Int = DEFAULT_CAPACITY
): (A) -> R = memorize(HashMap(initialCapacity))

fun <A, R> ((A) -> R).memorize(
    cache: MutableMap<A, R>
): (A) -> R = { a: A ->
    cache.getOrPut(a) { this(a) }
}

fun <A, B, R> ((A, B) -> R).memorize(
    initialCapacity: Int = DEFAULT_CAPACITY
): (A, B) -> R = memorize(HashMap(initialCapacity))

fun <A, B, R> ((A, B) -> R).memorize(
    cache: MutableMap<Pair<A, B>, R>
): (A, B) -> R = { a: A, b: B ->
    cache.getOrPut(a to b) { this(a, b) }
}

fun <A, B, C, R> ((A, B, C) -> R).memorize(
    initialCapacity: Int = DEFAULT_CAPACITY
): (A, B, C) -> R = memorize(HashMap(initialCapacity))

fun <A, B, C, R> ((A, B, C) -> R).memorize(
    cache: MutableMap<Triple<A, B, C>, R>
): (A, B, C) -> R = { a: A, b: B, c: C ->
    cache.getOrPut(Triple(a, b, c)) { this(a, b, c) }
}

fun <A, B, C, D, R> ((A, B, C, D) -> R).memorize(
    initialCapacity: Int = DEFAULT_CAPACITY
): (A, B, C, D) -> R = memorize(HashMap(initialCapacity))

fun <A, B, C, D, R> ((A, B, C, D) -> R).memorize(
    cache: MutableMap<Quadruple<A, B, C, D>, R>
): (A, B, C, D) -> R = { a: A, b: B, c: C, d: D ->
    cache.getOrPut(Quadruple(a, b, c, d)) { this(a, b, c, d) }
}

fun <A, B, C, D, E, R> ((A, B, C, D, E) -> R).memorize(
    initialCapacity: Int = DEFAULT_CAPACITY
): (A, B, C, D, E) -> R = memorize(HashMap(initialCapacity))

fun <A, B, C, D, E, R> ((A, B, C, D, E) -> R).memorize(
    cache: MutableMap<Quintuple<A, B, C, D, E>, R>
): (A, B, C, D, E) -> R = { a: A, b: B, c: C, d: D, e: E ->
    cache.getOrPut(Quintuple(a, b, c, d, e)) { this(a, b, c, d, e) }
}

fun <A, R> (suspend (A) -> R).memorizeSuspend(
    initialCapacity: Int = DEFAULT_CAPACITY
): suspend (A) -> R = memorizeSuspend(ConcurrentHashMap(initialCapacity))

fun <A, R> (suspend (A) -> R).memorizeSuspend(
    cache: ConcurrentMap<A, R>
): suspend (A) -> R = { a: A ->
    cache.getOrPut(a) { this(a) }
}

fun <A, B, R> (suspend (A, B) -> R).memorizeSuspend(
    initialCapacity: Int = DEFAULT_CAPACITY
): suspend (A, B) -> R = memorizeSuspend(ConcurrentHashMap(initialCapacity))

fun <A, B, R> (suspend (A, B) -> R).memorizeSuspend(
    cache: ConcurrentMap<Pair<A, B>, R>
): suspend (A, B) -> R = { a: A, b: B ->
    cache.getOrPut(a to b) { this(a, b) }
}

fun <A, B, C, R> (suspend (A, B, C) -> R).memorizeSuspend(
    initialCapacity: Int = DEFAULT_CAPACITY
): suspend (A, B, C) -> R = memorizeSuspend(ConcurrentHashMap(initialCapacity))

fun <A, B, C, R> (suspend (A, B, C) -> R).memorizeSuspend(
    cache: ConcurrentMap<Triple<A, B, C>, R>
): suspend (A, B, C) -> R = { a: A, b: B, c: C ->
    cache.getOrPut(Triple(a, b, c)) { this(a, b, c) }
}

fun <A, B, C, D, R> (suspend (A, B, C, D) -> R).memorizeSuspend(
    initialCapacity: Int = DEFAULT_CAPACITY
): suspend (A, B, C, D) -> R = memorizeSuspend(ConcurrentHashMap(initialCapacity))

fun <A, B, C, D, R> (suspend (A, B, C, D) -> R).memorizeSuspend(
    cache: ConcurrentMap<Quadruple<A, B, C, D>, R>
): suspend (A, B, C, D) -> R = { a: A, b: B, c: C, d: D ->
    cache.getOrPut(Quadruple(a, b, c, d)) { this(a, b, c, d) }
}

fun <A, B, C, D, E, R> (suspend (A, B, C, D, E) -> R).memorizeSuspend(
    initialCapacity: Int = DEFAULT_CAPACITY
): suspend (A, B, C, D, E) -> R = memorizeSuspend(ConcurrentHashMap(initialCapacity))

fun <A, B, C, D, E, R> (suspend (A, B, C, D, E) -> R).memorizeSuspend(
    cache: ConcurrentMap<Quintuple<A, B, C, D, E>, R>
): suspend (A, B, C, D, E) -> R = { a: A, b: B, c: C, d: D, e: E ->
    cache.getOrPut(Quintuple(a, b, c, d, e)) { this(a, b, c, d, e) }
}
