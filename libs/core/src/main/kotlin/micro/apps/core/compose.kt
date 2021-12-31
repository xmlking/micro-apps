package micro.apps.core

typealias Predicate<T> = (T) -> Boolean

infix fun <A> Predicate<A>.and(f: Predicate<A>): Predicate<A> = { x: A -> this(x) && f(x) }
infix fun <A> Predicate<A>.or(f: Predicate<A>): Predicate<A> = { x: A -> this(x) || f(x) }
//inline infix fun <A> Predicate<A>.or(crossinline f: Predicate<A>): Predicate<A> = { x: A -> this(x) || f(x) }
