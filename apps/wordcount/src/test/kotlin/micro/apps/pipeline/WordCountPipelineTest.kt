package micro.apps.pipeline

import micro.apps.shared.dsl.countPerElement
import micro.apps.shared.dsl.flatMap
import micro.apps.shared.dsl.map
import org.apache.beam.sdk.testing.PAssert
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.transforms.Create
import org.junit.Rule
import java.io.Serializable
import kotlin.test.Test

const val TOKENIZER_PATTERN = "[^\\p{L}]+"

class WordCountPipelineTest : Serializable {

    @Rule
    @Transient
    @JvmField
    val pipeline = TestPipeline.create()

    @Test
    fun `Should process WordCount by GroupByKey`() {

        val results = pipeline
                .apply(Create.of(
                        "apache beam in kotlin",
                        "this is kotlin",
                        "awesome kotlin",
                        ""))
                .flatMap { it.split(Regex(TOKENIZER_PATTERN)).filter { it.isNotEmpty() }.toList() }
                .countPerElement()
                .map { "${it.key}: ${it.value}" }

        PAssert.that(results).containsInAnyOrder(
                "this: 1", "apache: 1", "beam: 1", "is: 1", "kotlin: 3", "awesome: 1", "in: 1")

        pipeline.run()
    }
}