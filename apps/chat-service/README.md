# Chat App

**SpringBoot** app showcasing *Kotlin Coroutines* and *RSocket* 
   
## Run
```bash
gradle :apps:chat-service:bootRun
# log at debug level
gradle :apps:chat-service:bootRun --debug
```

Open two browser tabs and start chatting...
```bash
open http://localhost:8080/
```


### Reference 
* [Spring Boot with Kotlin and RSocket](https://spring.io/guides/tutorials/spring-webflux-kotlin-rsocket/)
* [Original Repo](https://github.com/kotlin-hands-on/kotlin-spring-chat)
