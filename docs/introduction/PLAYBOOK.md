# DIY Playbook

Do-it-yourself step-by-step instructions to create this project structure from scratch.

### Prerequisites

> you need following tools. versions listed here are minimal versions tested.

| Software                      | Version         | Optional         |  
|-------------------------------|-----------------|------------------| 
| Java                          | 11.0.6          | 20.0.0.r11       | 
| Kotlin                        | 1.13.70         |                  | 
| Gradle                        | 6.2.2           |                  |
| IntelliJ                      |                 | 2020.1           |
| Docker for Mac                | latest          |                  |
| SDKMan                        | latest          |                  |
| Micronaut                     |                 | 1.0.0            |

### Install Prerequisites

```bash
# install or Update Node with brew or NVM
sdk install java 11.0.6.hs-adpt
sdk install java 8.0.242.hs-adpt
sdk install java 120.0.0.r11-grl 
sdk default java 20.0.0.r11-grl
sdk install gradle
# to remove old version e.g., gradle 4.10:
sdk remove gradle 4.10
sdk install kotlin 
sdk install micronaut
# Optional
sdk install maven
sdk install spark
sdk install
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

#### Generate Artifacts

TODO

```bash
cd apps/greeting-api
mn create-controller micro.apps.service.controllers.greeting
mn create-bean  micro.apps.service.services.greetingService
mn create-client greetingClient
```

