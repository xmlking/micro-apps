package micro.apps.service.directive

import graphql.schema.DataFetchingEnvironment
import micro.apps.core.capitalize

class CapitalizeDirective : StringFormatDirective() {
    override fun format(env: DataFetchingEnvironment, value: String): String {
        return value.capitalize(env.locale)
    }
}
