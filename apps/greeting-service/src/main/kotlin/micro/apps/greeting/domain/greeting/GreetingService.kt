package micro.apps.greeting.domain.greeting

import micro.apps.model.Greeting
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GreetingService {

    fun greeting(name: String): Greeting {
        return Greeting("hello, $name")
    }
}
