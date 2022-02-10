package micro.apps.pipeline

import com.github.avrokotlin.avro4k.Avro
import com.google.api.gax.rpc.ApiException
import com.google.api.gax.rpc.StatusCode.Code.ALREADY_EXISTS
import com.google.protobuf.ByteString
import com.google.pubsub.v1.PubsubMessage
import kotlinx.serialization.ExperimentalSerializationApi
import micro.apps.kbeam.map
import micro.apps.model.Person
import micro.apps.model.fixtures.mockPersonList
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.beam.sdk.extensions.gcp.auth.NoopCredentialFactory
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.options.PipelineOptionsFactory
import org.apache.beam.sdk.testing.PAssert
import org.apache.beam.sdk.testing.TestPipeline
import org.joda.time.Instant
import org.junit.Rule
import org.junit.jupiter.api.Tag
import java.io.Serializable
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalSerializationApi::class)
class ClassifierPipelineTest : Serializable {

    private val host = "localhost:8085"
    private val projectId = "my-project-id"
    private val jobName = "classifier"
    private val inputTopicName = "$jobName-input"
    private val outputSuccessTopicName = "$jobName-output-success"
    private val outputFailureTopicName = "$jobName-output-failure"
    private val pubsubHelper = PubsubHelper(host, projectId)
    private lateinit var testOptions: ClassifierOptions

    private val persons = mockPersonList()

    @Rule
    @Transient
    @JvmField
    val pipeline = TestPipeline.create().enableAbandonedNodeEnforcement(false)

    @BeforeTest
    @Throws(Exception::class)
    fun setup() {
        PipelineOptionsFactory.register(ClassifierOptions::class.java)
        val args = arrayOf("--project=$projectId", "--jobName=$jobName", "--windowDuration=310s")
        testOptions = PipelineOptionsFactory.fromArgs(*args).withValidation().`as`(ClassifierOptions::class.java)
        testOptions.pubsubRootUrl = "http://localhost:8085"
        testOptions.credentialFactoryClass = NoopCredentialFactory::class.java
        testOptions.isBlockOnRun = false
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
        // seed data
        persons.forEach {
            val data = Avro.default.encodeToByteArray(Person.serializer(), it)
            val pubsubMessage = PubsubMessage.newBuilder()
                .setData(ByteString.copyFrom(data))
                .putAttributes("timestamp", Instant.now().millis.toString()).build()
            pubsubHelper.sendMessage(inputTopicName, pubsubMessage)
        }
    }

    @AfterTest
    @Throws(ApiException::class)
    fun after() {
        pubsubHelper.deleteSubscription(inputTopicName)
        pubsubHelper.deleteSubscription(outputSuccessTopicName)
        pubsubHelper.deleteSubscription(outputFailureTopicName)

        pubsubHelper.deleteTopic(inputTopicName)
        pubsubHelper.deleteTopic(outputSuccessTopicName)
        pubsubHelper.deleteTopic(outputFailureTopicName)
    }

    // NOTE:Google PubSub Emulator must be running for this test to be passed.
    @Test
    @Ignore
    @Tag("integration")
    fun runE2ETest() {
        assertEquals("310s", testOptions.windowDuration)

        val schema = Schema.Parser().parse(javaClass.getResourceAsStream("/data/person.avsc"))
        val serializer = Person.serializer()
        val inputs: List<GenericRecord> = persons.map {
            Avro.default.toRecord(serializer, it)
        }

        val read = pipeline.apply(
            "Read data from PubSub",
            PubsubIO.readAvroGenericRecords(schema).fromSubscription(testOptions.inputSubscription)
        ).map {
            println(it)
            it
        }

        PAssert.that(read).containsInAnyOrder(inputs)

        pipeline.run(testOptions)
    }
}
