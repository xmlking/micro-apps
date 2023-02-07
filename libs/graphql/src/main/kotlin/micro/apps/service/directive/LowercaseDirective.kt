package micro.apps.service.directive

import graphql.schema.DataFetchingEnvironment

class LowercaseDirective : StringFormatDirective() {
    override fun format(env: DataFetchingEnvironment, value: String): String {
        return value.lowercase(env.locale)
    }
}
