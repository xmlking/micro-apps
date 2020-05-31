package micro.apps.pipeline

/* ktlint-disable no-wildcard-imports */
import org.apache.beam.runners.direct.DirectOptions
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions
import org.apache.beam.sdk.io.gcp.pubsub.PubsubOptions
import org.apache.beam.sdk.options.*

/* ktlint-enable no-wildcard-imports */

interface BaseStreamingOptions : ApplicationNameOptions, PipelineOptions, DirectOptions, GcpOptions, StreamingOptions, PubsubOptions {

    /**
     * PubSub Options
     */

    @get:Description(
        """The Cloud Pub/Sub topic to read from.
        The name should be in the format of projects/<project-id>/topics/<input-topic-name>"""
    )
    @get:Default.InstanceFactory(PubsubInputTopicFactory::class)
    @get:Validation.Required
    var inputTopic: ValueProvider<String>

    @get:Description(
        """The Cloud Pub/Sub subscription to read from.
        The name should be in the format of projects/<project-id>/subscriptions/<input-subscriptions-name>"""
    )
    @get:Default.InstanceFactory(PubsubInputSubscriptionFactory::class)
    @get:Validation.Required
    var inputSubscription: ValueProvider<String>

    @get:Description(
        """The Cloud Pub/Sub topic to publish SUCCESS events.
        The name should be in the format of projects/<project-id>/topics/<output-success-topic-name>"""
    )
    @get:Default.InstanceFactory(PubsubOutputSuccessTopicFactory::class)
    @get:Validation.Required
    var outputSuccessTopic: ValueProvider<String>

    @get:Description(
        """The Cloud Pub/Sub topic to publish FAILURE events.
        The name should be in the format of projects/<project-id>/topics/<output-failure-topic-name>"""
    )
    @get:Default.InstanceFactory(PubsubOutputFailureTopicFactory::class)
    @get:Validation.Required
    var outputFailureTopic: ValueProvider<String>

    /**
     * Default Factories
     */

    /** Returns a default Pub/Sub topic based on the project and the job names.  */
    class PubsubInputTopicFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) =
            """projects/${(options as GcpOptions).project}/topics/${options.jobName}-input""".trimIndent()
    }

    /** Returns a default Pub/Sub subscription based on the project and the job names.  */
    class PubsubInputSubscriptionFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) =
            """projects/${(options as GcpOptions).project}/subscriptions/${options.jobName}-input""".trimIndent()
    }

    /** Returns a default Pub/Sub topic based on the project and the job names.  */
    class PubsubOutputSuccessTopicFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) =
            """projects/${(options as GcpOptions).project}/topics/${options.jobName}-output-success""".trimIndent()
    }

    /** Returns a default Pub/Sub topic based on the project and the job names.  */
    class PubsubOutputFailureTopicFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) =
            """projects/${(options as GcpOptions).project}/topics/${options.jobName}-output-failure""".trimIndent()
    }
}
