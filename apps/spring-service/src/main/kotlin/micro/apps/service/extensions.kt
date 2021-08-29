package micro.apps.service

import io.opentelemetry.api.metrics.LongCounter
import io.opentelemetry.api.metrics.LongHistogram
import io.opentelemetry.api.metrics.Meter
import io.opentelemetry.api.trace.Span
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.Context

/**
 * opentelemetry
 */

fun Tracer.startSpanWithParent(spanName: String): Span =
    this.spanBuilder(spanName)
        .setParent(Context.current().with(Span.current()))
        .startSpan()

fun Meter.createCounter(name: String, description: String = name): LongCounter =
    this.counterBuilder(name).setDescription(description).setUnit("one").build()

fun Meter.createHistogram(name: String, description: String = name): LongHistogram =
    this.histogramBuilder(name).setDescription(description).ofLongs().setUnit("ms").build()

/**
 * Usage:
 override suspend fun fetchTestRunSummary(publicId: PublicId): TestRunSummary? =
withContext(Dispatchers.IO) {
val span = tracer.startSpanWithParent("projektor.fetchTestRunSummary")

val testRunSummary = dslContext
.select(TEST_RUN.PUBLIC_ID.`as`("id"))
.select(TEST_RUN.fields().filterNot { it.name == "id" }.toList())
.from(TEST_RUN)
.where(TEST_RUN.PUBLIC_ID.eq(publicId.id))
.fetchOneInto(TestRunSummary::class.java)

span.end()

testRunSummary
}
 */
