package micro.apps.pipeline

import com.google.common.collect.ImmutableMap
import com.sksamuel.avro4k.Avro
import java.io.Serializable
import kotlin.test.Ignore
import kotlin.test.Test
import micro.apps.kbeam.coder.AvroToPubsubMessage
import micro.apps.model.Person
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.beam.sdk.coders.AvroCoder
import org.apache.beam.sdk.io.AvroIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubOptions
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.MapElements
import org.junit.Rule

// use Avro.default.fromRecord(serializer, rec) , Avro.default.toRecord Avro.default.toRecord(serializer, obj) to convert GenericRecord <==> Data Class
class PubSubProducerTest : Serializable {

    @Rule
    @Transient
    @JvmField
    val pipeline = TestPipeline.create()

    @Test @Ignore // TODO: remove @Ignore to use it
    fun generateTestData() {
        val options = TestPipeline.testingPipelineOptions()
        options.`as`(PubsubOptions::class.java).pubsubRootUrl = "http://localhost:8085"

        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)

        // sample data
        val records: List<GenericRecord> = listOf(
            Avro.default.toRecord(serializer, Person(firstName = "sumo1", lastName = "demo1", email = "sumo1@demo.com", phone = "0000000000", age = 99)),
            Avro.default.toRecord(serializer, Person(firstName = "sumo2", lastName = "demo1", email = "sumo2@demo.com", phone = "1111111111", age = 99, valid = true))
        )

        val attributes = ImmutableMap.builder<String, String>()
            .put("timestamp", "")
            .put("fingerprint", "fingerprint")
            .put("uuid", "uuid")
            .build() // Collections.emptyMap()

        pipeline.apply(Create.of(records).withCoder(AvroCoder.of(schema)))
            .apply(MapElements.via(AvroToPubsubMessage(attributes)))
            .apply("Write Message to PubSub", PubsubIO.writeMessages().to("projects/my-project-id/topics/streaming-input"))

        pipeline.run(options)
    }

    @Test @Ignore
    fun generateTestDataFromFile() {
        val options = TestPipeline.testingPipelineOptions()
        options.`as`(PubsubOptions::class.java).pubsubRootUrl = "http://localhost:8085"

        val schema = Schema.Parser().parse(javaClass.getResourceAsStream("/data/person.avsc"))

        pipeline.apply(AvroIO.readGenericRecords(schema).from("./src/test/resources/data/person.avro"))
            .apply(MapElements.via(AvroToPubsubMessage()))
            .apply("Write Message to PubSub", PubsubIO.writeMessages().to("projects/my-project-id/topics/streaming-input"))

        pipeline.run(options)
    }
}
