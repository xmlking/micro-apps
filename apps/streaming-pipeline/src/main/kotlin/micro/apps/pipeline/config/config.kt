package micro.apps.pipeline.config

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import com.uchuhimo.konf.OptionalItem
import com.uchuhimo.konf.source.yaml

// https://github.com/uchuhimo/konf

val config by lazy {
    Config {
        addSpec(TLS)
        addSpec(Account)
        addSpec(Endpoints)
        addSpec(Cloud)
    }
        .from.yaml.resource("config.yaml")
        .from.yaml.resource("config.prod.yaml", true)
        .from.env()
        .from.systemProperties()
}

object TLS : ConfigSpec("certs") {
    val serverKey by optional("certs/server-key.pem")
    val serverCert by optional("certs/server-cert.pem")

    val clientKey by optional("certs/client-key.pem")
    val clientCert by optional("certs/client-cert.pem")
    val clientPasscode by optional("fake_one")

    val caCert: OptionalItem<String> by optional("certs/ca-cert.pem")

    val upstreamKey by optional("certs/upstream-key.pem")
    val upstreamCert by optional("certs/upstream-cert.pem")
    val upstreamCaCert by optional("certs/upstream-ca-cert.pem")
}

object Account : ConfigSpec("account") {
    val endpoint by optional("http://localhost:8080")
    val authority by required<String>()
    val maxRetry by optional<Int>(3)
    val flowControlWindow by optional<Int>(65 * 1024)
    val deadline by optional("1s") // in the format of ['-']sssss[.nnnn]'s'
}

object Endpoints : ConfigSpec("endpoints") {
    val account by optional("http://localhost:8080", description = "endpoint of account service")
    val echo by required<String>(description = "endpoint of echo service")
}

object Cloud : ConfigSpec() {
    object Dataflow : ConfigSpec() {
        val windowDuration by optional("300s")
    }
}
