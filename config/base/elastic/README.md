# Elasticsearch

Elasticsearch is a distributed, RESTful search and analytics engine capable of solving a growing number of use cases. As the heart of the Elastic Stack, it centrally stores your data so you can discover the expected and uncover the unexpected.

>This default distribution is governed by the Elastic License, and includes the [full set of free features](https://www.elastic.co/subscriptions).

This will run Elasticsearch in a single node via `env` variable baked into the container, we can run the container service via `docker run` command or in Kubernetes `kubectl apply -k deployment/`.

## Installed Plugins list

- repository-s3
- discovery-ec2
- mapper-size
- mapper-murmur3
- mapper-annotated-text
- ingest-attachment
- analysis-icu
- analysis-phonetic
