package micro.apps.service

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/*
import org.springframework.nativex.hint.InitializationHint
import org.springframework.nativex.hint.InitializationTime
import org.springframework.nativex.hint.NativeHint

@NativeHint(
    // options = ["--enable-https", "-H:+AddAllCharsets", "-H:+ReportExceptionStackTraces", "-H:+ReportUnsupportedElementsAtRuntime"],
    // FIXME: java.lang.ClassNotFoundException: org.bouncycastle.jsse.BCSSLEngine https://github.com/netty/netty/issues/11369
    initialization = [
        InitializationHint(
            typeNames = ["org.slf4j.jul.JDK14LoggerAdapter", "org.slf4j.simple.SimpleLogger"],
            packageNames = ["org.slf4j.jul", "org.slf4j.simple"],
            initTime = InitializationTime.BUILD
        )
    ]
)
*/

@SpringBootApplication
class EntityApplication

fun main(args: Array<String>) {
    runApplication<EntityApplication>(*args)
}
