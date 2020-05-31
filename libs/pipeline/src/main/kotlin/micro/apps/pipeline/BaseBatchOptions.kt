package micro.apps.pipeline

/* ktlint-disable no-wildcard-imports */
import org.apache.beam.runners.direct.DirectOptions
import org.apache.beam.sdk.extensions.gcp.options.GcpOptions
import org.apache.beam.sdk.extensions.gcp.options.GcsOptions
import org.apache.beam.sdk.options.*

/* ktlint-enable no-wildcard-imports */

interface BaseBatchOptions : ApplicationNameOptions, PipelineOptions, DirectOptions, GcpOptions, GcsOptions {

    /**
     * GCS Options
     */

    @get:Description(
        """GCS input path to read from.
        |Format is: gs://<project-id>/dataflow/<jobName>/input/*.avro"""
    )
    @get:Default.InstanceFactory(GcsInputPathFactory::class)
    @get:Validation.Required
    var inputPath: ValueProvider<String>

    @get:Description(
        """GCS outputPrefix to write SUCCESS events.
        |Format is: gs://<project-id>/dataflow/<jobName>/output/success"""
    )
    @get:Default.InstanceFactory(GcsOutputSuccessPathFactory::class)
    @get:Validation.Required
    var outputSuccessPath: ValueProvider<String>

    @get:Description(
        """GCS outputPrefix to write FAILURE events.
        |Format is: gs://<project-id>/dataflow/<jobName>/output/failure"""
    )
    @get:Default.InstanceFactory(GcsOutputFailurePathFactory::class)
    @get:Validation.Required
    var outputFailurePath: ValueProvider<String>

    @get:Description("GCS Bucket for this job's inputs and outputs")
    @get:Default.String("classifier-bucket")
    @get:Validation.Required
    var gcsBucket: String

    @get:Description(
        """GCP Cloud KMS key for Dataflow pipelines.
        |Key format is: projects/<project>/locations/<location>/keyRings/<keyring>/cryptoKeys/<key>"""
    )
    @get:Default.InstanceFactory(GcsKeystorePathFactory::class)
    @get:Validation.Required
    var kmsKey: ValueProvider<String>

    /**
     * Default Factories
     */

    class GcsInputPathFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) =
            """gs://${(options as BaseBatchOptions).gcsBucket}/dataflow/${options.jobName}/input/*.avro""".trimIndent()
    }

    class GcsOutputSuccessPathFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) =
            """gs://${(options as BaseBatchOptions).gcsBucket}/dataflow/${options.jobName}/output/success""".trimIndent()
    }

    class GcsOutputFailurePathFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) =
            """gs://${(options as BaseBatchOptions).gcsBucket}/dataflow/${options.jobName}/output/failure""".trimIndent()
    }

    class GcsKeystorePathFactory : DefaultValueFactory<String> {
        override fun create(options: PipelineOptions) =
            """projects/${(options as GcpOptions).project}/locations/${options.jobName}/keyRings/${options.jobName}/cryptoKeys""".trimIndent()
    }
}
