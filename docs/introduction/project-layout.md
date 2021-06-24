# Project Layout

Project layout, Domain driven development, Single Responsibility Principle

## Project structure

we will be using **monorepo** style project layout, utilizing **gradle** multi-project build system.

```
micro-apps/
├── apps
│   ├── account-service
│   │   ├── README.md
│   │   ├── build.gradle.kts
│   │   └── src
│   ├── greeting-service
│   ├── streaming-quarkus
│   └── wordcount-pipeline
├── libs
│   ├── core
│   │   ├── README.md
│   │   ├── build.gradle.kts
│   │   └── src
│   ├── kbeam
│   ├── model
│   └── proto
├── config     <-- deployment configuration ie., k8s yaml
│   ├── base
│   └── envs
├── docs
├── gradle
│   └── wrapper
├── gradle.properties
├── build.gradle.kts
└── settings.gradle.kts 
```

### sub-projects

- apps
    - contains runnable `apps`. i.e., microservices, cli tools, ETL pipelines etc
    - can be build into `fatJars` and/or `docker` images
    - there should be only one entry point per app. i.e, one top-level class with `main()` method.
    - `apps` imports local `libs` as needed `implementation(project(":libs:dlib"))`

- libs
    - libraries are two types: `core` and `shared`
    - `core` type libraries are shared libraries, imported in all `apps` or in all `apps` of specific type.
      e.g., `models`, `kbeam`
    - `shared` type libraries are reusable libraries focused on solving only one type of problem,
      e.g., `encryption-utils` `avro-utils`

#### Rules

- Keep **core** liberties as slim as possible (zero or fewer dependencies)
- Keep **shared** liberties focus on doing one thing. i.e, `avro-utils`, `encryption-utils`, `beam-utils` etc
- Add system-wide 3-party dependencies in root `build.gradle.kts` and project specific dependencies in
  sub-project's `build.gradle.kts`
- Keep all 3-party dependencies' version management in `gradle.properties` UpToDate.
