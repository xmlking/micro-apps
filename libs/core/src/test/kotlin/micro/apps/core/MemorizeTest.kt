package micro.apps.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContainValue
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis


class MemorizeTest : FunSpec({
    val MAX_CACHE_SIZE = 40

    test("test memorize") {
        // Considerably inefficient implementation for test purposes
        fun fib(k: Int): Long = when (k) {
            0 -> 1
            1 -> 1
            else -> fib(k - 1) + fib(k - 2)
        }

        val map = HashMap<Int, Long>(MAX_CACHE_SIZE)
        val memoizedFib = ::fib.memorize(cache = map)

        println("Test for range 1-$MAX_CACHE_SIZE")
        print("1st iteration: ")
        var totalMs = measureTimeMillis {
            (1..MAX_CACHE_SIZE).forEach {
                memoizedFib(it)
            }
        }
        print("$totalMs ms")
        println()
        println()
        print("2nd iteration: ")

        totalMs = measureTimeMillis {
            (1..MAX_CACHE_SIZE).forEach {
                memoizedFib(it)
            }
        }
        print("$totalMs ms")

        map shouldHaveSize 40
        map shouldHaveKey 40
        map shouldContainValue 165580141

        // Free cache
        map.clear()
    }


})

class MemorizeSpecTest : FunSpec({
    val counter = AtomicInteger(0)
    fun concatSpaced(first: String, second: String, third: String, fourth: String): String =
        "$first $second $third $fourth".also { counter.incrementAndGet() }

    suspend fun concatSpacedS(first: String, second: String, third: String, fourth: String): String =
        concatSpaced(first, second, third, fourth)

    fun givenMemoizedFunction() = ::concatSpaced.memorize()
    fun givenMemoizedSuspendFunction() = ::concatSpacedS.memorizeSuspend()

    test("memoized function should call only once with same params") {
        val (a, b, c) = listOf("sumo-a", "sumo-b", "sumo-c")
        val (d, e, f) = listOf("sumo-d", "sumo-e", "sumo-f")
        val concatSpacedMemoized = givenMemoizedFunction()
        concatSpacedMemoized(a, b, c, d) shouldBe "$a $b $c $d"
        concatSpacedMemoized(b, c, a, d) shouldBe "$b $c $a $d"
        concatSpacedMemoized(a, d, e, c) shouldBe "$a $d $e $c"
        concatSpacedMemoized(a, d, d, f) shouldBe "$a $d $d $f"
        concatSpacedMemoized(a, b, c, d) shouldBe "$a $b $c $d"
        concatSpacedMemoized(a, b, c, d) shouldBe "$a $b $c $d"
        counter.get() shouldBe 4
    }

    test("memoized suspend function should call only once with same params") {
        val (a, b, c) = listOf("sumo-a", "sumo-b", "sumo-c")
        val (d, e, f) = listOf("sumo-d", "sumo-e", "sumo-f")
        val concatSpacedMemoized = givenMemoizedSuspendFunction()
        concatSpacedMemoized(a, b, c, d) shouldBe "$a $b $c $d"
        concatSpacedMemoized(b, c, a, d) shouldBe "$b $c $a $d"
        concatSpacedMemoized(a, d, e, c) shouldBe "$a $d $e $c"
        concatSpacedMemoized(a, d, d, f) shouldBe "$a $d $d $f"
        concatSpacedMemoized(a, b, c, d) shouldBe "$a $b $c $d"
        concatSpacedMemoized(a, b, c, d) shouldBe "$a $b $c $d"
        counter.get() shouldBe 4
    }
})
