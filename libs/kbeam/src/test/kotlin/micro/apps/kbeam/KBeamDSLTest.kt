package micro.apps.kbeam

import micro.apps.kbeam.io.writeText
import org.apache.beam.sdk.io.Compression
import org.apache.beam.sdk.options.ValueProvider.StaticValueProvider
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.values.KV
import org.junit.Rule
import java.io.Serializable
import kotlin.test.Test

class KBeamDSLTest : Serializable {

    @Rule
    @Transient
    @JvmField
    val pipeline = TestPipeline.create().enableAbandonedNodeEnforcement(false)

    @Test
    fun ShouldRunCoGroupByKeyDSLTest() {
        val list1 = (1..20).map { KV.of("Key_${it % 10})", it) }
        val list2 = (0..9).flatMap { ('a'..'z').map { KV.of("Key_$it)", "$it") } }
        val plist1 = pipeline.apply("Create List1", Create.of(list1))
        val plist2 = pipeline.apply("Create List2", Create.of(list2))

        val group = pipeline.coGroupByKey(plist1, plist2) { key, left, right ->
            listOf("$key : $left $right")
        }

        group.parDo<String, Void> {
            println(element)
        }

        group.writeText {
            path = StaticValueProvider.of("build/output/test")
            compression = Compression.GZIP
            numShards = 1
            header = "#Header"
            footer = "#Footer"
        }
        pipeline.run().waitUntilFinish()
    }
}
