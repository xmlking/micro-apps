# ingestion-pipeline

Streaming pipeline demo.

## Features
- [x] Deduplication

## Prerequisites

1. Google PubSub [Emulator](https://cloud.google.com/pubsub/docs/emulator)
    ```bash
    gcloud components install pubsub-emulator
    ```

## Run

### Local Run

#### Start Google PubSub Emulator

```bash
export PROJECT_ID=my-project-id
export PIPELINE_NAME=ingestion

gcloud beta emulators pubsub start --project=${PROJECT_ID} --host-port=localhost:8085
```

#### Run Job

> publish sample data into `PubSub` Emulator for testing

```bash
gradle :pipelines:ingestion:integrationTest --tests "micro.apps.pipeline.PubSubProducerTest.generateTestData"
```

> run subscription job

```bash
export PROJECT_ID=my-project-id
export PIPELINE_NAME=ingestion
GCS_BUCKET=ingestion-bucket
export PUBSUB_EMULATOR_HOST=http://localhost:8085

gradle :pipelines:ingestion:run --args="--runner=DirectRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--pubsubRootUrl=${PUBSUB_EMULATOR_HOST}"

# run non-stream mode 
gradle :pipelines:ingestion:run --args="--runner=DirectRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--pubsubRootUrl=${PUBSUB_EMULATOR_HOST} \
--stream=false \
--gcpTempLocation=./build/temp \
--inputPath=./src/main/resources/data/*.avro \
--outputSuccessPath=./build/output/success \
--outputFailurePath=./build/output/failure"

# or via jar [gradle :pipelines:ingestion:build -x test]
java -jar ./apps/ingestion/build/libs/ingestion-pipeline-0.1.2-SNAPSHOT-all.jar --runner=DirectRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--pubsubRootUrl=${PUBSUB_EMULATOR_HOST}
```

### Cloud Run

Set variables
```bash
# DataHem generic settings
PROJECT_ID='' # Required. Your google project id. Example: 'my-project'

# Dataflow settings
DF_REGION='europe-west1' # Optional. Default: us-central1
DF_ZONE='europe-west1-b' # Optional. Default:  an availability zone from the region set in DF_REGION.
DF_NUM_WORKERS=1 # Optional. Default: Dataflow service will determine an appropriate number of workers. Example: 2
DF_MAX_NUM_WORKERS=1 # Optional. Default: Dataflow service will determine an appropriate number of workers. Example: 5
DF_DISK_SIZE_GB=30 # Optional. Default: Size defined in your Cloud Platform project. Minimum is 30. Example: 50
DF_WORKER_MACHINE_TYPE='n1-standard-1' # Optional. Default: The Dataflow service will choose the machine type based on your job. Example: 'n1-standard-1'
JOB_NAME='' # Optional. Default: The Dataflow service will name the job if not provided

# GenericStream Pipeline settings
BUCKET_NAME=${PROJECT_ID}-schema-registry # Required. Example. ${PROJECT_ID}-schema-registry
FILE_DESCRIPTOR_NAME='' # Required. Example: schemas.desc
FILE_DESCRIPTOR_PROTO_NAME='' # Required. Example: datahem/protobuf/order/v1/order.proto'
MESSAGE_TYPE='' # Required. Example: order
PUBSUB_SUBSCRIPTION='' # Required. The name of the stream. Example: 'order-stream'
BIGQUERY_TABLE_SPEC='' # Required. Example: streams.orders
TAXONOMY_RESOURCE_PATTERN='' # Optional. Default: '.*'. Example: '7564324984364324'  (taxonomy id)
```

compile and run

```bash
PROJECT_ID=<my-project-id>
PIPELINE_NAME=streaming
GCS_BUCKET=<my-gcs-bucket>
export GOOGLE_APPLICATION_CREDENTIALS=<full-path-to-your-json>

gradle :pipelines:ingestion:run --args="--runner=DataflowRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--inputSubscription=projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-input \
--outputSuccessTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-success" \
--outputFailureTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-failure" \
--gcpTempLocation=gs://${PROJECT_ID}/dataflow/${PIPELINE_NAME}/temp/ \
--stagingLocation=gs://${PROJECT_ID}/dataflow/${PIPELINE_NAME}/staging"

# Or with fatJar
java -jar ./pipelines/ingestion/build/libs/ingestion-pipeline-0.1.2-SNAPSHOT-all.jar --runner=DataflowRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--inputSubscription=projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-input \
--outputSuccessTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-success" \
--outputFailureTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-failure" \
--gcpTempLocation=gs://${PROJECT_ID}/dataflow/${PIPELINE_NAME}/temp/ \
--stagingLocation=gs://${PROJECT_ID}/dataflow/${PIPELINE_NAME}/staging/ \
```

### Creating Template

```bash
gradle :pipelines:ingestion:run --args="--runner=DataflowRunner --project=$PROJECT_ID --gcpTempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ --stagingLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/staging/ --templateLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/template/${PIPELINE_NAME}"
```

> Creating as template from VM

```bash
java -jar ./apps/ingestion-pipeline/build/libs/ingestion-pipeline-0.1.6-SNAPSHOT-all.jar --runner=DataFlowRunner \
    --project=$PROJECT_ID \
    --gcpTempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ \
    --stagingLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/staging/ \
    --templateLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/template/${PIPELINE_NAME}
```

### Running template

> Create Job

```bash
gcloud dataflow jobs run wordcount \
    --gcs-location gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/template/${PIPELINE_NAME} \
    --zone=$DF_ZONE \
    --region=$DF_REGION \
    --max-workers=$DF_MAX_NUM_WORKERS \
    --parameters inputFile=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/input/shakespeare.txt,gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/output/output.txt
```

## Test

```bash
gradle :pipelines:ingestion:test
```

## Build

```bash
# clean
gradle :pipelines:ingestion:clean
# make fatJar
gradle :pipelines:ingestion:build -x test
```

### Using PubSub Emulator

> you can generate topics and subscription for Emulator via REST API

#### Setup Env

```bash
export PROJECT_ID=my-project-id
export PIPELINE_NAME=ingestion
export PUBSUB_EMULATOR_HOST=http://localhost:8085
```

#### Create

```bash
# Create Topics and Subscriptions every time you restart pubsub emulator 
# if you run `generateTestData` test, it will also generate below topics.
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-input
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-success
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-failure

# Create a subscription to input topic
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-input \
-H "Content-Type: application/json" \
-d '{
"topic": "'"projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-input"'"
}' 

# Create a subscription to output success topic
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-output-success \
-H "Content-Type: application/json" \
-d '{
"topic": "'"projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-success"'"
}' 

# Create a subscription to output failure topic
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-output-failure \
-H "Content-Type: application/json" \
-d '{
"topic": "'"projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-failure"'"
}' 
```

#### Verify

```bash
# List Topics (optional)
curl -X GET ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics
# List Subscriptions (optional)
curl -X GET ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions
 
# publishing a message to input topic
curl -d '{"messages": [{"data": "c3Vwc3VwCg=="}]}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-input:publish
 
# Read messages from success topic
curl -d '{"returnImmediately":true, "maxMessages":1}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-output-success:pull

# Read messages from error topic
curl -d '{"returnImmediately":true, "maxMessages":1}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-output-failure:pull
``` 

## TODO

- https://beam.apache.org/documentation/sdks/java/euphoria/
- [Batched RPC](https://beam.apache.org/blog/2017/08/28/timely-processing.html)
- E2E tests with
  PutSub <https://github.com/stevenextwave/beam/blob/master/src/test/java/org/apache/beam/examples/test/EndToEndTest.java>
- [A Distributed Tracing Adventure in Apache Beam](http://rion.io/2020/07/04/a-distributed-tracing-adventure-in-apache-beam/)
- [Avoiding Kotlin Minefields in Apache Beam](http://rion.io/2020/06/17/avoiding-kotlin-minefields-in-apache-beam/)
- Async https://github.com/danielhultqvist/async-scio-future/blob/master/src/main/scala/example/FutureAsyncDoFn.scala

- [Deduplication: Where Beam Fits In](https://www.youtube.com/watch?v=9OfJKDs3h40)
  - Talk's [Source Code](https://github.com/mozilla/gcp-ingestion)

- PubSub --> BigQuery Streaming
  - Protobuf [GenericStreamPipeline](https://github.com/mhlabs/datahem.processor/tree/master/generic/src/main/java/org/datahem/processor/generic)

- Data Confidentiality
  - Reduce data risk with obfuscation and de-identification methods like masking and tokenization
  - Seamlessly inspect and transform structured data via schema annotations. 
