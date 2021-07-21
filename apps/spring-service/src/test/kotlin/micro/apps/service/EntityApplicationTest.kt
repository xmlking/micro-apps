package micro.apps.service

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.env.Environment

@SpringBootTest
class EntityApplicationTest(private val environment: Environment) : FunSpec({
    test("application starts") {
        environment.shouldNotBeNull()
    }
})
