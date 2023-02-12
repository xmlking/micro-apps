# FAQ

- How to get _commitizen_ and _cz-conventional-changelog_ work for `non-node-git-repo`?

  You can get _commitizen_ and _cz-conventional-changelog_ work for `non-node-git-repo` with global installation. Here
  are the steps that I have followed.

  ```bash
  yarn global add commitizen cz-conventional-changelog
  echo '{ "path": "cz-conventional-changelog" }' > ~/.czrc; # will create .czrc
  cd non-node-git-repo
  touch foo
  git cz -a
  ```
  
- How to setup IntelliJ IDEA for Spring Boot?
    - 
    - Make sure Spring Boot plugin in enabled in menu *File | Settings | Plugins | Spring Boot*
    - Enable [annotation processing](https://www.jetbrains.com/help/idea/annotation-processors-support.html) via menu *File | Settings | Build, Execution, Deployment | Compiler | Annotation Processors | Enable annotation processing*
    - Since [Kapt is not yet integrated in IDEA](https://youtrack.jetbrains.com/issue/KT-15040), you need to run manually the command `gradle kaptKotlin` to generate the metadata

- How to configure SLF4J with different logger implementations?
    - 
    - [How to configure SLF4J with different logger implementations](http://saltnlight5.blogspot.com/2013/08/how-to-configure-slf4j-with-different.html)
    - [Addressing the complexity of the Java logging ecosystem with capabilitiesblog](https://blog.gradle.org/addressing-logging-complexity-capabilities)
    - [logging-capabilities gradle plugin](https://github.com/ljacomet/logging-capabilities)

- Difference between @Valid and @Validated in Spring?
    - 
    - **@Validated** is for "validation groups", This can be used in multi-step forms
    - **@Valid** is for one-step validation
    - Ref [stackoverflow](https://stackoverflow.com/questions/36173332/difference-between-valid-and-validated-in-spring)

- How to use lombok with kotlin-spring-boot projects?
  - 
  - Add `alias(libs.plugins.gradle.lombok)` and `alias(libs.plugins.kotlin.lombok)` plugins to subproject's `build.gradle.kts`.
  - No need to add `compileOnly(libs.lombok)` and `annotationProcessor(libs.lombok)` dependencies.
  - You can only add lombok annotations to java classes.
  - Kotlin compiler ignores Lombok annotations if you use them in Kotlin code.

- Logging with slf4j fluent API
   - 
   - We are using slf4j 2.0.0's [fluent API](http://www.slf4j.org/manual.html#fluent) for logging.
   - declare logger for each class `private val logger = KotlinLogging.logger {}`

- Spring WebFlux and Exception handing
  -
  - [Spring WebFlux and domain exceptions](https://blog.softwaremill.com/spring-webflux-and-domain-exceptions-10ae2096b159)
  - Sample [Code](https://github.com/Opalo/spring-webflux-and-domain-exceptions) for above Blog
