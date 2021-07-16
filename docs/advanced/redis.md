# Redis

## Operations

### Prerequisites

```bash
brew install redis
brew install redis-developer/tap/riot-file
brew install redis-developer/tap/riot-gen
```

### Setup

```bash
# start local redis
docker compose -f infra/redis.yml up
# stop local redis before restart again
docker compose -f infra/redis.yml down
# this will stop redis and remove all volumes
docker compose -f infra/redis.yml down -v 
```

#### Redis Insight

Redis db visualization dashboard

> when prompted, use host:redis, port:6379 and name:any_name

```bash
open http://localhost:8001/
```

#### Redis Grafana

For the first time, enable **Redis-Application** plugin at:

http://localhost:3000/plugins/redis-app/

### Schema

### Queries

```bash
PING Marco!
MODULE LIST

KEYS *

HGETALL people:22a985b7-983a-419a-b1cd-91a9f8a044b2

# Delete all keys of the currently selected Redis database:
FLUSHDB
```

### Data Loading

## Tools

## Reference
- Redis Developer Community [Projects](https://github.com/redis-developer)
- [RIOT: Redis Input/Output Tools](https://github.com/redis-developer/riot)
- [Terraform Provider Redis Cloud](https://github.com/RedisLabs/terraform-provider-rediscloud)
- Command-line utility for load generation and bechmarking NoSQL key-value databases[memtier_benchmark](https://github.com/RedisLabs/memtier_benchmark)

- **Redis Search**
    - Fast and Furious: Searching in a Distributed World with Highly Available, [Spring Data Redis](https://www.youtube.com/watch?v=QZdUXrzdxos)
    - Source Code on [GitHub](https://github.com/Redislabs-Solution-Architects/rediscogs)
    - [RediSearch](https://volkovlabs.com/i-taught-my-wife-how-to-use-redisearch-2-0-77d6f32660df) with **NPI** and **CommonWell Health Alliance** data, and  [Redis Application plug-in for Grafana](https://grafana.com/grafana/plugins/redis-datasource/)
    - [Real-time observability with Redis and Grafana](https://docs.google.com/presentation/d/1dt4lduof6qIZF1dJ8Sv4_sCjKYHBY_a5ODAVQSEANgE/edit#slide=id.g9bf045ab42_0_40)

- **Redis Graph**
    - RedisGraph [commands](https://oss.redislabs.com/redisgraph/commands/)
    - [RedisGraph bulk loader](https://github.com/RedisGraph/redisgraph-bulk-loader)
    - [search-graph-demo](https://github.com/stockholmux/conf19-search-graph-demo)
    - [Redis and Spring: Building High Performance RESTful APIs](https://github.com/wilvdb/redi2read/blob/main/src/main/kotlin/com/redislabs/edu/redi2read/services/RecommendationService.kt)
    
- **Redis Spring**
    - [Spring Data Redis](https://docs.spring.io/spring-data/redis/docs/2.5.2/reference/html/#why-spring-redis)
    - [Accessing Data Reactively with Redis](https://spring.io/guides/gs/spring-data-reactive-redis/)
    - [Redis Reactive kotlin tests](https://github.com/spring-projects/spring-data-redis/blob/main/src/test/kotlin/org/springframework/data/redis/core/ReactiveHashOperationsExtensionsUnitTests.kt)
    
- **Redis Quarkus**
    - QUARKUS - USING THE [REDIS CLIENT](https://quarkus.io/guides/redis)

- **Example Projects**
    - [Microservices with Redis](https://github.com/redis-developer/redis-microservices-demo)
        - Has Streams, Graph and Search
        - Deploy the application on [Kubernetes](https://github.com/redis-developer/redis-microservices-demo/tree/master/kubernetes)
