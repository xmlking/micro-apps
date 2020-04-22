package micro.apps.pipeline

import com.google.common.flogger.FluentLogger
import com.sksamuel.avro4k.Avro
import kotlinx.serialization.protobuf.ProtoBuf
import micro.apps.core.LogDefinition.Companion.config
/* ktlint-disable no-wildcard-imports */
import micro.apps.kbeam.*
import micro.apps.kbeam.io.*
import micro.apps.model.*
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

object StreamingPipeline {
    @JvmStatic
    private val logger: FluentLogger = FluentLogger.forEnclosingClass().config()

    @JvmStatic
    fun main(args: Array<String>) {

        logger.atConfig().log("My Args: %s", args)

        val (pipe, options) = PipeBuilder.from<WordCountOptions>(args)

        logger.atInfo().log(
            """Runner: ${options.runner.name}
                    |Job name: ${options.jobName}
                """.trimMargin()
        )

        val schema = Avro.default.schema(Person.serializer())
        println(schema.toString(true))
        val aaa = Person(firstName = "sumo", lastName = "demo", email = "sumo@demo", phone = "000-000-0000", age = 99)
        val dump2 = ProtoBuf.dump<Person>(Person.serializer(), aaa)
        println(ProtoBuf.load<Person>(Person.serializer(), dump2))

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
