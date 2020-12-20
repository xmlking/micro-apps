package micro.apps.pipeline

import io.kotest.core.spec.style.FunSpec

class Junit5Test : FunSpec({

    beforeTest {
        println("Starting test ${it.displayName}!")
    }
    afterTest {
        println("Finished test ${it.a.displayName}!")
    }

    test("person should have message") {
        println("hi...")
    }
})
