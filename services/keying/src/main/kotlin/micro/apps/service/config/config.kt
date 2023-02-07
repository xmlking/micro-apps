package micro.apps.service.config

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
    }
        .from.yaml.resource("config.yaml")
        // .from.yaml.resource("config.prod.yaml", true)
        .from.env()
        .from.systemProperties()
}

object TLS : ConfigSpec("certs") {
    val caCert: OptionalItem<String> by optional("certs/ca-cert.pem")

    val clientKey by optional("certs/client-key.pem")
    val clientCert by optional("certs/client-cert.pem")

    val proxyKey by optional("certs/proxy-key.pem")
    val proxyCert by optional("certs/proxy-cert.pem")

    val upstreamKey by optional("certs/upstream-key.pem")
    val upstreamCert by optional("certs/upstream-cert.pem")
    val upstreamCaCert by optional("certs/upstream-ca-cert.pem")
}

object Account : ConfigSpec("account") {
    val endpoint by required<String>()
    val authority by optional("www.sumo.com")
    val maxRetry by optional<Int>(3)
    val flowControlWindow by optional<Int>(65 * 1024)
    val deadline by optional("1s") // in the format of ['-']sssss[.nnnn]'s'
}

object Endpoints : ConfigSpec("endpoints") {
    val account by optional("http://localhost:8080", description = "endpoint of account service")
    val echo by required<String>(description = "endpoint of echo service")
}
