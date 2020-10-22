#! /bin/zsh

# usage ----------------------------------------------------------------
# gcpg
# gcpg ingestion
# gcpg ingestion dev
# ----------------------------------------------------------------------

PROJECT_ID=my-project-id
DOMAIN=ingestion
ENVIRONMENT=dev
PUBSUB_EMULATOR_HOST=http://localhost:8085
PUBSUB_LOG=/tmp/gcloud-pubsub.log

# ----------------------------------------------------------------------
gcloud-pubsub-init() {
    eval $(gcloud beta emulators pubsub env-init)
}

gcloud-pubsub-status() {
    RESULT=$(curl -s ${PUBSUB_EMULATOR_HOST})
    if [ "$RESULT" = "Ok" ]; then # if ps aux | grep -v grep | grep -q pubsub
        echo "pubsub running at $PUBSUB_EMULATOR_HOST"
    else
        echo "pubsub not running"
    fi
}

gcloud-pubsub-start() {
    # nohup gcloud beta emulators pubsub start --quiet --project=${PROJECT_ID} --host-port=${PUBSUB_EMULATOR_HOST} /dev/null 2>&1 &
    nohup gcloud beta emulators pubsub start --quiet --project=${PROJECT_ID} --host-port=${PUBSUB_EMULATOR_HOST} >${PUBSUB_LOG} 2>&1 &
    gcloud-pubsub-init
    gcloud-pubsub-status
}

gcloud-pubsub-stop() {
    for i in $(ps aux | grep pubsub | grep -v grep | awk '{print $2}'); do
        if [ $i -gt 0 ]; then
            kill -9 $i
        fi
    done
    [ -e ${PUBSUB_LOG}  ] && rm ${PUBSUB_LOG}
}

gcloud-pubsub-restart() {
    gcloud-pubsub-stop
    gcloud-pubsub-start
}

gcloud-pubsub-log() {
    tail -f ${PUBSUB_LOG}
}

gcloud-pubsub-setup() {
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-in-${2:-$ENVIRONMENT}"
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-in-dead-${2:-$ENVIRONMENT}"
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-out-${2:-$ENVIRONMENT}"
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-out-dead-${2:-$ENVIRONMENT}"
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-error-${2:-$ENVIRONMENT}"
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-error-dead-${2:-$ENVIRONMENT}"

    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${1:-$DOMAIN}-in-${2:-$ENVIRONMENT}" \
    -H "Content-Type: application/json" \
    -d '{
    "topic": "'"projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-in-${2:-$ENVIRONMENT}"'"
    }'
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${1:-$DOMAIN}-in-dead-${2:-$ENVIRONMENT}" \
    -H "Content-Type: application/json" \
    -d '{
    "topic": "'"projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-in-dead-${2:-$ENVIRONMENT}"'"
    }'
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${1:-$DOMAIN}-out-${2:-$ENVIRONMENT}" \
    -H "Content-Type: application/json" \
    -d '{
    "topic": "'"projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-out-${2:-$ENVIRONMENT}"'"
    }'
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${1:-$DOMAIN}-out-dead-${2:-$ENVIRONMENT}" \
    -H "Content-Type: application/json" \
    -d '{
    "topic": "'"projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-out-dead-${2:-$ENVIRONMENT}"'"
    }'
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${1:-$DOMAIN}-error-${2:-$ENVIRONMENT}" \
    -H "Content-Type: application/json" \
    -d '{
    "topic": "'"projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-error-${2:-$ENVIRONMENT}"'"
    }'
    curl -X PUT "${PUBSUB_EMULATOR_HOST}/v1/projects/${PROJECT_ID}/subscriptions/${1:-$DOMAIN}-error-dead-${2:-$ENVIRONMENT}" \
    -H "Content-Type: application/json" \
    -d '{
    "topic": "'"projects/${PROJECT_ID}/topics/${1:-$DOMAIN}-error-dead-${2:-$ENVIRONMENT}"'"
    }'
}

alias gcps=gcloud-pubsub-start
alias gcpk=gcloud-pubsub-stop
alias gcpr=gcloud-pubsub-restart
alias gcpl=gcloud-pubsub-log
alias gcpt=gcloud-pubsub-status
alias gcpg=gcloud-pubsub-setup

# ----------------------------------------------------------------------

function _shutdown_datastore_database() {
    gcloud-pubsub-init
    curl -X POST ${DATASTORE_HOST}/shutdown
}

