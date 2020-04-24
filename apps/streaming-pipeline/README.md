# streaming-pipeline

Streaming pipeline demo. 

## Prerequisites

1. Google PubSub
    ```bash
    gcloud components install pubsub-emulator
    ```
   
## Run

### Local Run  

#### Start Google PubSub Emulator 

```bash
export PROJECT_ID=my-project-id
export PIPELINE_NAME=streaming

gcloud beta emulators pubsub start --project=${PROJECT_ID} --host-port=localhost:8085

# export PUBSUB_EMULATOR_HOST before using emulator pubsub
export PUBSUB_EMULATOR_HOST=http://localhost:8085

# Create Topic every time you restart pubsub emulator 
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-input
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output

# Create subscription
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/{sub}
{
  "topic": "projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-input"
}

# List Topics
curl -X GET ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics
# List subscriptions
curl -X GET ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions
```

#### Run Job

```bash
gradle :apps:streaming-pipeline:run --args="--runner=DirectRunner --windowDuration=100s  --pubsubRootUrl=${PUBSUB_EMULATOR_HOST} --inputTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-input --outputTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output"

java -jar -Dflogger.level=INFO \
./apps/wordcount/build/libs/streaming-0.1.6-SNAPSHOT-all.jar  \
--runner=DirectRunner \
--windowDuration=300s \
--pubsubRootUrl=${PUBSUB_EMULATOR_HOST} \
--inputTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-input \
--outputTopic=projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-output
```

> publish sample data into `PubSub` Emulator for testing
```bash
gradle :apps:streaming-pipeline:test --tests "micro.apps.pipeline.PubSubProducerTest.generateTestData"
```

### Cloud Run  
```bash
PROJECT_ID=<my-project-id>
PIPELINE_NAME=streaming
export GOOGLE_APPLICATION_CREDENTIALS=<full-path-to-your-json>

gradle -Dflogger.level=ALL  :apps:streaming-pipeline:run --args="--runner=DataflowRunner --project=$PROJECT_ID --gcpTempLocation==gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ --stagingLocation=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/staging/ --inputFile=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/input/shakespeare.txt --output=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/output/output.txt"

# Or with fatJar
java -jar ./apps/wordcount/build/libs/wordcount-0.1.6-SNAPSHOT-all.jar  \
--runner=DataflowRunner \
--windowDuration=2m \
--numShards=1 \
--project=${PROJECT_ID} \
--inputTopic=projects/${PROJECT_ID}/topics/windowed-files \
--gcpTempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ \
--stagingLocation=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/staging/ \
--inputFile=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/input/shakespeare.txt \
--output=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/output/output.txt
```

### Creating Template
```bash
gradle :apps:streaming-pipeline:run --args="--runner=DataflowRunner --project=$PROJECT_ID --gcpTempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ --stagingLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/staging/ --templateLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/template/${PIPELINE_NAME}"
```

> Creating as template from VM
```bash
java -jar /mnt/data/pipelines/templates/wordcount-0.1.6-SNAPSHOT-all.jar --runner=DataFlowRunner \
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
    --parameters inputFile=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/input/shakespeare.txt,gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/output/output.txt
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
gradle :apps:streaming-pipeline:build
```

## TODO

- https://beam.apache.org/documentation/sdks/java/euphoria/
