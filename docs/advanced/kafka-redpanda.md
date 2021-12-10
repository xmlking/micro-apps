# Kafka

we are using **Redpanda** as  **Kafka** broker

## usage

### Start

```bash
# start redpanda
docker compose -f infra/redpanda.yml up redpanda
# this will stop redis and remove all volumes (-v to remove volumes)
docker compose -f infra/redpanda.yml down redpanda -v 
# create topic
docker compose -f infra/redpanda.yml up topic-creator
```

### Access

#### Schema Registry

Endpoints are documented with Swagger at http://localhost:8081/v1

> The currently supported schema type is AVRO, we plan to support JSON and PROTOBUF.

```bash
curl -s "http://localhost:8081/schemas/types" | jq .
```
Publish a schema

```bash
curl -s \
  -X POST \
  "http://localhost:8081/subjects/sensor-value/versions" \
  -H "Content-Type: application/vnd.schemaregistry.v1+json" \
  -d '{"schema": "{\"type\":\"record\",\"name\":\"sensor_sample\",\"fields\":[{\"name\":\"timestamp\",\"type\":\"long\",\"logicalType\":\"timestamp-millis\"},{\"name\":\"identifier\",\"type\":\"string\",\"logicalType\":\"uuid\"},{\"name\":\"value\",\"type\":\"long\"}]}"}' \
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



## Reference 
- Redpanda's [schema registry](https://vectorized.io/blog/schema_registry/)
