# 🔗 Linking

Linking gRPC service.

### 🚀 Run

```bash
# server
gradle :services:linking:run
# proxy
docker compose up envoy
# client -> proxy -> server
gradle :services:linking:runLinkingClient
# client -> server
CERTS_CACERT=config/certs/upstream-ca-cert.pem \
ACCOUNT_ENDPOINT=dns:///localhost:5003 \
ACCOUNT_AUTHORITY=localhost \
gradle :services:linking:runLinkingClient
```

### 🔭 Test

```bash
gradle :services:linking:test
gradle :services:linking:test -Dkotest.tags.exclude=Slow
gradle :services:linking:test -Dkotest.tags.include=E2E
```

### 📦 Build

```bash
# clean
gradle :services:linking:clean
# make fatJar
gradle :services:linking:build
# docker build
gradle :services:linking:jibDockerBuild
# prune dangling images.
docker image prune -f
# run image
docker run -it xmlking/micro-services-linking:1.6.5-SNAPSHOT
```

```bash
# custom build
gradle :services:linking:jib \
    -Djib.to.image=myregistry/myimage:latest \
    -Djib.to.auth.username=$USERNAME \
    -Djib.to.auth.password=$PASSWORD
```

## 🔗 Credits
