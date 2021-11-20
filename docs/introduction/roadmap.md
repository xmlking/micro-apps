# Roadmap

##  Purposes

- **Integrated Observability:** Currently Kubernetes-based service mesh only can see the ingress/egress traffic, and it has no idea what's happened in service/application. 
So, combining with Java Agent technology, we can have the full capability to observe everything inside and outside of service/application.

Shortly, we leverage the *Kubernetes operators* and *Java Agent* techniques to make Java applications have service governance and integrated observability without change a line of source code.

## Principles

- **Service Insight:** Service running metrics/tracing/logs monitoring.
- **Adaptive Limits:** [concurrency-limits](https://netflixtechblog.medium.com/performance-under-load-3e6fa9a60581)
- **Infrastructure Runtime Resource:** The underlying computing node, storage, and network are all managed and scheduled by container and Kubernetes.
- **Traffic Orchestration:** Most of the online services need to deal with user requests from all over the world. That traffic would have different purposes and access different resources. So, it’s very important to manage that traffic and align it with Cloud Native backend services.

## Architecture

**Control Plane - Data Plane** model

## Features

- **Non-intrusive Design:** Zero code modification for Java Spring Cloud application migration, only small configuration update needed.
- **Java Register/Discovery:** Compatible with popular Java Spring Cloud ecosystem's Service registry/discovery
- Resource Management: Rely on Kubernetes platform for CPU/Memory resources management.
- **Traffic Orchestration**
  - **Rich Routing Rules:** Exact path, path prefix, regular expression of the path, method, headers.
  - **Traffic Splitting** Coloring & Scheduling east-west and north-south traffic to configured services.
  - **Load Balance** Support Round Robin, Weight Round Robin, Random, Hash by Client IP Address, Hash by HTTP Headers.
- **Resilience:** Including Timeout/CircuitBreaker/Retryer/Limiter, completely follow sophisticated resilience design. 
  - **Resilience & Fault Tolerance**
    - **Circuit breaker:** Temporarily blocks possible failures. 
    - **Rate limiter:** Limits the rate of incoming requests. 
    - **Retryer:** Repeats failed executions. 
    - **Time limiter:** Limits the duration of execution. 
  - **Chaos engineering**
    - **Fault injection** Working in progress.
    - **Delay injection** Working in progress.
  - **Service Observability:**
    - **Logs**
    - **Tracing**
        - Service Tracing — built-in Open Zipkin, and Open Tracing for vendor-neutral APIs.
    - **Metrics**
      - Throughput — QPS/TPS m1, m5, m15 and error rate
      - Latency — P999, P99, P98, P95, P75, P50, P25
      - Bandwidth — request and response data size
      - Response — HTTP Status Code statistics.
  - **Security**
    - **mTLS** Working in progress.
    - **mTLS Enforcement** Working in progress.
    - **External CA certificate** Working in progress.
    - **Service-to-Service Authorization Rules** Working in progress.


## Reference 

- [The Next Generation Service Gateway](https://medium.com/codex/the-next-generation-service-gateway-7cf4bd50c9bd)
- [easemesh](https://github.com/megaease/easemesh)
