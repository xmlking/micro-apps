# Redpanda

we are using **Redpanda** as drop-in replacement for **Kafka** broker

## usage

### Start

```bash
# start redpanda
docker compose -f infra/redpanda.yml up redpanda
# this will stop redis and remove all volumes (-v to remove volumes)
docker compose -f infra/redpanda.yml down redpanda -v 
# create topic
docker compose -f infra/redpanda.yml up topic-creator

# debug
docker compose -f infra/redpanda.yml ps
# name of the container can be found from output of the above command 
docker exec -it infra-redpanda-1 /bin/bash
docker exec -it infra-redpanda-1 rpk version
docker exec -it infra-redpanda-1 rpk topic list
docker exec -it infra-redpanda-1 rpk cluster info
docker exec -it infra-redpanda-1 rpk topic delete state-out-0 city-in-0

# produce
docker exec -it infra-redpanda-1 rpk topic produce all-in-topic -k my-key
{"name": "Red", "city": "nuur", "state": "ca"}
{"name": "Red2", "city": "nuur2", "state": "ca"}
# or
docker exec -it infra-redpanda-1 /bin/bash
echo '{"name": "Red", "city": "nuur", "state": "ca"}' | rpk topic produce all-in-topic -k my-key
# consume
docker exec -it infra-redpanda-1 rpk topic consume all-in-topic
```

### Access

#### Schema Registry

Endpoints are documented with Swagger at http://localhost:8081/v1

> The currently supported schema type is AVRO, we plan to support JSON and PROTOBUF.

```bash
curl -s "http://localhost:8081/schemas/types" | jq .
```
Publish a avro schema

```bash
curl -s \
  -X POST \
  "http://localhost:8081/subjects/sensor-value/versions" \
  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  -d '{"schema": "{\"type\":\"record\",\"name\":\"sensor_sample\",\"fields\":[{\"name\":\"timestamp\",\"type\":\"long\",\"logicalType\":\"timestamp-millis\"},{\"name\":\"identifier\",\"type\":\"string\",\"logicalType\":\"uuid\"},{\"name\":\"value\",\"type\":\"long\"}]}"}' \
  | jq
```

Import a Protobuf schema with a message called Simple and subject **simple.proto**:

```bash
curl -s \
  -X POST \
  "http://localhost:8081/subjects/simple.proto/versions" \
  -H "Content-Type: application/json" \
  -d '{"schema":"syntax = \"proto3\"; message Simple { string id = 1; }","schemaType":"PROTOBUF"}' \
  | jq
```

Retrieve the schema by its ID

```bash
curl -s "http://localhost:8081/schemas/ids/1" | jq .
```

List the subjects
```bash
curl -s \
  "http://localhost:8081/subjects" \
  | jq .
```

Retrieve the schema versions for the subject

```bash
curl -s \
  "http://localhost:8081/subjects/sensor-value/versions" \
  | jq .
```

Retrieve a schema for the subject
```bash
curl -s \
  "http://localhost:8081/subjects/sensor-value/versions/1" \
  | jq .
```
```bash
curl -s \
"http://localhost:8081/subjects/sensor-value/versions/latest" \
| jq .
```

```bash
curl -s \
  "http://localhost:8081/subjects/sensor-value/versions/latest/schema" \
  | jq .
```

Schema types

```bash
curl -s \
  "http://localhost:8081/schemas/types" \
  | jq .
```


## Reference 
- Redpanda's [schema registry](https://vectorized.io/blog/schema_registry/)
- [Redpanda Quickstart](https://docs.redpanda.com/docs/get-started/quick-start/quick-start-docker/?num-brokers=one)
