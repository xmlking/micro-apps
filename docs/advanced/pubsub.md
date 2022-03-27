# Using Google PubSub Emulator

> emulator currently does not support dead letter topics

## Install PubSub Emulator

Google PubSub [Emulator](https://cloud.google.com/pubsub/docs/emulator)

```bash
gcloud components install pubsub-emulator
```

## Start PubSub Emulator

```
export PROJECT_ID=my-project-id

gcloud beta emulators pubsub start --project=${PROJECT_ID} --host-port=localhost:8085
```

You can generate topics and subscription for Emulator via REST API

## Setup Env

```bash
export PROJECT_ID=my-project-id
export DOMAIN=ingestion
export ENVIRONMENT=dev
export PUBSUB_EMULATOR_HOST=http://localhost:8085
```

### Create

```bash
# Create Topics and Subscriptions every time you restart pubsub emulator 
# if you run `generateTestData` test, it will also generate below topics.
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${DOMAIN}-in-${ENVIRONMENT}
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${DOMAIN}-in-dead-${ENVIRONMENT}
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${DOMAIN}-out-${ENVIRONMENT}
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${DOMAIN}-out-dead-${ENVIRONMENT}

# Create a subscription to input topic
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${DOMAIN}-in-${ENVIRONMENT} \
-H "Content-Type: application/json" \
-d '{
    "topic": "'"projects/${PROJECT_ID}/topics/${DOMAIN}-in-${ENVIRONMENT}"'"
}' 

# Create a subscription to output success topic
curl -X PUT ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${DOMAIN}-out-${ENVIRONMENT} \
-H "Content-Type: application/json" \
-d '{
"topic": "'"projects/${PROJECT_ID}/topics/${DOMAIN}-out-${ENVIRONMENT}"'"
}' 
```

### Verify

```bash
# List Topics (optional)
curl -X GET ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics
# List Subscriptions (optional)
curl -X GET ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions
 
# publishing a message to input topic
curl -d '{"messages": [{"data": "c3Vwc3VwCg=="}]}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${DOMAIN}-in-${ENVIRONMENT}:publish

# Read messages from input topic
curl -d '{"returnImmediately":true, "maxMessages":1}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${DOMAIN}-in-${ENVIRONMENT}:pull

# Read messages from success topic
curl -d '{"returnImmediately":true, "maxMessages":1}' -H "Content-Type: application/json" -X POST ${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${DOMAIN}-out-${ENVIRONMENT}:pull
``` 

## Automation

### PubSub

Source the script needed for next steps

```bash
. ./scripts/pubsub_functions.sh
```

#### Start PubSub

Start emulator via gcloud cli

```bash
gcps
```

As alternative, you can also start emulator via docker

```bash
nerdctl compose up pub-sub-emulator
```

#### Setup PubSub

```bash
gcpg
# or
gcpg tooklit
# or
gcpg tooklit dev
```

#### Tail logs

```bash
# when using gcloud cli to start emulator
gcpl
```

#### Stop PubSub

```bash
# when using gcloud cli to start emulator
gcpk
# or if you are using nerdctl compose
nerdctl compose up down
```
