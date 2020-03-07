# DIY Playbook

Do-it-yourself step-by-step instructions to create this project structure from scratch.


### Prerequisites  
> you need following tools. versions listed here are minimal versions tested.

| Software                      | Version         | Optional         |  
|-------------------------------|-----------------|------------------| 
| Java                          | 8.0.181-oracle  | 1.0.0-rc6-graal  | 
| Spring Boot                   | 2.0.4.RELEASE   |                  |
| Micronaut                     |                 | 1.0.0            |
| Gradle                        | 4.10            |                  |
| Kotlin                        |                 | 1.2.61           |
| IntelliJ                      |                 | 2018.2           |
| Docker for Mac                | latest          |                  |
| SDKMan                        | latest          |                  |



### Install Prerequisites
```bash
# install or Update Node with brew or NVM
sdk install java 10.0.2-oracle
sdk install java 1.0.0-rc6-graal
sdk default java  1.0.0-rc6-graal
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

### Install Kubernetes (optional)
follow instructions [here](https://gist.github.com/xmlking/62ab53753c0f0f5247d0e174b31dab21) to install kubernetes toolchain:
1. Docker for Mac (edge version)
2. Helm (optional)
3. kubectx (optional)

#### Install Bazel (optional)
For Mac, install via Brew. [Instructions](https://docs.bazel.build/versions/master/install-os-x.html#install-on-mac-os-x-homebrew)
```bash
brew install bazel
bazel version
# you can upgrade to a newer version of Bazel with:
brew upgrade bazel

# if needed 
sudo xcode-select -s /Applications/Xcode.app/Contents/Developer
sudo xcodebuild -license
bazel clean --expunge
```

### Scaffold Project
> steps below are for setting up a new project from the scratch.


#### Create Workspace
TODO
```bash
 
```
 
#### Generate Artifacts
TODO
```bash
cd apps/greeting-api
mn create-controller micro.apps.greeting.controllers.greeting
mn create-bean  micro.apps.greeting.services.greetingService
mn create-client greetingClient
```

### Run

#### Docker
> start mongodb, kafka
```bash
# start local mongodb
docker-compose up -V mongodb
# stop local mongodb before restart again
docker-compose down -v
# start local kafka
docker-compose up broker
```

### Gradle Commands
```bash
# upgrade project gradle version
gradle wrapper --gradle-version 4.10 --distribution-type all
# gradle daemon status 
gradle --status
gradle --stop
# show dependencies
gradle classifier:dependencies
gradle classifier:dependencyInsight --dependency spring-messaging
# refresh dependencies
gradle build -x test --refresh-dependencies 
```

 
