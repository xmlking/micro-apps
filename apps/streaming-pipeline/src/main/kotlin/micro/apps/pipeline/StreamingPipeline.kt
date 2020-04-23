package micro.apps.pipeline

/* ktlint-disable no-wildcard-imports */
import com.google.common.flogger.FluentLogger
import com.google.common.flogger.MetadataKey
import com.sksamuel.avro4k.Avro
import micro.apps.core.LogDefinition.Companion.config
import micro.apps.kbeam.PipeBuilder
import micro.apps.kbeam.map
import micro.apps.model.Person
import org.apache.avro.Schema
import org.apache.beam.runners.dataflow.util.TimeUtil
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubOptions
import org.apache.beam.sdk.options.*
import org.apache.beam.sdk.transforms.windowing.FixedWindows
import org.apache.beam.sdk.transforms.windowing.Window

/* ktlint-enable no-wildcard-imports */

interface MyStreamingOptions : ApplicationNameOptions, PipelineOptions, StreamingOptions, PubsubOptions, GcpOptions {
    @get:Description("The Cloud Pub/Sub topic to read from.")
    @get:Default.String("projects/{PROJECT_ID}/topics/windowed-files")
    @get:Validation.Required
    var inputTopic: ValueProvider<String>

    @get:Description("The Cloud Pub/Sub topic to write to.")
    @get:Default.String("gs://apache-beam-samples/shakespeare/output/output.txt")
    @get:Validation.Required
    var outputTopic: ValueProvider<String>

    @get:Description(
        """The window duration in which data will be written. Defaults to 5m.
                Allowed formats are:
                Ns (for seconds, example: 5s),
                Nm (for minutes, example: 12m),
                Nh (for hours, example: 2h).")""")
    @get:Default.String("5m")
    var windowDuration: ValueProvider<String>
}

const val TOKENIZER_PATTERN = "[^\\p{L}]+"

object StreamingPipeline {
    @JvmStatic
    private val logger: FluentLogger = FluentLogger.forEnclosingClass().config()

    @JvmStatic
    fun main(args: Array<String>) {

        logger.atConfig().log("My Args: %s", args)

        val (pipe, options) = PipeBuilder.from<MyStreamingOptions>(args)
        options.setStreaming(true)

        logger.atInfo()
            .with(MetadataKey.single("Runner", String::class.java), options.runner.name)
            .with(MetadataKey.single("JobName", String::class.java), options.jobName)
            .log("Started job with:")

        val schema = Schema.Parser().parse(javaClass.getResourceAsStream("/data/person.avsc"))
        val schema1 = Avro.default.schema(Person.serializer())
        logger.atInfo().with(MetadataKey.single("text", Schema::class.java), schema).log()
        println(schema1)

        pipe
            .apply(
                "Read PubSub Events",
                PubsubIO.readMessagesWithAttributes().fromTopic(options.inputTopic))
            .map {
                println(it.payload)
                it.payload.toString()
            } // it.attributeMap
            // .apply("Map to Archive", ParDo.of(new PubsubMessageToArchiveDoFn()))
            .apply(
                " Window",
                // Window.into<String>(FixedWindows.of(parseDuration(options.windowDuration))))
                // Window.into<String>(FixedWindows.of(parseDuration("5s"))))
                Window.into<String>(FixedWindows.of(TimeUtil.fromCloudDuration("60s"))))

        pipe.run()
    }
}
