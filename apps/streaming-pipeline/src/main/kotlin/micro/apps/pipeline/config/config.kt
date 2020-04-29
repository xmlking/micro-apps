package micro.apps.pipeline.config

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.ConfigSpec
import com.uchuhimo.konf.OptionalItem
import com.uchuhimo.konf.source.yaml

// https://github.com/uchuhimo/konf

val config by lazy {
    Config {
        addSpec(TlsConfig)
        addSpec(EndpointConfig)
    }
        .from.yaml.resource("config.yaml")
        .from.yaml.resource("config.dev.yaml", true)
        .from.env()
        .from.systemProperties()
}

object TlsConfig : ConfigSpec("certs") {
    val serverKey by optional("certs/server-key.pem")
    val serverCert by optional("certs/server-cert.pem")

    val clientKey by optional("certs/client-key.pem")
    val clientCert by optional("certs/client-cert.pem")

    val caCert: OptionalItem<String> by optional("certs/ca-cert.pem")

    val upstreamKey by optional("certs/upstream-key.pem")
    val upstreamCert by optional("certs/upstream-cert.pem")
    val upstreamCaCert by optional("certs/upstream-ca-cert.pem")
}

object EndpointConfig : ConfigSpec("endpoints") {
    val account by optional("http://localhost:8080")
    val echo by optional("http://localhost:8081", description = "endpoints of echo")
}

object CloudConfig : ConfigSpec() {
    object DataflowConfig : ConfigSpec() {
        val user by optional("admin")
    }
}
