package micro.apps.service.config

object Authorities {
    const val SCOPE_ACTUATOR = "SCOPE_ACTUATOR"
    const val SCOPE_GRAPHQL = "SCOPE_GRAPHQL"
    const val SCOPE_GRAPHIQL = "SCOPE_GRAPHIQL"
    const val SCOPE_H2 = "SCOPE_H2"

    const val READ_AUTHORITY = "READ_AUTHORITY"
    const val WRITE_AUTHORITY = "WRITE_AUTHORITY"
    const val UPDATE_AUTHORITY = "UPDATE_AUTHORITY"
    const val DELETE_AUTHORITY = "DELETE_AUTHORITY"
}

object Roles {
    const val ROLE_VIEWER = "SCOPE_viewer"
    const val ROLE_EDITOR = "SCOPE_editor"
    const val ROLE_MODERATOR = "SCOPE_moderator"
    const val ROLE_SUPERVISOR = "SCOPE_supervisor"
}
