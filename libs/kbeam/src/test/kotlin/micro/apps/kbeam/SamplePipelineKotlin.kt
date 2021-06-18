package micro.apps.kbeam

import micro.apps.kbeam.io.readTextFile
import org.apache.beam.sdk.coders.SerializableCoder
import org.apache.beam.sdk.extensions.jackson.ParseJsons
import org.apache.beam.sdk.options.Default
import org.apache.beam.sdk.options.Description
import org.apache.beam.sdk.options.PipelineOptions
import org.apache.beam.sdk.options.ValueProvider.StaticValueProvider
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.values.KV
import org.junit.Rule
import java.io.Serializable
import kotlin.test.Test

interface KMyOptions : PipelineOptions {
    @Description("Custom Option")
    @Default.String("test")
    fun getTest(): String

    fun setTest(value: String)
}

data class KEntry(
    val name: String = "",
    val countryCode: String = "",
    val doubleValue: Double = 0.0,
    val countryName: String = "unknown"
) : Serializable

data class CountryCodeEntry(val code: String = "", val name: String = "") : Serializable

class DSLPipelineTest : Serializable {

    @Rule
    @Transient
    @JvmField
    val pipeline = TestPipeline.create().enableAbandonedNodeEnforcement(false)

    @Test
    fun runTest() {
        val (pipeline, options) = PipeBuilder.from<KMyOptions>(arrayOf("--test=toto"))
        println("$pipeline, $options")

        val countryCodes = pipeline.readTextFile { filePattern = StaticValueProvider.of("src/test/resources/data/country_codes.jsonl") }
            .apply("Parse", ParseJsons.of(CountryCodeEntry::class.java))
            .setCoder(SerializableCoder.of(CountryCodeEntry::class.java))
            .map("Convert to KV") { KV.of(it.code, it.name) }.toMap()

        val test = pipeline.readTextFile(name = "Read Lines") { filePattern = StaticValueProvider.of("src/test/resources/data/test.csv") }
            .filter { it.isNotEmpty() }
            .map(name = "Map to entries") {
                val words = it.split(",")
                KEntry(words[0], words[1], words[2].toDouble())
            }.parDo<KEntry, KEntry>(
                name = "Join with countries",
                sideInputs = listOf(countryCodes)
            ) {
                val countryName = sideInputs[countryCodes][element.countryCode] ?: "unknown"
                output(element.copy(countryName = countryName))
            }

        val (positives, negatives) = test.split {
            println(it)
            it.doubleValue >= 0
        }

        positives.parDo<KEntry, Void> {
            println("Positive: $element")
        }

        negatives.parDo<KEntry, Void> {
            println("Negative: $element")
        }

        pipeline.run().waitUntilFinish()
    }
}
