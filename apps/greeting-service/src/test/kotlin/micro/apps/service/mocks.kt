package micro.apps.service

import io.quarkus.test.Mock
import micro.apps.model.Greeting
import micro.apps.service.domain.greeting.GreetingService
import javax.enterprise.context.ApplicationScoped

@Mock
@ApplicationScoped
class MockedService : GreetingService() {
    override fun greeting(name: String) = Greeting("Welcome")
}
