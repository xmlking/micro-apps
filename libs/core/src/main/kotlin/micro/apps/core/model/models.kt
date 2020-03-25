package micro.apps.core.model

import java.io.Serializable
import java.math.BigDecimal
import java.time.Instant
/* ktlint-disable no-wildcard-imports */
import java.util.*
/* ktlint-enable no-wildcard-imports */

// ----------------
//  for Kotlin Gradle NoArg plugin
// ----------------
annotation class NoArg

@NoArg
class Test(val id: Int)

sealed class Message : Serializable

/** Event Message Class **/
data class DataMessage(
    val id: UUID,
    val name: String,
    val location: String = "CA",
    val price: BigDecimal,
    val timestamp: Instant = Instant.now()
) : Message(), Serializable

data class Fruit(var name: String = "", var description: String = "")
