package micro.apps.service.domain.account

import io.quarkus.oidc.UserInfo
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
import micro.apps.service.util.Failure
import micro.apps.service.util.Success
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.eclipse.microprofile.openapi.annotations.tags.Tags
import java.lang.Exception
import javax.enterprise.inject.Default
import javax.inject.Inject
import javax.ws.rs.BadRequestException
import javax.ws.rs.GET
import javax.ws.rs.NotFoundException
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/v1/account")
@Produces(MediaType.APPLICATION_JSON)
@Tags(
    Tag(name = "Account", description = "The account resource")
)
class AccountResource(private val accountService: AccountService) {

    @Inject
    @field: Default
    lateinit var securityIdentity: SecurityIdentity

    @GET
    @Path("/id/{id}")
    fun getAccountById(@PathParam("id") id: String?): AccountDTO {
        if (id == null)
            throw MissingParameterException("No parameter 'Id' was given.")
        val result = accountService.findById(id)
        if (result is Failure<AccountDTO>) {
            throw NotFoundException(result.left)
        } else if (result is Success<AccountDTO>) {
            return result.right
        }
        return AccountDTO(firstName = "no", phone = "no")
    }

    @GET
    @Path("/user-info")
    @Produces(MediaType.TEXT_PLAIN)
    @SecurityRequirement(name = "oauth2", scopes = ["openid", "email", "profile"])
    // @SecurityRequirement(name = "bearerAuth", scopes = [])
    @Authenticated
    fun printUsername(): String? {
        return if (isDevoteamEmployee()) {
            val userInfo: UserInfo = securityIdentity.getAttribute("userinfo")
            val email: String = userInfo.getString("email")
            "Authenticated with email: $email"
        } else {
            "Authenticated. Your ID is: " + securityIdentity.principal.name
        }
    }

    private fun isDevoteamEmployee(): Boolean {
        return try {
            val userInfo: UserInfo = securityIdentity.getAttribute("userinfo")
            var devoteamEmployee = false
            if (userInfo != null) {
                val hd: String = userInfo.getString("hd")
                devoteamEmployee = hd == "devoteam.com"
            }
            devoteamEmployee
        } catch (e: Exception) {
            false
        }
    }
}

// Exceptions
class MissingParameterException(msg: String = "") : BadRequestException(msg)
