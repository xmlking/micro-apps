# Dataflow

### Example data injection pipelines
1. Online transactions to data lake / journaling
2. IoT data aggregation
3. Viewership Data Lake
4. Suppliers data management ( you buy customer data from suppliers) - carfax collecting car service history
5. CDC to OLAP
6. Clickstream data analysis

## ELT Pipeline
   Sources - (normalization to CloudEvents  - Cleansing / filtering - generic anonymization / encryption)  - upsert - designations #BigQuery (staging/journaling, reporting/analytics)

Sourcing - online, streaming, batch

### Technology
Cloud Events , AsyncAPI , Schema Registry, wrapper encryption.

## Todo
- implement generic field-level encryption/description ParDo using proto annotations
- implement end-to-end crypto-shredding using AEAD/Tink

## Reference

- Schema evolution in streaming Dataflow jobs and BigQuery tables, [part 1](https://robertsahlin.com/schema-evolution-in-streaming-dataflow-jobs-and-bigquery-tables-part-1/)
- Schema evolution in streaming Dataflow jobs and BigQuery tables, [part 2](https://robertsahlin.com/schema-evolution-in-streaming-dataflow-jobs-and-bigquery-tables-part-2/)
- Schema evolution in streaming Dataflow jobs and BigQuery tables, [part 3](https://robertsahlin.com/schema-evolution-in-streaming-dataflow-jobs-and-bigquery-tables-part-3/)
- **datahem's** *GenericStreamPipeline* proto messages from [PubSub --> BigQuery](https://github.com/mhlabs/datahem.processor/tree/master/generic/src/main/java/org/datahem/processor/generic)
- Converting protobuf to bigquery in Java via **useBeamSchema** [stackoverflow](https://stackoverflow.com/questions/64903586/converting-protobuf-to-bigquery-in-java)
- BigQuery ProtoBuf, PubSub [examples](https://github.com/mhlabs/datahem.processor)
- Dataflow ProtoBuf, Time-Series ML [examples](https://github.com/kasna-cloud/dataflow-fsi-example/blob/main/docs/FLOWS.md)
  - [blog](https://github.com/kasna-cloud/dataflow-fsi-example/blob/main/docs/BLOG.md)
- GCP terraform setup [scripts](https://github.com/kasna-cloud/dataflow-fsi-example/tree/main/infra)
- beam [SchemaRegistry](https://beam.apache.org/releases/javadoc/2.7.0/org/apache/beam/sdk/schemas/SchemaRegistry.html)
- [Fast and flexible data pipelines with protobuf schema registry](https://robertsahlin.com/fast-and-flexible-data-pipelines-with-protobuf-schema-registry/)
- [protobeam](https://github.com/anemos-io/protobeam)
- Beam **AEAD** encryption [video](https://youtu.be/Oi946DJVE7g?t=1008) 
