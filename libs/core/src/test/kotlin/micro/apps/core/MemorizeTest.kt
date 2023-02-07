package micro.apps.core

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContainValue
import io.kotest.matchers.maps.shouldHaveKey
import io.kotest.matchers.maps.shouldHaveSize
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
