package micro.apps.service.config

import io.opentelemetry.api.GlobalOpenTelemetry
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.metrics.Meter
import io.opentelemetry.api.metrics.MeterProvider
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.TracerProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

// import io.opentelemetry.exporter.prometheus.PrometheusCollector
// import io.opentelemetry.exporter.prometheus.PrometheusHttpServer
// import io.opentelemetry.sdk.metrics.SdkMeterProvider
// import io.prometheus.client.CollectorRegistry

@Configuration
class OtelConfig {

    @Bean
    fun openTelemetry(): OpenTelemetry {
        return GlobalOpenTelemetry.get()
    }

    @Bean
    fun otelTracer(): Tracer {
        val tracerProvider: TracerProvider = GlobalOpenTelemetry.getTracerProvider()
        return tracerProvider.tracerBuilder(tracerName)
            .setInstrumentationVersion("0.5")
            .build()
    }

    @Bean
    fun otelMeter(): Meter {
        val meterProvider: MeterProvider = GlobalOpenTelemetry.getMeterProvider()
        return meterProvider.meterBuilder(metricsName)
            // .setSchemaUrl("https://opentelemetry.io/schemas/1.5.0")
            .setInstrumentationVersion("1.0.0")
            .build()
    }

//    @Bean
//    fun getMeterProvider(): MeterProvider {
//        val prometheusReaderFactory: MetricReaderFactory =
//            PrometheusHttpServer.builder().setPort(9091).newMetricReaderFactory()
//        return SdkMeterProvider.builder().registerMetricReader(prometheusReaderFactory).build()
//    }
//    @Bean
//    @Primary
//    fun otelMeterSpring(collectorRegistry: CollectorRegistry): Meter {
//        val meterProvider = SdkMeterProvider.builder().buildAndRegisterGlobal()
//        val prometheusCollector = PrometheusCollector.builder().setMetricProducer(meterProvider).buildAndRegister()
//        collectorRegistry.register(prometheusCollector) // register Prometheus with Spring Boot Actuator
//
//        return meterProvider.meterBuilder(metricsName)
//            // .setSchemaUrl("https://opentelemetry.io/schemas/1.5.0")
//            .setInstrumentationVersion("1.0.0")
//            .build()
//    }

//    @Bean
//    fun getOpenTelemetry(): OpenTelemetrySdk {
//        val serviceNameResource: Resource =
//            Resource.create(Attributes.of(ResourceAttributes.SERVICE_NAME, "print-service"))
//        val openTelemetrySdkBuilder = OpenTelemetrySdk.builder()
//            .setTracerProvider(
//                SdkTracerProvider.builder()
//                    .addSpanProcessor(SimpleSpanProcessor.create(getJaegerExporter()))
//                    .addSpanProcessor(SimpleSpanProcessor.create(getZipkinSpanExporter()))
//                    .addSpanProcessor(SimpleSpanProcessor.create(LoggingSpanExporter.create()))
//                    .setResource(Resource.getDefault().merge(serviceNameResource))
//                    .build()
//            )
//            .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
//        return openTelemetrySdkBuilder.buildAndRegisterGlobal()
//    }

//    @Bean
//    @ConditionalOnProperty(prefix = "graphql.tracing", name = ["enabled"], matchIfMissing = true)
//    open fun tracingInstrumentation(openTelemetry: OpenTelemetry): Instrumentation {
//        return GraphQLTelemetry.builder(openTelemetry).build().newInstrumentation()
//    }
    companion object {
        private const val tracerName = "micro.apps.spring.service.trace"
        private const val metricsName = "micro.apps.spring.service.metrics"
    }
}
