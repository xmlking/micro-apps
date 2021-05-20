package micro.apps.greeting

import io.quarkus.test.Mock
import micro.apps.greeting.domain.greeting.GreetingService
import micro.apps.model.Greeting
import javax.enterprise.context.ApplicationScoped

@Mock
@ApplicationScoped
class MockedService : GreetingService() {
    override fun greeting(name: String) = Greeting("Welcome")
}
