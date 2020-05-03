package micro.apps.pipeline.transforms

import com.sksamuel.avro4k.Avro
import micro.apps.model.Person
import micro.apps.pipeline.ClassifierOptions
import micro.apps.pipeline.config.Cloud
import micro.apps.pipeline.config.TLS
import micro.apps.pipeline.config.config
import micro.apps.pipeline.errorTag
import mu.KotlinLogging
import org.apache.avro.generic.GenericRecord
import org.apache.beam.sdk.metrics.Metrics
import org.apache.beam.sdk.transforms.DoFn
import org.apache.beam.sdk.values.PCollectionView

private val logger = KotlinLogging.logger {}
class EnrichFn(private val keysView: PCollectionView<List<String>>) : DoFn<GenericRecord, GenericRecord>() {

    private val enrichedPersons = Metrics.counter(EnrichFn::class.java, "enrichedPersons")
    private val failedPersons = Metrics.counter(EnrichFn::class.java, "failedPersons")

    @Transient lateinit var keys: List<String>

    @Setup
    fun setup() {
        logger.debug { "EnrichFn setup()" }
        println(config[TLS.caCert])
        println(config<String>("endpoints.account"))
        println("server.host" in config)
        println(config.containsRequired())
        println(config[Cloud.Dataflow.windowDuration])
    }
    @Teardown
    fun teardown() {
        logger.debug { "EnrichFn teardown()" }
    }
    @StartBundle
    fun startBundle(c: StartBundleContext) {
        val options = c.pipelineOptions.`as`(ClassifierOptions::class.java)
        logger.debug { "EnrichFn startBundle()" }
        logger.debug { options }
    }
    @FinishBundle
    fun finishBundle() {
        logger.debug { "EnrichFn finishBundle()" }
    }

    @ProcessElement
    fun processElement(@Element inPerson: GenericRecord, c: ProcessContext) {
        val inPerson = c.element()
        try {
            keys = c.sideInput(keysView)
            logger.debug { keys }
            var person = Avro.default.fromRecord(Person.serializer(), inPerson)

            if (person.gender.isFemale()) {
                person = person.copy(email = "decrypted@email")
                println("success:")
                println(person)
            } else {
                println("error:")
                println(person)
                throw Exception()
            }

            val outPerson = Avro.default.toRecord(Person.serializer(), person)
            c.output(outPerson)
            enrichedPersons.inc()
        } catch (e: Exception) {
            logger.error(e) { "error  message..." }
            c.output(errorTag, inPerson)
            failedPersons.inc()
        }
    }
}
