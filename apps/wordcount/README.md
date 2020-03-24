# WordCount

WordCount pipeline demo. 

### Run

#### Local Run  
```bash
gradle :apps:wordcount:run --args="--runner=DirectRunner --inputFile=./src/test/resources/data/input.txt --output=./build/output.txt"

java -jar -Dflogger.level=INFO \
./apps/wordcount/build/libs/wordcount-0.1.6-SNAPSHOT-all.jar  \
--runner=DirectRunner \
--inputFile=./apps/wordcount/src/test/resources/data/input.txt \
--output=./apps/wordcount/build/output.txt
```

#### Cloud Run  
```bash
PROJECT_ID=<my-project-id>
GCS_BUCKET=<my-project-gcs-bucket>
export GOOGLE_APPLICATION_CREDENTIALS=<full-path-to-your-json>

gradle -Dflogger.level=ALL  :apps:wordcount:run --args="--runner=DataflowRunner --project=$PROJECT_ID --gcpTempLocation=gs://$GCS_BUCKET/dataflow/wordcount/temp/ --stagingLocation=gs://$GCS_BUCKET/dataflow/wordcount/staging/ --inputFile=gs://$GCS_BUCKET/dataflow/wordcount/input/shakespeare.txt --output=gs://$GCS_BUCKET/dataflow/wordcount/output/output.txt"

# Or with fatJar
java -jar ./apps/wordcount/build/libs/wordcount-0.1.6-SNAPSHOT-all.jar  \
--runner=DataflowRunner \
--project=$PROJECT_ID \
--gcpTempLocation=gs://$GCS_BUCKET/dataflow/wordcount/temp/ \
--stagingLocation=gs://$GCS_BUCKET/dataflow/wordcount/staging/ \
--inputFile=gs://$GCS_BUCKET/dataflow/wordcount/input/shakespeare.txt \
--output=gs://$GCS_BUCKET/dataflow/wordcount/output/output.txt
```

#### Creating Template
```bash
gradle :apps:wordcount:run --args="--runner=DataflowRunner --project=$PROJECT_ID --gcpTempLocation=gs://$GCS_BUCKET/dataflow/wordcount/temp/ --stagingLocation=gs://$GCS_BUCKET/dataflow/wordcount/staging/ --templateLocation=gs://$GCS_BUCKET/dataflow/wordcount/template/wordcount"
```

> Creating as template from VM
```bash
java -jar /mnt/data/pipelines/templates/wordcount-0.1.6-SNAPSHOT-all.jar --runner=DataFlowRunner \
    --project=$PROJECT_ID \
    --gcpTempLocation=gs://$GCS_BUCKET/dataflow/wordcount/temp/ 
    --stagingLocation=gs://$GCS_BUCKET/dataflow/wordcount/staging/
    --templateLocation=gs://$GCS_BUCKET/dataflow/wordcount/template/wordcount
```

#### Running template
> Create Job
```bash
gcloud dataflow jobs run wordcount \
    --gcs-location gs://$GCS_BUCKET/dataflow/wordcount/template/wordcount \
    --parameters inputFile=gs://$GCS_BUCKET/dataflow/wordcount/input/shakespeare.txt,outputFile=gs://$GCS_BUCKET/dataflow/wordcount/output/output.txt
```

### Test
```bash
gradle :apps:wordcount:test
```

### Build
```bash
# clean
gradle :apps:wordcount:clean
# make fatJar
gradle :apps:wordcount:build
```
