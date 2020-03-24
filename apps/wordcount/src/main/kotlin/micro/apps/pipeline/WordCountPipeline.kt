package micro.apps.pipeline

import com.google.common.flogger.FluentLogger
import micro.apps.core.util.LogDefinition.Companion.config
/* ktlint-disable no-wildcard-imports */
import micro.apps.kbeam.*
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

object WordCountPipeline {
    @JvmStatic
    private val logger: FluentLogger = FluentLogger.forEnclosingClass().config()

    @JvmStatic
    fun main(args: Array<String>) {

        logger.atConfig().log("My Args: %s", args)

        val (pipe, options) = KPipeline.from<WordCountOptions>(args)

        logger.atInfo().log(
            """Runner: ${options.runner.name}
                    |Job name: ${options.jobName}
                """.trimMargin()
        )

        pipe.fromText(path = options.inputFile)
            .flatMap { it.split(Regex(TOKENIZER_PATTERN)).filter { it.isNotEmpty() }.toList() }
            .countPerElement()
            .map { "${it.key}: ${it.value}" }
            .parDo<String, String>(name = "just to demo logging") {
                logger.atFinest().log("THIS IS atFinest MESSAGE")
                output(element)
            }
            .toText(filename = options.output)

        pipe.run()
    }
}
