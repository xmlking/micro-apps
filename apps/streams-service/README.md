# Streams

**Spring Cloud Streams** example

## Run

Start kafka

```bash
nerdctl compose -f infra/redpanda.yml up redpanda

nerdctl exec -it infra_redpanda_1 /bin/bash
nerdctl exec -it infra_redpanda_1 rpk version
nerdctl exec -it infra_redpanda_1 rpk cluster info
nerdctl exec -it infra_redpanda_1 rpk topic delete state-out-0 city-in-0

# produce
nerdctl exec -it infra_redpanda_1 rpk topic produce all-in-topic -k my-key
{"name": "Red", "city": "nuur", "state": "ca"}
{"name": "Red2", "city": "nuur2", "state": "ca"}
# or
nerdctl exec -it infra_redpanda_1 /bin/bash
echo '{"name": "Red", "city": "nuur", "state": "ca"}' | rpk topic produce all-in-topic -k my-key
# consume
nerdctl exec -it infra_redpanda_1 rpk topic consume all-in-topic
nerdctl exec -it infra_redpanda_1 rpk topic consume state-out-topic
nerdctl exec -it infra_redpanda_1 rpk topic consume city-out-topic
```

Start µService 

```bash
gradle :apps:streams-service:bootRun
# log at debug level
gradle :apps:streams-service:bootRun --debug
```

## Build

```bash
gradle apps:streams-service:spotlessApply
gradle apps:streams-service:build
```

## Test

```bash
# list all schemas 
curl -s \
  "http://localhost:8081/subjects" \
  | jq .
# get schemas for `all-in-topic-value`
curl -s \
  "http://localhost:8081/subjects/all-in-topic-value/versions/1" \
  | jq '.schema | fromjson' 
# (or) you can see ` "sensitive": "true"` property.
curl -s \
  "http://localhost:8081/subjects/all-in-topic-value/versions/latest/schema" \
  | jq .
```

## Operations

### Metrics

```bash
http :8080/actuator

http :8080/actuator/health

http :8080/actuator/metrics
http :8080/actuator/metrics/kafka.admin.client.request.total

http :8080/actuator/bindings
http :8080/actuator/bindings/state-out-0
http :8080/actuator/bindings/generate-in-0
http :8080/actuator/bindings/print-in-0

http :8080/actuator/kafkastreamstopology
http :8080/actuator/kafkastreamstopology/<application-id of the processor>
http :8080/actuator/kafkastreamstopology/state-applicationId
http :8080/actuator/kafkastreamstopology/city-applicationId
http :8080/actuator/kafkastreamstopology/print-applicationId
````

### Binding control

```bash
curl -d '{"state":"STOPPED"}' -H "Content-Type: application/json" -X POST localhost:8080/actuator/bindings/print-in-0
http :8080/actuator/bindings/print-in-0
curl -d '{"state":"STARTED"}' -H "Content-Type: application/json" -X POST localhost:8080/actuator/bindings/print-in-0
curl -d '{"state":"PAUSED"}'  -H "Content-Type: application/json" -X POST localhost:8080/actuator/bindings/print-in-0
curl -d '{"state":"RESUMED"}' -H "Content-Type: application/json" -X POST localhost:8080/actuator/bindings/print-in-0
```

### Binders 
we need add `kafka` binder for `Supplier` functions to work
We can only use `Consumer` and `Function` functions with `KStream` binder.

```gradle
implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka")
implementation("org.springframework.cloud:spring-cloud-stream-binder-kafka-streams")
 ```

## Reference
- [Fictional Spring Cloud Streams](https://github.com/spring-cloud/spring-cloud-stream/blob/main/docs/src/main/asciidoc/spring-cloud-stream.adoc#functions-with-multiple-input-and-output-arguments)
- [Introducing Java Functions for Spring Cloud Stream Applications - Part 0](https://spring.io/blog/2020/07/13/introducing-java-functions-for-spring-cloud-stream-applications-part-0)
- [spring-cloud-stream-binder-kafka Docs](https://cloud.spring.io/spring-cloud-static/spring-cloud-stream-binder-kafka/)
- [No need for Schema Registry in your Spring-Kafka tests](https://medium.com/@igorvlahek1/no-need-for-schema-registry-in-your-spring-kafka-tests-a5b81468a0e1)
### Example projects 
- https://github.com/spring-cloud/spring-cloud-stream-samples/
- https://github.com/spring-cloud/spring-cloud-stream-samples/tree/main/kafka-streams-samples
- InteractiveQueryService https://github.com/piomin/sample-spring-cloud-stream-kafka/blob/master/stock-service/src/main/java/pl/piomin/samples/kafka/stock/controller/TransactionController.java
- https://github.com/spring-cloud/spring-cloud-stream-samples/blob/main/kafka-streams-samples/kafka-streams-inventory-count/src/main/java/kafka/streams/inventory/count/KafkaStreamsInventoryCountApplication.java
- https://github.com/ru-rocker/kafka-stream-employee-example
