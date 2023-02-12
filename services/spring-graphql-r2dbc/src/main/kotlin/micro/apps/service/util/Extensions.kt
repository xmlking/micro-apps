package micro.apps.service.util

import org.springframework.security.core.context.SecurityContext
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import java.security.Principal

fun SecurityContext.jwtAuthentication(): JwtAuthenticationToken {
    return authentication as JwtAuthenticationToken
}

fun Principal.jwtAuthentication(): JwtAuthenticationToken? {
    return this as? JwtAuthenticationToken
}

fun JwtAuthenticationToken.tokenValue(): String {
    return this.token.tokenValue
}
