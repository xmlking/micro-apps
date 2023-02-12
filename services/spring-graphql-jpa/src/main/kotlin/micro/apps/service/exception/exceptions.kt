package micro.apps.service.exception

/**
 * Project specific Exceptions
 * See `:libs:graphql` for shared Exceptions
 */
class ItemBizException(message: String?, args: Array<Any>? = null) : GraphqlException(message, args)
