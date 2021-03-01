package micro.apps.pipeline

import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.kbeam.PipeBuilder
import micro.apps.kbeam.toList
import micro.apps.kbeam.transforms.AvroToPubsub
import micro.apps.kbeam.transforms.PubsubToAvro
import micro.apps.pipeline.transforms.EnrichFn
import mu.KotlinLogging
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.beam.runners.dataflow.util.TimeUtil
import org.apache.beam.sdk.coders.AvroCoder
import org.apache.beam.sdk.io.AvroIO
import org.apache.beam.sdk.io.fs.EmptyMatchTreatment
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.MapElements
import org.apache.beam.sdk.transforms.ParDo
import org.apache.beam.sdk.transforms.windowing.AfterWatermark
import org.apache.beam.sdk.transforms.windowing.FixedWindows
import org.apache.beam.sdk.transforms.windowing.Window
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.TupleTagList
import org.joda.time.Duration

private val logger = KotlinLogging.logger {}

@ExperimentalSerializationApi
object IngestionPipeline {

    @JvmStatic
    fun main(args: Array<String>) {

        logger.info { "My Args: ${args.contentToString()}" }

        val (pipe, options) = PipeBuilder.from<IngestionOptions>(args)
        options.isStreaming = options.stream

        logger.underlyingLogger.atInfo()
            .addKeyValue("runner", options.runner.name)
            .addKeyValue("jobName", options.jobName)
            .log("Started job with:")
        logger.info { options }

        val schema = Schema.Parser().parse(javaClass.getResourceAsStream("/data/person.avsc"))

        // create dummy `keys` to use as `side input` for decryption
        val keysView = pipe.apply(Create.of(listOf("aaa", "bbb"))).toList()

        lateinit var input: PCollection<GenericRecord>
        if (options.isStreaming) {
            logger.info { "Reading from PubSub" }
            input = pipe
                .apply("Read new Data from PubSub", PubsubIO.readMessagesWithAttributes().fromSubscription(options.inputSubscription))
                .apply(
                    "Batch records with windowDuration: ${options.windowDuration}",
                    Window.into<PubsubMessage>(
                        FixedWindows.of(TimeUtil.fromCloudDuration(options.windowDuration)!!)
                    )
                        .triggering(AfterWatermark.pastEndOfWindow())
                        .discardingFiredPanes()
                        .withAllowedLateness(Duration.standardSeconds(300))
                )
                .apply("convert Pubsub to GenericRecord", MapElements.via(PubsubToAvro(schema))).setCoder(AvroCoder.of(schema))
        } else {
            logger.info { "Reading from GCS" }
            input = pipe.apply(
                "Read New Data from GCS",
                AvroIO.readGenericRecords(schema).from(options.inputPath)
                    .withEmptyMatchTreatment(EmptyMatchTreatment.ALLOW)
                    .withHintMatchesManyFiles()
            )
        }

        val output = input.apply(
            "decrypt and enrich record",
            ParDo.of(EnrichFn(keysView))
                .withSideInputs(keysView)
                .withOutputTags(successTag, TupleTagList.of(errorTag))
        )

        if (options.isStreaming) {
            logger.info { "Writing to PubSub" }
            output.get(successTag)
                .apply("convert GenericRecord to PubsubMessage", MapElements.via(AvroToPubsub()))
                .apply("write success records to SuccessTopic", PubsubIO.writeMessages().to(options.outputSuccessTopic))

            output.get(errorTag)
                .apply("convert GenericRecord to PubsubMessage", MapElements.via(AvroToPubsub()))
                .apply("write error records to FailureTopic", PubsubIO.writeMessages().to(options.outputFailureTopic))
        } else {
            logger.info { "Writing to GCS" }
            output.get(successTag)
                .apply("write success records to SuccessPath", AvroIO.writeGenericRecords(schema).to(options.outputSuccessPath))
            output.get(successTag)
                .apply("write error records to FailurePath", AvroIO.writeGenericRecords(schema).to(options.outputFailurePath))
        }

        pipe.run()
    }
}
