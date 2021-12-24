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
```

Start µService 

```bash
gradle :apps:streams-service:bootRun
# log at debug level
gradle :apps:streams-service:bootRun --debug
```

## Test

```bash
# health
http :8080/actuator
http :8080/actuator/health
http :8080/actuator/bindings
http :8080/actuator/bindings/state-out-0
````


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

### Example projects 
- https://github.com/spring-cloud/spring-cloud-stream-samples/
- https://github.com/spring-cloud/spring-cloud-stream-samples/tree/main/kafka-streams-samples
- InteractiveQueryService https://github.com/piomin/sample-spring-cloud-stream-kafka/blob/master/stock-service/src/main/java/pl/piomin/samples/kafka/stock/controller/TransactionController.java
- https://github.com/spring-cloud/spring-cloud-stream-samples/blob/main/kafka-streams-samples/kafka-streams-inventory-count/src/main/java/kafka/streams/inventory/count/KafkaStreamsInventoryCountApplication.java

