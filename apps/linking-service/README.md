# 🔗 Linking

Linking gRPC service.

### 🚀 Run

```bash
# server
gradle :apps:linking-service:run
# proxy
docker compose up envoy
# client -> proxy -> server
gradle :apps:linking-service:runLinkingClient
# client -> server
CERTS_CACERT=config/certs/upstream-ca-cert.pem \
ACCOUNT_ENDPOINT=dns:///localhost:5000 \
ACCOUNT_AUTHORITY=localhost \
gradle :apps:linking-service:runLinkingClient
```

### 🔭 Test

```bash
gradle :apps:linking-service:test
gradle :apps:linking-service:test -Dkotest.tags.exclude=Slow
gradle :apps:linking-service:test -Dkotest.tags.include=E2E
```

### 📦 Build

```bash
# clean
gradle :apps:linking-service:clean
# make fatJar
gradle :apps:linking-service:build
# docker build
gradle :apps:linking-service:jibDockerBuild
# prune dangling images.
docker image prune -f
# run image
docker run -it xmlking/micro-apps-linking-service:1.6.5-SNAPSHOT
```

```bash
# custom build
gradle :apps:linking-service:jib \
    -Djib.to.image=myregistry/myimage:latest \
    -Djib.to.auth.username=$USERNAME \
    -Djib.to.auth.password=$PASSWORD
```

## 🔗 Credits
