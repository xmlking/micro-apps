package micro.apps.pipeline.functions

import com.google.common.flogger.FluentLogger
import java.util.regex.Pattern
import micro.apps.core.LogDefinition.Companion.config
import micro.apps.model.Person
import org.apache.beam.sdk.metrics.Metrics
import org.apache.beam.sdk.transforms.DoFn
// import org.apache.beam.vendor.grpc.v1p26p0.io.grpc.ManagedChannel;
// import org.apache.beam.vendor.grpc.v1p26p0.io.grpc.ManagedChannelBuilder;
// java.util.concurrent.ExecutorService;

// https://github.com/bradkyle/AxiomJava/blob/master/src/main/java/com/axiom/pipeline/core/EnrichmentDoFn.java
// https://github.com/NorwinYu/UoN-Final-Year-Project-Public-Database/blob/master/Download-Java-Files/Normal/RemoteExecutionTest.java
// https://github.com/alexander-dev-hub/apache-beam/blob/master/runners/google-cloud-dataflow-java/worker/src/test/java/org/apache/beam/runners/dataflow/worker/StreamingDataflowWorkerTest.java
// https://github.com/xsm110/Beam15.0/blob/master/sdks/java/harness/src/test/java/org/apache/beam/fn/harness/FnApiDoFnRunnerTest.java
// https://github.com/alexander-dev-hub/apache-beam/blob/master/runners/google-cloud-dataflow-java/worker/src/test/java/org/apache/beam/runners/dataflow/worker/fn/data/BeamFnDataGrpcServiceTest.java
public class EnrichFn(pattern: String) : DoFn<Person, Person>() {
    companion object {
        @JvmStatic
        private val logger: FluentLogger = FluentLogger.forEnclosingClass().config()
    }

    private val filter: Pattern = Pattern.compile(pattern)

    private val enrichedPersons = Metrics.counter(EnrichFn::class.java, "enrichedPersons")
    private val failedPersons = Metrics.counter(EnrichFn::class.java, "failedPersons")

    @StartBundle
    fun setup() {
        // gson = Gson()
    }
    @ProcessElement
    fun processElement(c: ProcessContext) {
        try {
            val inPerson = c.element()
            c.output(inPerson.copy(email = "decrypted email"))
            enrichedPersons.inc()
        } catch (e: Exception) {
            logger.atInfo().withCause(e).log("error  message...")
            failedPersons.inc()
        }
    }
}
