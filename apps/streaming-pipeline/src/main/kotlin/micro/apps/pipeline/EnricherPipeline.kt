package micro.apps.pipeline

/* ktlint-disable no-wildcard-imports */
import com.google.common.flogger.FluentLogger
import com.google.common.flogger.MetadataKey.single
import com.sksamuel.avro4k.Avro
import com.sksamuel.avro4k.io.AvroFormat
import micro.apps.core.LogDefinition.Companion.config
import micro.apps.kbeam.PipeBuilder
import micro.apps.kbeam.parDo
import micro.apps.kbeam.toList
import micro.apps.model.Person
import micro.apps.pipeline.config.EndpointConfig
import micro.apps.pipeline.config.TlsConfig
import micro.apps.pipeline.config.config
import org.apache.avro.Schema
import org.apache.beam.runners.dataflow.util.TimeUtil
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.windowing.AfterWatermark
import org.apache.beam.sdk.transforms.windowing.FixedWindows
import org.apache.beam.sdk.transforms.windowing.Window
import org.joda.time.Duration

/* ktlint-enable no-wildcard-imports */

/**
 * showcase side-input and split
 */
object EnricherPipeline {
    @JvmStatic
    private val logger: FluentLogger = FluentLogger.forEnclosingClass().config()

    @JvmStatic
    fun main(args: Array<String>) {

        logger.atConfig().log("My Args: %s", args)

        val (pipe, options) = PipeBuilder.from<ClassifierOptions>(args)
        options.isStreaming = true
        
        println(config[TLS.caCert])
        println(config<String>("endpoints.account"))
        println("server.host" in config)
        println(config.containsRequired())
        println(config[Cloud.Dataflow.windowDuration])
        options.windowDuration = options.windowDuration ?: config[Cloud.Dataflow.windowDuration]

        logger.atInfo()
            .with(single("Runner", String::class.java), options.runner.name)
            .with(single("JobName", String::class.java), options.jobName)
            .with(single("windowDuration", String::class.java), options.windowDuration)
            .with(single("pubsubRootUrl", String::class.java), options.pubsubRootUrl)
            .log("Started job with:")

        val schema = Schema.Parser().parse(javaClass.getResourceAsStream("/data/person.avsc"))

        // load dummy `keys` to use as `side input` for decryption
        val keys = pipe.apply(Create.of(listOf("aaa", "bbb"))).toList()

        val input = pipe
            .apply("Read new Data from PubSub", PubsubIO.readMessagesWithAttributes().fromSubscription(options.inputSubscription))
            // Batch events into 5 minute windows
            .apply("Batch Events, windowDuration: ${options.windowDuration}", Window.into<PubsubMessage>(
                FixedWindows.of(TimeUtil.fromCloudDuration(options.windowDuration)))
                .triggering(AfterWatermark.pastEndOfWindow())
                .discardingFiredPanes()
                .withAllowedLateness(Duration.standardSeconds(300)))
            /*
            .apply("convert PubSub to Person", MapElements.via(PubsubToPerson())).setCoder(AvroPersonCoder())

            .parDo<Person, Person>("decrypt and enrich record") {
                println(element)
                element
            }
            */

            // decrypting and enrich record
            .parDo<PubsubMessage, PubsubMessage>(
                "decrypt and enrich record",
                sideInputs = listOf(keys)) {
                // val per = Avro.default.load(Person.serializer(), element.payload)
                val person = Avro.default.openInputStream(Person.serializer()) {
                    format = AvroFormat.BinaryFormat
                    writerSchema = Avro.default.schema(Person.serializer())
                }.from(element.payload).nextOrThrow()

                println(person)
                element
            }

        input
            // convert GenericRecord to PubsubMessage
            // .apply(MapElements.via(PersonToPubsub()))
            // write back to PubSub
            .apply("Write PubSub Events", PubsubIO.writeMessages().to(options.outputTopic))

        pipe.run()
    }
}
