package micro.apps.shared.dsl

import com.google.api.services.bigquery.model.TableReference
import com.google.api.services.bigquery.model.TableRow
import com.google.api.services.bigquery.model.TableSchema
import com.google.api.services.bigquery.model.TimePartitioning
import com.google.cloud.spanner.Mutation
import org.apache.beam.sdk.coders.Coder
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO
import org.apache.beam.sdk.io.gcp.bigquery.TableRowJsonCoder
import org.apache.beam.sdk.io.gcp.bigquery.WriteResult
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage
import org.apache.beam.sdk.io.gcp.spanner.SpannerIO
import org.apache.beam.sdk.values.PCollection
import org.apache.beam.sdk.values.PDone
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets

/**
 * Write data to a PubSub topic.
 */
fun PCollection<PubsubMessage>.toPubsub(
        name: String = "Write to PubSub",
        topic: String,
        idAttribute: String?,
        maxBatchSize: Int? = 10
): PDone {
    return this.apply(name, PubsubIO.writeMessages().to(topic).apply {
        maxBatchSize?.let { withMaxBatchSize(maxBatchSize) }
        idAttribute?.let { withIdAttribute(idAttribute) }
    })
}

/**
 * Write data to a BigQuery table.
 */
fun PCollection<TableRow>.toBigquery(
        name: String = "Write to BigQuery",
        table: String,
        dataset: String,
        project: String
): WriteResult {
    val tableRef = TableReference().setTableId(table).setDatasetId(dataset).setProjectId(project)

    return this.apply(name,
            BigQueryIO.writeTableRows()
                    .to(tableRef)
                    .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_NEVER)
                    .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
    )
}

/**
 * Write data to a BigQuery table with Partitioning.
 */
fun PCollection<TableRow>.toBigqueryTable(
        name: String = "Write to BigQuery table",
        table: String,
        dataset: String,
        project: String,
        tableSchema: TableSchema,
        timePartitioning: TimePartitioning = TimePartitioning()
): WriteResult {
    val tableRef = TableReference().setTableId(table).setDatasetId(dataset).setProjectId(project)

    return this.apply(name,
            BigQueryIO.writeTableRows()
                    .withExtendedErrorInfo()
                    .withSchema(tableSchema)
                    .to(tableRef)
                    .withTimePartitioning(timePartitioning)
                    .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
                    .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND))
}

/**
 * Write data to a Spanner table.
 */
fun PCollection<Mutation>.toSpanner(
        name: String = "Write to Spanner",
        instanceId: String,
        databaseId: String,
        batchSizeBytes: Long = (1024 * 10)
) {
    this.apply(name, SpannerIO.write()
            .withInstanceId(instanceId)
            .withDatabaseId(databaseId)
            .withBatchSizeBytes(batchSizeBytes)
    )
}


/**
 * Converts a JSON string to a [TableRow] object. If the data fails to convert, a RuntimeException will be thrown.
 *
 * @param json The JSON string to parse.
 * @return The parsed [TableRow] object.
 */
fun convertJsonToTableRow(json: String): TableRow? {
    // TODO: avoid converting to JSON => String => JSON again
    var row: TableRow? = null
    try {
        ByteArrayInputStream(json.toByteArray(StandardCharsets.UTF_8)).use { inputStream ->
            row = TableRowJsonCoder.of().decode(inputStream, Coder.Context.OUTER)
        }
    } catch (e: IOException) {
        throw RuntimeException("Failed to serialize json to table row: $json", e)
    }

    return row
}
