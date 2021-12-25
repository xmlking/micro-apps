# WorkCount 

**Spring Cloud Streams** example app with **Kafka-Streams**

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
gradle :apps:wordcount-service:bootRun
# log at debug level
gradle :apps:wordcount-service:bootRun --debug
```

## Test

```bash
http :8080/iq/count/{word}
http :8080/iq/count/chuck

# health
http :8080/actuator
http :8080/actuator/health
http :8080/actuator/bindings
http :8080/actuator/bindings/state-out-0
````

## Reference
