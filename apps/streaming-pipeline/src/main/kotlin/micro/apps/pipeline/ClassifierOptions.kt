package micro.apps.pipeline

/* ktlint-disable no-wildcard-imports */
import org.apache.beam.runners.direct.DirectOptions
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions
import org.apache.beam.sdk.extensions.gcp.options.GcsOptions
import org.apache.beam.sdk.io.gcp.pubsub.PubsubOptions
import org.apache.beam.sdk.options.*

/* ktlint-enable no-wildcard-imports */

interface ClassifierOptions : ApplicationNameOptions, PipelineOptions, StreamingOptions, PubsubOptions, GcpOptions, GcsOptions, DirectOptions {

    /**
     * PubSub Options
     */

    @get:Description("""The Cloud Pub/Sub topic to read from.
        The name should be in the format of projects/<project-id>/topics/<input-topic-name>""")
    @get:Default.InstanceFactory(PubsubInputTopicFactory::class)
    @get:Validation.Required
    var inputTopic: ValueProvider<String>

    @get:Description("""The Cloud Pub/Sub subscription to read from.
        The name should be in the format of projects/<project-id>/subscriptions/<input-subscriptions-name>""")
    @get:Default.InstanceFactory(PubsubInputSubscriptionFactory::class)
    @get:Validation.Required
    var inputSubscription: ValueProvider<String>

    @get:Description("""The Cloud Pub/Sub topic to publish SUCCESS events.
        The name should be in the format of projects/<project-id>/topics/<output-success-topic-name>""")
    @get:Default.InstanceFactory(PubsubOutputSuccessTopicFactory::class)
    @get:Validation.Required
    var outputSuccessTopic: ValueProvider<String>

    @get:Description("""The Cloud Pub/Sub topic to publish FAILURE events.
        The name should be in the format of projects/<project-id>/topics/<output-failure-topic-name>""")
    @get:Default.InstanceFactory(PubsubOutputFailureTopicFactory::class)
    @get:Validation.Required
    var outputFailureTopic: ValueProvider<String>

    /**
     * GCS Options
     */

    @get:Description("""GCS input path to read from.
        |Format is: gs://<project-id>/dataflow/pipelines/<jobName>/input/*.avro""")
    @get:Default.InstanceFactory(GcsInputPathFactory::class)
    @get:Validation.Required
    var inputPath: ValueProvider<String>

    @get:Description("""GCS output path to write SUCCESS events.
        |Format is: gs://<project-id>/dataflow/pipelines/<jobName>/output_success""")
    @get:Default.InstanceFactory(GcsOutputSuccessPathFactory::class)
    @get:Validation.Required
    var outputSuccessPath: ValueProvider<String>

    @get:Description("""GCS output path to write FAILURE events.
        |Format is: gs://<project-id>/dataflow/pipelines/<jobName>/output_failure""")
    @get:Default.InstanceFactory(GcsOutputFailurePathFactory::class)
    @get:Validation.Required
    var outputFailurePath: ValueProvider<String>

    @get:Description("""GCP Cloud KMS key for Dataflow pipelines.
        |Key format is: projects/<project>/locations/<location>/keyRings/<keyring>/cryptoKeys/<key>""")
    @get:Default.InstanceFactory(GcsKeystorePathFactory::class)
    @get:Validation.Required
    var kmsKey: ValueProvider<String>

    /**
     * Custom Options
     */

    @get:Description(
        """The window duration in which data will be written. Defaults to 5m.
                Allowed formats are:
                Ns (for seconds, example: 5s),
                Nm (for minutes, example: 12m),
                Nh (for hours, example: 2h).")""")
    @get:Default.String("300s")
    var windowDuration: String

    @get:Description("Inquiry Service gRPC End Point. Format host:port")
    @get:Default.String("localhost:443")
    @get:Validation.Required
    var accountEndpoint: String

    @get:Description("Inquiry gRPC Service TLS Authority. Format my.domain.com")
    @get:Default.String("my.domain.com")
    @get:Validation.Required
    var accountAuthority: ValueProvider<String>

    /**
     * Default Factories
     */

    /** Returns a default Pub/Sub topic based on the project and the job names.  */
    class PubsubInputTopicFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """projects/${(options as GcpOptions).project}/topics/${options.jobName}-input""".trimIndent()
    }

    /** Returns a default Pub/Sub subscription based on the project and the job names.  */
    class PubsubInputSubscriptionFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """projects/${(options as GcpOptions).project}/subscriptions/${options.jobName}-input""".trimIndent()
    }

    /** Returns a default Pub/Sub topic based on the project and the job names.  */
    class PubsubOutputSuccessTopicFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """projects/${(options as GcpOptions).project}/topics/${options.jobName}-output-success""".trimIndent()
    }

    /** Returns a default Pub/Sub topic based on the project and the job names.  */
    class PubsubOutputFailureTopicFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """projects/${(options as GcpOptions).project}/topics/${options.jobName}-output-failure""".trimIndent()
    }

    class GcsInputPathFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """gs://${(options as GcpOptions).project}/dataflow/pipelines/${options.jobName}/input/*.avro""".trimIndent()
    }
    class GcsOutputSuccessPathFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """gs://${(options as GcpOptions).project}/dataflow/pipelines/${options.jobName}/output_success""".trimIndent()
    }
    class GcsOutputFailurePathFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """gs://${(options as GcpOptions).project}/dataflow/pipelines/${options.jobName}/output_failure""".trimIndent()
    }
    class GcsKeystorePathFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """projects/${(options as GcpOptions).project}/locations/${options.jobName}/keyRings/${options.jobName}/cryptoKeys""".trimIndent()
    }
}
