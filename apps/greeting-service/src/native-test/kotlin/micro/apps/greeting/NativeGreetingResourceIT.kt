package micro.apps.greeting

import io.quarkus.test.junit.NativeImageTest

@NativeImageTest
open class NativeGreetingResourceIT : GreetingResourceTest()
