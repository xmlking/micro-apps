package micro.apps.pipeline

/* ktlint-disable no-wildcard-imports */

import micro.apps.kbeam.PipeBuilder
import micro.apps.kbeam.toList
import micro.apps.kbeam.transforms.AvroToPubsub
import micro.apps.kbeam.transforms.PubsubToAvro
import micro.apps.pipeline.transforms.EnrichFn
import mu.KotlinLogging
import org.apache.avro.Schema
import org.apache.beam.runners.dataflow.util.TimeUtil
import org.apache.beam.sdk.coders.AvroCoder
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.ParDo
import org.apache.beam.sdk.transforms.windowing.AfterWatermark
import org.apache.beam.sdk.transforms.windowing.FixedWindows
import org.apache.beam.sdk.transforms.windowing.Window
import org.apache.beam.sdk.values.TupleTagList
import org.joda.time.Duration

/* ktlint-enable no-wildcard-imports */

/**
 * showcase side-input and split
 */
private val logger = KotlinLogging.logger {}

object EnricherPipeline {

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

        // create dummy `keys` to use as `side input` for decryption
        val keysView = pipe.apply(Create.of(listOf("aaa", "bbb"))).toList()

        val input = pipe
            .apply("Read new Data from PubSub", PubsubIO.readMessagesWithAttributes().fromSubscription(options.inputSubscription))
            // Batch events into 5 minute windows
            .apply("Batch Events, windowDuration: ${options.windowDuration}", Window.into<PubsubMessage>(
                FixedWindows.of(TimeUtil.fromCloudDuration(options.windowDuration)))
                .triggering(AfterWatermark.pastEndOfWindow())
                .discardingFiredPanes()
                .withAllowedLateness(Duration.standardSeconds(300)))

            .apply("convert Pubsub to GenericRecord", MapElements.via(PubsubToAvro(schema))).setCoder(AvroCoder.of(schema))

            val output = input.apply("decrypt and enrich record", ParDo.of(EnrichFn(keysView))
                .withSideInputs(keysView)
                .withOutputTags(successTag, TupleTagList.of(errorTag))
            )

        output.get(successTag)
            .apply("convert GenericRecord to PubsubMessage", MapElements.via(AvroToPubsub()))
            .apply("write success events to SuccessTopic", PubsubIO.writeMessages().to(options.outputSuccessTopic))

        output.get(errorTag)
            .apply("convert GenericRecord to PubsubMessage", MapElements.via(AvroToPubsub()))
            .apply("write error events to FailureTopic", PubsubIO.writeMessages().to(options.outputFailureTopic))

        pipe.run()
    }
}
