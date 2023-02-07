package micro.apps.service.config

import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.adapter.HttpWebHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder

@Configuration(proxyBeanMethods = false)
@ImportRuntimeHints(MessageSourceConfig.GraphQlRuntimeHints::class)
class MessageSourceConfig {
    @Bean
    fun httpHandler(applicationContext: ApplicationContext): HttpHandler {
        val delegate = WebHttpHandlerBuilder
            .applicationContext(applicationContext).build()
        return object : HttpWebHandlerAdapter((delegate as HttpWebHandlerAdapter)) {
            override fun createExchange(
                request: ServerHttpRequest,
                response: ServerHttpResponse
            ): ServerWebExchange {
                val serverWebExchange = super
                    .createExchange(request, response)
                val localeContext = serverWebExchange.localeContext
                LocaleContextHolder.setLocaleContext(localeContext)
                return serverWebExchange
            }
        }
    }

    internal class GraphQlRuntimeHints : RuntimeHintsRegistrar {
        override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
            hints.resources().registerResourceBundle("i18n.messages")
        }
    }
}
