# Using Google PubSub Emulator
 

## Install PubSub Emulator
Google PubSub [Emulator](https://cloud.google.com/pubsub/docs/emulator) 
```bash
gcloud components install pubsub-emulator
```
    
## Start PubSub Emulator

```
export PROJECT_ID=my-project-id
export PIPELINE_NAME=ingestion

gcloud beta emulators pubsub start --project=${PROJECT_ID} --host-port=localhost:8085
```

You can generate topics and subscription for Emulator via REST API

## Setup Env

```bash
export PROJECT_ID=my-project-id
export PIPELINE_NAME=ingestion
export ENVIRONMENT=dev
export PUBSUB_EMULATOR_HOST=http://localhost:8085
```

### Create 

```bash
# Create Topics and Subscriptions every time you restart pubsub emulator 
# if you run `generateTestData` test, it will also generate below topics.
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-in-${ENVIRONMENT}
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-outs-${ENVIRONMENT}
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-outf-${ENVIRONMENT}

# Create a subscription to input topic
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-in-${ENVIRONMENT} \
-H "Content-Type: application/json" \
-d '{
"topic": "'"projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-in-${ENVIRONMENT}"'"
}' 

# Create a subscription to output success topic
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-outs-${ENVIRONMENT} \
-H "Content-Type: application/json" \
-d '{
"topic": "'"projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-outs-${ENVIRONMENT}"'"
}' 

# Create a subscription to output failure topic
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-outf-${ENVIRONMENT} \
-H "Content-Type: application/json" \
-d '{
"topic": "'"projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-outf-${ENVIRONMENT}"'"
}' 
```

### Verify 

```bash
# List Topics (optional)
curl -X GET ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics
# List Subscriptions (optional)
curl -X GET ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions
 
# publishing a message to input topic
curl -d '{"messages": [{"data": "c3Vwc3VwCg=="}]}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${PIPELINE_NAME}-in-${ENVIRONMENT}:publish
 
# Read messages from success topic
curl -d '{"returnImmediately":true, "maxMessages":1}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-outs-${ENVIRONMENT}:pull

# Read messages from error topic
curl -d '{"returnImmediately":true, "maxMessages":1}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${PIPELINE_NAME}-outf-${ENVIRONMENT}:pull
``` 
