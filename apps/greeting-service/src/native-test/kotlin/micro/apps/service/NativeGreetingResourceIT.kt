package micro.apps.service

import io.quarkus.test.junit.NativeImageTest
import kotlin.time.ExperimentalTime

@ExperimentalTime
@NativeImageTest
open class NativeGreetingResourceIT : GreetingResourceTest()
