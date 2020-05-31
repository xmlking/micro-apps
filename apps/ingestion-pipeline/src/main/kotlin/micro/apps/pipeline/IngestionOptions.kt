package micro.apps.pipeline

import org.apache.beam.sdk.options.Default
import org.apache.beam.sdk.options.Description
import org.apache.beam.sdk.options.Validation
import org.apache.beam.sdk.options.ValueProvider

interface IngestionOptions : BaseStreamingOptions, BaseBatchOptions {

    @get:Description(
        """The window duration in which data will be written. Defaults to 5m.
                Allowed formats are:
                Ns (for seconds, example: 5s),
                Nm (for minutes, example: 12m),
                Nh (for hours, example: 2h).")"""
    )
    @get:Default.String("300s")
    var windowDuration: String

    @get:Description("Set Streaming Mode. Defaults to TRUE")
    @get:Default.Boolean(true)
    @get:Validation.Required
    var stream: Boolean

    @get:Description("Inquiry Service gRPC End Point. Format host:port")
    @get:Default.String("localhost:443")
    @get:Validation.Required
    var accountEndpoint: String

    @get:Description("Inquiry gRPC Service TLS Authority. Format my.domain.com")
    @get:Default.String("my.domain.com")
    @get:Validation.Required
    var accountAuthority: ValueProvider<String>
}
