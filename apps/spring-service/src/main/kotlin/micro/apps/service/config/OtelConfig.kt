package micro.apps.service.config

// import io.opentelemetry.semconv.resource.attributes.ResourceAttributes.SERVICE_NAME

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.metrics.GlobalMeterProvider
import io.opentelemetry.api.metrics.Meter
import io.opentelemetry.api.metrics.MeterProvider
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.TracerProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

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
// https://github.com/SquidDev-CC/eval.tweaked.cc/blob/dev/src/main/java/cc/tweaked/eval/telemetry/TelemetryConfiguration.java
// https://github.com/open-telemetry/opentelemetry-java/blob/main/examples/prometheus/src/main/java/io/opentelemetry/example/prometheus/ExampleConfiguration.java
//        val resource = Resource.getDefault().merge(
//            Resource.create(
//                Attributes.builder()
//                    .put(SERVICE_NAME, "spring-service")
//                    .build()
//            )
//        )
//
//        val meterProvider: SdkMeterProvider = SdkMeterProvider.builder().setResource(resource).buildAndRegisterGlobal()
//        PrometheusCollector.builder().setMetricProducer(meterProvider).buildAndRegister()

        val meterProvider: MeterProvider = GlobalMeterProvider.get()
        return meterProvider.meterBuilder(metricsName)
            // .setSchemaUrl("https://opentelemetry.io/schemas/1.5.0")
            .setInstrumentationVersion("1.0.0")
            .build()

        // return GlobalMeterProvider.get()[metricsName]
    }

    // https://github.com/helloworlde/netty-rpc/blob/master/opentelemetry/src/main/java/io/github/helloworlde/netty/rpc/opentelemetry/metrics/MetricsConfiguration.java
    // https://github.com/needle1989/OTEL-Instrumenting/blob/main/src/main/java/com/test/instrument/Metrics/ExampleConfiguration.java
    // https://github.com/gaohanghang/spring-boot-tips-tricks-and-techniques/blob/master/src/main/java/com/tomekl007/PaymentApplication.java
//    @Bean
//    fun otelMeter2(CollectorRegistry collectorRegistry): Meter {
//        val meterProvider = SdkMeterProvider.builder().buildAndRegisterGlobal()
//        val prometheusCollector = PrometheusCollector.builder().setMetricProducer(meterProvider).buildAndRegister()
//        // Prometheus Spring Boot Actuator
//        collectorRegistry.register(prometheusCollector)
//
//        return meterProvider.meterBuilder(metricsName)
//            // .setSchemaUrl("https://opentelemetry.io/schemas/1.5.0")
//            .setInstrumentationVersion("1.0.0")
//            .build()
//    }

//    @Bean
//    @ConditionalOnMissingBean
//    fun collectorRegistry(): CollectorRegistry? {
//        return CollectorRegistry(true)
//    }

//    @Bean
//    fun collectorRegistry(): CollectorRegistry {
//        return CollectorRegistry.defaultRegistry
//    }

    companion object {
        private const val tracerName = "micro.apps.spring.service.trace"
        private const val metricsName = "micro.apps.spring.service.metrics"
    }
}
