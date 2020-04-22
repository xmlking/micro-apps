package micro.apps.pipeline

import java.io.Serializable
import kotlin.test.Test
import micro.apps.kbeam.countPerElement
import micro.apps.kbeam.flatMap
import micro.apps.kbeam.map
import org.apache.beam.sdk.testing.PAssert
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.values.TypeDescriptor
import org.junit.Rule

const val TOKENIZER_PATTERN = "[^\\p{L}]+"

class StreamingPipelineTest : Serializable {

    @Rule
    @Transient
    @JvmField
    val pipeline = TestPipeline.create()

    @Test
    fun ShouldProcessWordCountByGroupByKey() {

        val results = pipeline
            .apply(Create.of(
                "apache beam in kotlin",
                "this is kotlin",
                "awesome kotlin",
                ""))
            .setTypeDescriptor(TypeDescriptor.of(String::class.java))
            .flatMap { it.split(Regex(TOKENIZER_PATTERN)).filter { it.isNotEmpty() }.toList() }
            .countPerElement()
            .map { "${it.key}: ${it.value}" }

        PAssert.that(results).containsInAnyOrder(
            "this: 1", "apache: 1", "beam: 1", "is: 1", "kotlin: 3", "awesome: 1", "in: 1")

        pipeline.run()
    }
}
