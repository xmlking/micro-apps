# Java Prerequisites
 
### Prerequisites

> you need following tools. versions listed here are minimal versions tested.

| Software                      | Version  | Optional |  
|-------------------------------|----------|----------| 
| Java                          | 21-graal | 17-graal | 
| Kotlin                        | 1.9.20   |          |
| Gradle                        | 8.4      |          |
| IntelliJ                      | 2023.2.3 |          |
| Docker for Mac                | latest   |          |
| SDKMan                        | latest   |          |


### Install Prerequisites

```bash
# install or Update Node with brew or NVM
sdk install java 21-graal
sdk install gradle
# to remove old version e.g., gradle 8.4:
sdk remove gradle 8.4
sdk install kotlin 
# Optional
sdk install maven
#sdkman self upgrade
sdk selfupdate
```

### IntelliJ IDEA

Be sure to enable delegate IDE build/run actions to Gradle so that Intellij does not use its internal build mechanism to
compile source code.

```
Settings -> Build, Execution, Deployment
  -> Build Tools -> Gradle -> Runner
  -> Delegate IDE build/run actions to gradle.
```

Point to local Gradle instead of gradle in wrapper

```
Settings -> Build, Execution, Deployment
    -> Build Tools -> Gradle
    -> Gradle -> set 'Use Gradle From' to 'Specified Loction' to local gradle for eg '/Users/{user-name}/.sdkman/candidates/gradle/6.0.1' 
```

Install IntelliJ Plugins

1. SonarLint
2. Detekt
3. [kotest](https://plugins.jetbrains.com/plugin/14080-kotest)

### Lint

```bash
# help
ktlint --help
# one time apply ktlint to intelliJ
# it makes Intellij IDEA's built-in formatter produce 100% ktlint-compatible code.
 ktlint applyToIDEAProject -y
# lint
ktlint
```

### Install Kubernetes (optional)

follow instructions [here](https://gist.github.com/xmlking/62ab53753c0f0f5247d0e174b31dab21) to install kubernetes
toolchain:

1. Docker for Mac (edge version)
2. Kustomize (optional)
3. kubectx (optional)

### Scaffold Project

> steps below are for setting up a new project from the scratch.

#### Create Workspace

```bash
mkdir micro-apps && cd micro-apps
gradle init --type kotlin-application --dsl kotlin
```

