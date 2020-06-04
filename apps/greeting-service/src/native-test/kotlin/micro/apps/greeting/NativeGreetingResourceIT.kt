package micro.apps.greeting

import io.quarkus.test.junit.NativeImageTest
import kotlin.time.ExperimentalTime

@ExperimentalTime
@NativeImageTest
open class NativeGreetingResourceIT : GreetingResourceTest()
