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
nerdctl exec -it infra_redpanda_1 rpk topic produce facts -k my-key
"hi there"
# or
nerdctl exec -it infra_redpanda_1 /bin/bash
echo 'hi there' | rpk topic produce facts -k my-key
# consume
nerdctl exec -it infra_redpanda_1 rpk topic consume facts
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
http :8080/iq/count/norris
```

## Operations

### Metrics

```bash
http :8080/actuator

http :8080/actuator/health

http :8080/actuator/metrics
http :8080/actuator/metrics/kafka.admin.client.request.total

http :8080/actuator/bindings
http :8080/actuator/bindings/processWords-in-0
http :8080/actuator/bindings/produceChuckNorris-out-0
http :8080/actuator/bindings/consumeCounts-in-0
````

### Binding control

```bash
curl -d '{"state":"STOPPED"}' -H "Content-Type: application/json" -X POST localhost:8080/actuator/bindings/consumeCounts-in-0
http :8080/actuator/bindings/consumeCounts-in-0
curl -d '{"state":"STARTED"}' -H "Content-Type: application/json" -X POST localhost:8080/actuator/bindings/consumeCounts-in-0
curl -d '{"state":"PAUSED"}'  -H "Content-Type: application/json" -X POST localhost:8080/actuator/bindings/consumeCounts-in-0
curl -d '{"state":"RESUMED"}' -H "Content-Type: application/json" -X POST localhost:8080/actuator/bindings/consumeCounts-in-0
```

## Reference
