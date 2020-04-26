package micro.apps.pipeline

/* ktlint-disable no-wildcard-imports */
import org.apache.beam.runners.direct.DirectOptions
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions
import org.apache.beam.sdk.io.gcp.pubsub.PubsubOptions
import org.apache.beam.sdk.options.*
/* ktlint-enable no-wildcard-imports */

interface ClassifierOptions : ApplicationNameOptions, PipelineOptions, StreamingOptions, PubsubOptions, GcpOptions, DirectOptions {

    @get:Description("""The Cloud Pub/Sub subscription to read from.
        The name should be in the format of projects/<project-id>/subscriptions/<subscriptions-name>.""")
    @get:Default.InstanceFactory(PubsubSubscriptionFactory::class)
    @get:Validation.Required
    var inputSubscription: ValueProvider<String>

    @get:Description("""The Cloud Pub/Sub topic to read from.
        The name should be in the format of projects/<project-id>/topics/<topic-name>.""")
    @get:Default.String("projects/my-project-id/topics/classifier-input")
    @get:Validation.Required
    var inputTopic: ValueProvider<String>

    @get:Description("""The Cloud Pub/Sub topic to publish to.
        The name should be in the format of projects/<project-id>/topics/<topic-name>.""")
    @get:Default.InstanceFactory(PubsubTopicFactory::class)
    @get:Validation.Required
    var outputTopic: ValueProvider<String>

    @get:Description(
        """The window duration in which data will be written. Defaults to 5m.
                Allowed formats are:
                Ns (for seconds, example: 5s),
                Nm (for minutes, example: 12m),
                Nh (for hours, example: 2h).")""")
    @get:Default.String("300s")
    var windowDuration: String

    /** Returns a default Pub/Sub subscription based on the project and the job names.  */
    class PubsubSubscriptionFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """
                projects/
                ${(options as GcpOptions).project}
                /subscriptions/
                ${options.jobName}
            """.trimIndent()
    }

    /** Returns a default Pub/Sub topic based on the project and the job names.  */
    class PubsubTopicFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) = """
                projects/"
                    ${(options as GcpOptions).project}
                    /topics/
                    ${options.jobName}
            """.trimIndent()
    }
}
