package micro.apps.pipeline

/* ktlint-disable no-wildcard-imports */
import micro.apps.kbeam.*
import micro.apps.kbeam.io.readTextFile
import micro.apps.kbeam.io.writeText
import mu.KotlinLogging
import org.apache.beam.sdk.options.*

/* ktlint-enable no-wildcard-imports */

interface WordCountOptions : PipelineOptions {
    @get:Description("Path of the file to read from")
    @get:Default.String("gs://apache-beam-samples/shakespeare/kinglear.txt")
    @get:Validation.Required
    var inputFile: ValueProvider<String>

    @get:Description("Path of the file output")
    @get:Default.String("gs://apache-beam-samples/shakespeare/output/output.txt")
    @get:Validation.Required
    var output: ValueProvider<String>
}

const val TOKENIZER_PATTERN = "[^\\p{L}]+"

private val logger = KotlinLogging.logger {}

object WordCountPipeline {

    @JvmStatic
    fun main(args: Array<String>) {

        logger.info { "My Args: ${args.contentToString()}" }

        val (pipe, options) = PipeBuilder.from<WordCountOptions>(args)

        logger.underlyingLogger.atInfo()
            .addKeyValue("runner", options.runner.name)
            .addKeyValue("jobName", options.jobName)
            .log("Started job with:")

        pipe.readTextFile { filePattern = options.inputFile }
            .flatMap { it.split(Regex(TOKENIZER_PATTERN)).filter { it.isNotEmpty() }.toList() }
            .countPerElement()
            .map { "${it.key}: ${it.value}" }
            .parDo<String, String>(name = "just to demo logging") {
                logger.debug { "THIS IS atFinest MESSAGE" }
                output(element)
            }
            .writeText { path = options.output }

        pipe.run()
    }
}
