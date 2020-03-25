package micro.apps.greeting

import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class GreetingService {

    fun greeting(name: String): String {
        return "hello, $name"
    }

}
