package micro.apps.pipeline

import com.google.common.flogger.FluentLogger
import micro.apps.core.LogDefinition.Companion.config
/* ktlint-disable no-wildcard-imports */
import micro.apps.kbeam.*
import micro.apps.kbeam.io.readTextFile
import micro.apps.kbeam.io.writeText
import mu.KotlinLogging
import org.apache.beam.sdk.options.*
/* ktlint-enable no-wildcard-imports */

private val logger = KotlinLogging.logger {}
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

        logger.info("My Args: $args")

        val (pipe, options) = PipeBuilder.from<WordCountOptions>(args)

        logger.info {
            """Runner: ${options.runner.name}
                    |Job name: ${options.jobName}
                """.trimMargin()


            pipe.readTextFile { filePattern = options.inputFile }
            .flatMap { it.split(Regex(TOKENIZER_PATTERN)).filter { it.isNotEmpty() }.toList() }
            .countPerElement()
            .map { "${it.key}: ${it.value}" }
            .parDo<String, String>(name = "just to demo logging") {
                logger.atFinest().log("THIS IS atFinest MESSAGE")
                output(element)
            }
            .writeText { path = options.output }

        pipe.run()
    }
}
