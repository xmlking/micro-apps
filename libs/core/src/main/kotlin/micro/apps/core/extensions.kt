package micro.apps.core

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale
import java.util.Optional


/**
 * Format an Instant as an ISO8601 timestamp
 */
fun Instant.toISO8601(): String = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    .withZone(ZoneId.of("UTC")).format(this)

/**
 * Format a LocalDate in ISO8601 format
 */
fun LocalDate.toISO8601(): String = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    .withZone(ZoneId.of("UTC")).format(this)

/**
 * Find the day of week of an Instant
 */
fun Instant.firstDayOfWeek(): LocalDate = this.atZone(ZoneId.of("UTC")).toLocalDate()
    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

/**
 * Execute a block of code when a variable is not null
 */
fun <T : Any> T?.whenNotNull(callback: (it: T) -> Unit) {
    if (this != null) callback(this)
}

/**
 * Execute a block of code when a variable is null
 */
fun <T : Any> T?.whenNull(callback: () -> Unit) {
    this ?: callback()
}

/**
 * Create PST Date
 */
fun dateOf(year: Int, month: Int, day: Int): Date = Date.from(LocalDate.of(year, month, day).atStartOfDay(ZoneId.of("America/Los_Angeles")).toInstant())

/**
 * convert java.util.Optional to kotlin Nullable and vice versa
 * val msg: Something? = optional.toNullable()  // the type is enforced
 */
fun <T : Any> Optional<T>.toNullable(): T? = orElse(null)
fun <T : Any> T?.toOptional(): Optional<T> = Optional.ofNullable(this)

/**
 * Get property value dynamically from Any object.
 * Usage:
 * val p = Person("Jane", true)
 * val name = p.getThroughReflection<String>("name")
 * val employed = p.getThroughReflection<Boolean>("employed")
 */
inline fun <reified T : Any> Any.getThroughReflection(propertyName: String): T? {
    val getterName = "get" + propertyName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    return try {
        javaClass.getMethod(getterName).invoke(this) as? T
    } catch (e: NoSuchMethodException) {
        null
    }
}
