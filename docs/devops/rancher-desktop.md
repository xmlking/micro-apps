# Rancher Desktop

**Rancher Desktop** is a replacement for **Docker for Mac**

It installs:
1. [k3s](https://k3s.io/)
2. [traefik](https://traefik.io/)
3. [lima](https://github.com/lima-vm/lima)
4. Kubernetes Image Manager (kim)
5. kubectl
6. nerdctl

## Prerequisites 

You **don't** need `Docker for Mac` installed as prerequisite

## Install

Download and install the latest binary for your OS from [here](https://github.com/rancher-sandbox/rancher-desktop/releases)

## Usage

```bash
docker info 
docker version
docker stats
docker top CONTAINER
docker volume ls
docker network ls
```

### Namespace management

```bash
docker namespace ls
```

### Images

```bash
docker build .
docker tag
docker tag SOURCE_IMAGE[:TAG] TARGET_IMAGE[:TAG]

docker images

docker login -u aaaa -p bbb

# inspect image 
docker image inspect redislabs/redismod:edge

# Remove one or more images
docker  rmi docker.vectorized.io/vectorized/redpanda:v21.11.2

# default from docker.io
docker  pull jwsy/jade-shooter:v1.1

docker  images | grep jwsy
docker  run -d -p 8080:80 jwsy/jade-shooter:v1.1
docker  run -d -p 80:80 --name=nginx --restart=always nginx

# `e2a5` is output from above command
docker  exec -it e2a5 sh
docker  images
# save load  
docker  save -o local_jwsy_jade-shooter_v1.2.tar
docker  load -i local_jwsy_jade-shooter_v1.2.tar
```

Encrypt image layers with [ocicrypt](https://github.com/containerd/docker/blob/master/docs/ocicrypt.md).

```bash
openssl genrsa -out mykey.pem
openssl rsa -in mykey.pem -pubout -out mypubkey.pem
docker image encrypt --recipient=jwe:mypubkey.pem --platform=linux/amd64,linux/arm64 foo example.com/foo:encrypted
docker push example.com/foo:encrypted
```

### Compose

```bash
docker compose -f infra/redis.yml up redis
docker compose -f infra/redpanda.yml up redpanda
docker compose -f infra/redpanda.yml logs
docker compose -f infra/redis.yml down
docker compose -f infra/redpanda.yml down
# this will stop redpanda and remove all volumes
docker compose -f infra/redpanda.yml down -v 

docker compose -f infra/redpanda.yml ps
# name of the container can be found from output of above command 
docker exec -it infra-redpanda-1 /bin/bash
docker exec -it infra-redpanda-1 rpk version
docker exec -it infra-redpanda-1 rpk topic list
docker exec -it infra-redpanda-1 rpk cluster info


docker exec -it redpanda-1 \
rpk topic produce twitch_chat --brokers=localhost:9092
docker exec -it redpanda-1 \
rpk topic consume twitch_chat --brokers=localhost:9092
```

### Kubernetes

```bash
kubectl apply -f jade-shooter
```

How to expose traefik v2 dashboard?

https://github.com/rancher-sandbox/rancher-desktop/issues/896

create `dashboard.yaml` route

```yaml
apiVersion: traefik.containo.us/v1alpha1
kind: IngressRoute
metadata:
  name: dashboard
spec:
  entryPoints:
    - web
  routes:
    - match: Host(`traefik.localhost`) && (PathPrefix(`/dashboard`) || PathPrefix(`/api`))
      kind: Rule
      services:
        - name: api@internal
          kind: TraefikService
```


```bash
kubectl -n kube-system apply -f dashboard.yaml
```

open dashboard in your favorite browser and **don't forget the second slash**

```bash
open http://traefik.localhost/dashboard/#/
```
