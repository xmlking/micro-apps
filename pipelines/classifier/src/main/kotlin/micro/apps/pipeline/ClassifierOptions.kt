package micro.apps.pipeline

import org.apache.beam.sdk.options.Default
import org.apache.beam.sdk.options.Description

interface ClassifierOptions : BaseStreamingOptions {
    @get:Description(
        """The window duration in which data will be written. Defaults to 5m.
                Allowed formats are:
                Ns (for seconds, example: 5s),
                Nm (for minutes, example: 12m),
                Nh (for hours, example: 2h).")"""
    )
    @get:Default.String("300s")
    var windowDuration: String
}
