package micro.apps.core.util

import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

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
