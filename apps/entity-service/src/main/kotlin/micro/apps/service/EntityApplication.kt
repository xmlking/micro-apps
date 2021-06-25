package micro.apps.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

// @NativeHint(options = ["-H:+AddAllCharsets", "-H:+ReportExceptionStackTraces", "-H:+ReportUnsupportedElementsAtRuntime"])
// @TypeHint(types = [BCSSLEngine::class])
@SpringBootApplication
class EntityApplication

fun main(args: Array<String>) {
    runApplication<EntityApplication>(*args)
}
