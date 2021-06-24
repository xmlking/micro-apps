# GitHub Actions

- build executable in native mode
- put the executable in a Docker image
- publish the docker image to a remote registry
- deploy it on GCP with Cloud Run

See [github actions](../../.github/main.yml)

## GitHub Workflows

- Push Pipeline
- Pull Request(PR) Pipeline
- Release Pipeline
- Deployment Pipeline

### Feature Branch Push Pipeline

- lint
- unit tests

### PR Pipeline

> triggered when PR is created for `develop` branch

- lint
- unit tests
- integration tests

### Release Pipeline

- build docker images
- sign images
- push images to GCR
- generate build/kubernetes.yaml for k8s with Helm or Kustomize
- generate release on GitHub

### Deployment Pipeline

- Deploy to KinD on CI
- E2E Test

- deploy to GKE

## Reference

- https://medium.com/@max.day/how-to-use-github-actions-to-deploy-your-quarkus-app-to-gcp-6ed5d9fdecb3
- https://github.com/maxday/quarkus-demo-actions
