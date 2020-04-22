package micro.apps.core.model

import java.io.Serializable as JavaSerializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

// ----------------
//  for Kotlin Gradle NoArg plugin
// ----------------
annotation class NoArg

@NoArg
class Test(val id: Int)

sealed class Message : JavaSerializable

/** Event Message Class **/
data class DataMessage(
    val id: UUID,
    val name: String,
    val location: String = "CA",
    val price: BigDecimal,
    val timestamp: Instant = Instant.now()
) : Message(), JavaSerializable

data class Fruit(var name: String = "", var description: String = "")

data class Greeting(val message: String = "")
