package micro.apps.pipeline

import com.google.api.gax.rpc.ApiException
import com.google.api.gax.rpc.StatusCode.Code.ALREADY_EXISTS
import com.google.common.collect.ImmutableMap
import com.sksamuel.avro4k.Avro
import java.io.Serializable
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.kbeam.transforms.AvroToPubsub
import micro.apps.model.Person
import micro.apps.model.fixtures.mockPersonList
import org.apache.avro.generic.GenericRecord
import org.apache.beam.sdk.coders.AvroCoder
import org.apache.beam.sdk.extensions.gcp.auth.NoopCredentialFactory
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.options.PipelineOptionsFactory
import org.apache.beam.sdk.testing.TestPipeline
import org.apache.beam.sdk.transforms.Create
import org.apache.beam.sdk.transforms.MapElements
import org.joda.time.Instant
import org.junit.Rule

// use Avro.default.fromRecord(serializer, rec) , Avro.default.toRecord Avro.default.toRecord(serializer, obj) to convert GenericRecord <==> Data Class
@ExperimentalSerializationApi
class PubSubProducerTest : Serializable {

    private val host = "localhost:8085"
    private val projectId = "my-project-id"
    private val jobName = "ingestion"
    private val inputTopicName = "$jobName-input"
    private val outputSuccessTopicName = "$jobName-output-success"
    private val outputFailureTopicName = "$jobName-output-failure"
    private val pubsubHelper = PubsubHelper(host, projectId)
    private lateinit var testOptions: IngestionOptions

    private val persons = mockPersonList()

    @Rule
    @Transient
    @JvmField
    val pipeline = TestPipeline.create()

    @BeforeTest
    @Throws(Exception::class)
    fun setup() {
        PipelineOptionsFactory.register(IngestionOptions::class.java)
        val args = arrayOf("--project=$projectId", "--jobName=$jobName")
        testOptions = PipelineOptionsFactory.fromArgs(*args).withValidation().`as`(IngestionOptions::class.java)
        testOptions.pubsubRootUrl = "http://localhost:8085"
        testOptions.credentialFactoryClass = NoopCredentialFactory::class.java
        println(testOptions)
        try {
            pubsubHelper.createTopic(inputTopicName)
            // using subscription name same as topic name
            pubsubHelper.createSubscription(inputTopicName, inputTopicName)
            pubsubHelper.createTopic(outputSuccessTopicName)
            pubsubHelper.createSubscription(outputSuccessTopicName, outputSuccessTopicName)
            pubsubHelper.createTopic(outputFailureTopicName)
            pubsubHelper.createSubscription(outputFailureTopicName, outputFailureTopicName)
        } catch (e: ApiException) {
            if (e.statusCode.code == ALREADY_EXISTS) {
                println("topic already exists")
            }
        }
    }

    @AfterTest
    @Throws(Exception::class)
    fun after() {
        println("cleaning...")
    }

    @Test @Ignore
    fun tummyTest() {
        println("tummyTest...")
    }

    // NOTE:Google PubSub Emulator must be running for this test to be passed.
    @Test @Ignore // TODO: remove @Ignore to use it
    fun generateTestData() {
        val serializer = Person.serializer()
        val schema = Avro.default.schema(serializer)

        // sample data
        val records: List<GenericRecord> = persons.map {
            Avro.default.toRecord(serializer, it)
        }

        val attributes = ImmutableMap.builder<String, String>()
            .put("timestamp", Instant.now().millis.toString())
            .put("fingerprint", "fingerprint")
            .put("uuid", "uuid")
            .build() // Collections.emptyMap()

        pipeline.apply(Create.of(records).withCoder(AvroCoder.of(schema)))
            .apply(MapElements.via(AvroToPubsub(attributes)))
            .apply("Write Message to PubSub", PubsubIO.writeMessages().to(testOptions.inputTopic))

        pipeline.run(testOptions)
    }
}
