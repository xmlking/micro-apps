# GCS

# list keystore files in GCS bucket
```
export GOOGLE_APPLICATION_CREDENTIALS=/Users/sumo/my-gcs.json
gcloud auth activate-service-account --key-file $GOOGLE_APPLICATION_CREDENTIALS
gsutil ls gs://my-project-bucket-name/files
```
