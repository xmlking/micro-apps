package micro.apps.greeting

import javax.enterprise.context.ApplicationScoped
import micro.apps.model.Greeting

@ApplicationScoped
class GreetingService {

    fun greeting(name: String): Greeting {
        return Greeting("hello, $name")
    }
}
