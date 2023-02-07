package micro.apps.service.directive

import graphql.schema.DataFetchingEnvironment

class UppercaseDirective : StringFormatDirective() {
    override fun format(env: DataFetchingEnvironment, value: String): String {
        return value.uppercase(env.locale)
    }
}
