package micro.apps.service.config

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.metrics.GlobalMeterProvider
import io.opentelemetry.api.metrics.Meter
import io.opentelemetry.api.metrics.MeterProvider
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.TracerProvider
import io.opentelemetry.exporter.prometheus.PrometheusCollector
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.prometheus.client.CollectorRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class OtelConfig {
    @Bean
    fun otelTracer(): Tracer {

        val tracerProvider: TracerProvider = GlobalOpenTelemetry.getTracerProvider()
        return tracerProvider.tracerBuilder(tracerName)
            .setInstrumentationVersion("0.5")
            .build()
    }

    @Bean
    fun otelMeter(): Meter {
        val meterProvider: MeterProvider = GlobalMeterProvider.get()
        return meterProvider.meterBuilder(metricsName)
            // .setSchemaUrl("https://opentelemetry.io/schemas/1.5.0")
            .setInstrumentationVersion("1.0.0")
            .build()
    }

    @Bean
    @Primary
    fun otelMeterSpring(collectorRegistry: CollectorRegistry): Meter {
        val meterProvider = SdkMeterProvider.builder().buildAndRegisterGlobal()
        val prometheusCollector = PrometheusCollector.builder().setMetricProducer(meterProvider).buildAndRegister()
        collectorRegistry.register(prometheusCollector) // register Prometheus with Spring Boot Actuator

        return meterProvider.meterBuilder(metricsName)
            // .setSchemaUrl("https://opentelemetry.io/schemas/1.5.0")
            .setInstrumentationVersion("1.0.0")
            .build()
    }

    companion object {
        private const val tracerName = "micro.apps.spring.service.trace"
        private const val metricsName = "micro.apps.spring.service.metrics"
    }
}
