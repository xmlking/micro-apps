package micro.apps.kbeam.gcp

import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PDone

/**
 * Write data to a PubSub topic.
 */
fun PCollection<PubsubMessage>.toPubSub(
    name: String = "Write to PubSub",
    topic: String,
    idAttribute: String?,
    maxBatchSize: Int? = 10
): PDone {
    return this.apply(
        name,
        PubsubIO.writeMessages().to(topic).apply {
            maxBatchSize?.let { withMaxBatchSize(maxBatchSize) }
            idAttribute?.let { withIdAttribute(idAttribute) }
        }
    )
}
