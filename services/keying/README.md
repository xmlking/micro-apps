# 🔑 Keying

Keying gRPC service.

### 🚀 Run

```bash
# server
gradle :services:keying:run
# proxy
docker compose up envoy
# client -> proxy -> server
gradle :services:keying:runKeyingClient
# client -> server
CERTS_CACERT=config/certs/upstream-ca-cert.pem \
ACCOUNT_ENDPOINT=dns:///localhost:5002 \
ACCOUNT_AUTHORITY=localhost \
gradle :services:keying:runKeyingClient
```

### 🔭 Test

```bash
gradle :services:keying:test
gradle :services:keying:test -Dkotest.tags.exclude=Slow
gradle :services:keying:test -Dkotest.tags.include=E2E
```

### 📦 Build

```bash
# clean
gradle :services:keying:clean
# make fatJar
gradle :services:keying:build
# docker build
gradle :services:keying:jibDockerBuild
# prune dangling images.
docker image prune -f
# run image
docker run -it xmlking/micro-services-keying:1.6.5-SNAPSHOT
```

```bash
# custom build
gradle :services:keying:jib \
    -Djib.to.image=myregistry/myimage:latest \
    -Djib.to.auth.username=$USERNAME \
    -Djib.to.auth.password=$PASSWORD
```

## 🔗 Credits
