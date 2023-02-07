package micro.apps.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import java.util.concurrent.atomic.AtomicInteger

class MemorizeCounterTest : FunSpec({

    val counter = AtomicInteger(0)
    fun concatSpaced(first: String, second: String, third: String, fourth: String): String =
        "$first $second $third $fourth".also { counter.incrementAndGet() }

    suspend fun concatSpacedS(first: String, second: String, third: String, fourth: String): String =
        concatSpaced(first, second, third, fourth)

    fun givenMemoizedFunction() = ::concatSpaced.memorize()

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
})

class MemorizeCounterSuspendTest : FunSpec({

    val counter = AtomicInteger(0)
    fun concatSpaced(first: String, second: String, third: String, fourth: String): String =
        "$first $second $third $fourth".also { counter.incrementAndGet() }

    suspend fun concatSpacedS(first: String, second: String, third: String, fourth: String): String =
        concatSpaced(first, second, third, fourth)

    fun givenMemoizedSuspendFunction() = ::concatSpacedS.memorizeSuspend()

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
