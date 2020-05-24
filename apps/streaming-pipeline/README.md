# streaming-pipeline

Streaming pipeline demo. 

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
export PIPELINE_NAME=classifier

gcloud beta emulators pubsub start --project=${PROJECT_ID} --host-port=localhost:8085
```

#### Run Job

> publish sample data into `PubSub` Emulator for testing

```bash
# make sure @Ignore is uncommented in `PubSubProducerTest.kt`
gradle :apps:streaming-pipeline:test --tests "micro.apps.pipeline.PubSubProducerTest.generateTestData"
```

> run subscription job

```bash
export PROJECT_ID=my-project-id
export PIPELINE_NAME=classifier
GCS_BUCKET=classifier-bucket
export PUBSUB_EMULATOR_HOST=http://localhost:8085

gradle :apps:streaming-pipeline:run --args="--runner=DirectRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--pubsubRootUrl=${PUBSUB_EMULATOR_HOST}

# run non-stream mode 
gradle :apps:streaming-pipeline:run --args="--runner=DirectRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--pubsubRootUrl=${PUBSUB_EMULATOR_HOST} \
--stream=false \
--gcpTempLocation=./build/temp \
--inputPath=./src/main/resources/data/*.avro \
--outputSuccessPath=./build/output/success \
--outputFailurePath=./build/output/failure"

# or via jar [gradle :apps:streaming-pipeline:build -x test]
java -jar ./apps/streaming-pipeline/build/libs/streaming-pipeline-0.1.2-SNAPSHOT-all.jar --runner=DirectRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--pubsubRootUrl=${PUBSUB_EMULATOR_HOST}
```


### Cloud Run  
```bash
PROJECT_ID=<my-project-id>
PIPELINE_NAME=streaming
GCS_BUCKET=<my-gcs-bucket>
export GOOGLE_APPLICATION_CREDENTIALS=<full-path-to-your-json>

gradle :apps:streaming-pipeline:run --args="--runner=DataflowRunner \
--project=${PROJECT_ID} \
--jobName=${PIPELINE_NAME} \
--gcsBucket=${GCS_BUCKET} \
--inputSubscription=projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-input \
--outputSuccessTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-success" \
--outputFailureTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output-failure" \
--gcpTempLocation=gs://${PROJECT_ID}/dataflow/${PIPELINE_NAME}/temp/ \
--stagingLocation=gs://${PROJECT_ID}/dataflow/${PIPELINE_NAME}/staging"

# Or with fatJar
java -jar ./apps/streaming-pipeline/build/libs/streaming-pipeline-0.1.2-SNAPSHOT-all.jar --runner=DataflowRunner \
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
gradle :apps:streaming-pipeline:run --args="--runner=DataflowRunner --project=$PROJECT_ID --gcpTempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ --stagingLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/staging/ --templateLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/template/${PIPELINE_NAME}"
```

> Creating as template from VM
```bash
java -jar ./apps/streaming-pipeline/build/libs/streaming-pipeline-0.1.6-SNAPSHOT-all.jar --runner=DataFlowRunner \
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
    --parameters inputFile=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/input/shakespeare.txt,gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/output/output.txt
```

## Test
```bash
gradle :apps:streaming-pipeline:test
```

## Build
```bash
# clean
gradle :apps:streaming-pipeline:clean
# make fatJar
gradle :apps:streaming-pipeline:build -x test
```

### Using PubSub Emulator

> you can generate topics and subscription for Emulator via REST API

#### Setup Env

```bash
export PROJECT_ID=my-project-id
export PIPELINE_NAME=classifier
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
- E2E tests with PutSub <https://github.com/stevenextwave/beam/blob/master/src/test/java/org/apache/beam/examples/test/EndToEndTest.java>

- Async https://github.com/danielhultqvist/async-scio-future/blob/master/src/main/scala/example/FutureAsyncDoFn.scala
