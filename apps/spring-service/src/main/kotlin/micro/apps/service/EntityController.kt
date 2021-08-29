package micro.apps.service

import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.metrics.Meter
import io.opentelemetry.api.trace.Tracer
import kotlinx.coroutines.delay
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.model.Person
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import kotlin.system.measureTimeMillis

val DIMENSION_API_NAME: AttributeKey<String> = AttributeKey.stringKey("apiName")
val DIMENSION_STATUS_CODE: AttributeKey<String> = AttributeKey.stringKey("statusCode")

@ExperimentalSerializationApi
@CrossOrigin
@RestController
class EntityController(
    private val repository: EntityRepository,
    private val tracer: Tracer,
    private val meter: Meter,
) {

    var counter = meter.createCounter("entity_request_counter", "Entity API request counter")
    var recorder = meter.createHistogram("entity_request_latency", "Entity API latency time", "ms")

    @GetMapping("/entity/{id}")
    suspend fun findOne(@PathVariable("id") id: String): Person? {
        delay(2000)
        return repository.get(id)
    }

    @GetMapping("/intro")
    suspend fun hello(): String {
        val timeInMillis = measureTimeMillis {
            val span = tracer.spanBuilder("time").startSpan()
            span.setAttribute("what.am.i", "Tu es une legume")
            // span.setStatus(StatusCode.ERROR)
            counter.add(1, Attributes.of(DIMENSION_API_NAME, "intro", DIMENSION_STATUS_CODE, "200"))
            delay(1000)
            span.end()
        }
        recorder.record(timeInMillis)
        return "Hello"
    }
}
