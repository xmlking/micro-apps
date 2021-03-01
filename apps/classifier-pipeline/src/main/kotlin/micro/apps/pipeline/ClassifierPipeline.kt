package micro.apps.pipeline

import micro.apps.kbeam.PipeBuilder
import micro.apps.kbeam.parDo
import micro.apps.kbeam.split
import micro.apps.kbeam.transforms.AvroToPubsub
import micro.apps.kbeam.transforms.PubsubToAvro
import mu.KotlinLogging
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.beam.runners.dataflow.util.TimeUtil
import org.apache.beam.sdk.coders.AvroCoder
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.windowing.AfterWatermark
import org.apache.beam.sdk.transforms.windowing.FixedWindows
import org.apache.beam.sdk.transforms.windowing.Window
import org.joda.time.Duration

private val logger = KotlinLogging.logger {}

object ClassifierPipeline {

    @JvmStatic
    fun main(args: Array<String>) {

        logger.info { "My Args: ${args.contentToString()}" }

        val (pipe, options) = PipeBuilder.from<ClassifierOptions>(args)
        options.isStreaming = true

        logger.underlyingLogger.atInfo()
            .addKeyValue("runner", options.runner.name)
            .addKeyValue("jobName", options.jobName)
            .log("Started job with:")
        logger.info { options }

        val schema = Schema.Parser().parse(javaClass.getResourceAsStream("/data/person.avsc"))

        val input = pipe
            .apply("Read new Data from PubSub", PubsubIO.readMessagesWithAttributes().fromSubscription(options.inputSubscription))
            // Batch events into 5 minute windows
            .apply(
                "Batch Events, windowDuration: ${options.windowDuration}",
                Window.into<PubsubMessage>(
                    FixedWindows.of(TimeUtil.fromCloudDuration(options.windowDuration)!!)
                )
                    .triggering(AfterWatermark.pastEndOfWindow())
                    .discardingFiredPanes()
                    .withAllowedLateness(Duration.standardSeconds(300))
            )

            .apply("convert Pubsub to GenericRecord", MapElements.via(PubsubToAvro(schema))).setCoder(AvroCoder.of(schema))

            .parDo<GenericRecord, GenericRecord>("decrypt and enrich record") {
                println(element)
                println(timestamp)
                println(element.schema)
                for (field in element.schema.fields) {
                    val fieldKey: String = field.name()
                    println("$fieldKey : ${element.get(fieldKey)}, is encrypted? ${field.getProp("encrypted")}")
                }
                // TODO: may a copy, modify and emit
                // output(element.copy(email = "decrypted email"))
                element
            }

        // split records
        val (old, young) = input.split {
            println(it)
            true // it.age >= 20
        }

        old.parDo<GenericRecord, Void> {
            println("Old: $element")
        }

        young.parDo<GenericRecord, Void> {
            println("Young: $element")
        }

        input
            .apply("convert GenericRecord to PubsubMessage", MapElements.via(AvroToPubsub()))
            .apply("write back to PubSub", PubsubIO.writeMessages().to(options.outputSuccessTopic))

        pipe.run()
    }
}
