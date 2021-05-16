package micro.apps.account

import io.grpc.Grpc
import io.grpc.Server
import io.grpc.ServerInterceptors
import io.grpc.TlsServerCredentials
// import io.grpc.alts.AltsServerCredentials
import io.grpc.health.v1.HealthCheckResponse.ServingStatus
import io.grpc.protobuf.services.ProtoReflectionService
import io.grpc.services.HealthStatusManager
// import io.grpc.xds.XdsServerCredentials
import micro.apps.account.config.TLS
import micro.apps.account.config.config
import micro.apps.μservice.interceptors.UnknownStatusInterceptor
import mu.KotlinLogging
import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.io.File
import java.security.Security
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

class AccountServer(private val port: Int) {

//    var creds = InsecureServerCredentials.create()

    // The xDS credentials use the security configured by the xDS server when available. When xDS
    // is not used or when xDS does not provide security configuration, the xDS credentials fall
    // back to other credentials (in this case, InsecureServerCredentials).
    // var creds = XdsServerCredentials.create(InsecureServerCredentials.create())

    var creds = TlsServerCredentials.newBuilder()
        .keyManager(File(config[TLS.upstreamCert]), File(config[TLS.upstreamKey]))
        .trustManager(File(config[TLS.upstreamCaCert]))
//        .keyManager(File(config[TLS.proxyCert]), File(config[TLS.proxyKey]))
//        .trustManager(File(config[TLS.caCert]))
//        .clientAuth(TlsServerCredentials.ClientAuth.REQUIRE)
        .build()

//    var creds = AltsServerCredentials.newBuilder()
//        .enableUntrustedAltsForTesting()
//        .setHandshakerAddressForTesting("localhost:" + server.port)
//        .build();

    val health: HealthStatusManager = HealthStatusManager()

    val server: Server = Grpc
        .newServerBuilderForPort(port, creds)
        .addService(AccountService())
        .addService(ProtoReflectionService.newInstance()) // convenient for command line tools
        .addService(health.healthService) // allow management servers to monitor health
        .addService(ServerInterceptors.intercept(AccountService(), UnknownStatusInterceptor()))
        .build()

    fun start() {
        server.start()
        logger.info { "Server started, listening on: $port" }
        Runtime.getRuntime().addShutdownHook(
            Thread {
                logger.atInfo().log("*** shutting down gRPC server since JVM is shutting down")
                this@AccountServer.stop()
                logger.atInfo().log("*** server shut down")
            }
        )
    }

    private fun stop() {
        health.setStatus("", ServingStatus.NOT_SERVING)
        // Start graceful shutdown
        logger.atInfo().log("Gracefully stopping... (press Ctrl+C again to force)")
        server.shutdown()
        // let proceed to kill if timeout
        try {
            // Wait for RPCs to complete processing
            if (!server.awaitTermination(30, TimeUnit.SECONDS)) {
                // That was plenty of time. Let's cancel the remaining RPCs
                server.shutdownNow()
                // shutdownNow isn't instantaneous, so give a bit of time to clean resources up
                // gracefully. Normally this will be well under a second.
                server.awaitTermination(5, TimeUnit.SECONDS)
            }
        } catch (ex: InterruptedException) {
            server.shutdownNow()
        }
    }

    fun blockUntilShutdown() {
        health.setStatus("", ServingStatus.SERVING)
        server.awaitTermination()
    }
}

fun main() {
    // Add BCP to avoid `
    // algid parse error, not a sequence` eror
    Security.addProvider(BouncyCastleProvider())

    // logger.atInfo().withCause(exception).log("Log message with: %s", argument);
    logger.atDebug()
        .addKeyValue("upstreamCert", config[TLS.upstreamCert])
        .addKeyValue("upstreamKey", config[TLS.upstreamKey])
        .addKeyValue("upstreamCaCert", config[TLS.upstreamCaCert])
        .log("Config:")

    val port = System.getenv("PORT")?.toInt() ?: 5000
    val server = AccountServer(port)
    server.start()
    server.blockUntilShutdown()
}
