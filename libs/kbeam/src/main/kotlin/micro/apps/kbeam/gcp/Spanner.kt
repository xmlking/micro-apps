package micro.apps.kbeam.gcp

import com.google.cloud.spanner.Mutation
import org.apache.beam.sdk.io.gcp.spanner.SpannerIO
import org.apache.beam.sdk.values.PCollection

/**
 * Write data to a Spanner table.
 */
fun PCollection<Mutation>.toSpanner(
    name: String = "Write to Spanner",
    instanceId: String,
    databaseId: String,
    batchSizeBytes: Long = (1024 * 10)
) {
    this.apply(
        name, SpannerIO.write()
        .withInstanceId(instanceId)
        .withDatabaseId(databaseId)
        .withBatchSizeBytes(batchSizeBytes)
    )
}
