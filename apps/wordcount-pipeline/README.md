# WordCount

WordCount pipeline demo. 

### Run

#### Local Run  
```bash
gradle :apps:wordcount-pipeline:run --args="--runner=DirectRunner --inputFile=./src/test/resources/data/input.txt --output=./build/output.txt"

java -jar -Dflogger.level=INFO \
./apps/wordcount/build/libs/wordcount-0.1.6-SNAPSHOT-all.jar  \
--runner=DirectRunner \
--inputFile=./apps/wordcount/src/test/resources/data/input.txt \
--output=./apps/wordcount/build/output.txt
```

#### Cloud Run  
```bash
PROJECT_ID=<my-project-id>
PIPELINE_FOLDER=wordcount
export GOOGLE_APPLICATION_CREDENTIALS=<full-path-to-your-json>

gradle -Dflogger.level=ALL  :apps:wordcount-pipeline:run --args="--runner=DataflowRunner --project=$PROJECT_ID --gcpTempLocation==gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ --stagingLocation=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/staging/ --inputFile=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/input/shakespeare.txt --output=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/output/output.txt"

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

#### Creating Template
```bash
gradle :apps:wordcount-pipeline:run --args="--runner=DataflowRunner --project=$PROJECT_ID --gcpTempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ --stagingLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/staging/ --templateLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/template/${PIPELINE_NAME}"
```

> Creating as template from VM
```bash
java -jar /mnt/data/pipelines/templates/wordcount-0.1.6-SNAPSHOT-all.jar --runner=DataFlowRunner \
    --project=$PROJECT_ID \
    --gcpTempLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/temp/ \
    --stagingLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/staging/ \
    --templateLocation=gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/template/${PIPELINE_NAME}
```

#### Running template
> Create Job
```bash
gcloud dataflow jobs run wordcount \
    --gcs-location gs://${PROJECT_ID}/dataflow/pipelines/${PIPELINE_NAME}/template/${PIPELINE_NAME} \
    --parameters inputFile=gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/input/shakespeare.txt,gs://${PROJECT_ID/dataflow/pipelines/${PIPELINE_NAME}/output/output.txt
```

### Test
```bash
gradle :apps:wordcount-pipeline:test
```

### Build
```bash
# clean
gradle :apps:wordcount-pipeline:clean
# make fatJar
gradle :apps:wordcount-pipeline:build
```

### Kubernetes

Scheduled Apache Beam jobs using Kubernetes Cronjobs

```bash
kubectl apply -f config/base/beam/cronjob.yml
```
