package micro.apps.greeting

import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.ApplicationPath
import javax.ws.rs.core.Application


@ApplicationScoped
@ApplicationPath("api")
class ApplicationConfig : Application()
