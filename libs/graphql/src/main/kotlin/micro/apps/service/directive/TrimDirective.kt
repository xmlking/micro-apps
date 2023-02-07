package micro.apps.service.directive

import graphql.schema.DataFetchingEnvironment

class TrimDirective : StringFormatDirective() {
    override fun format(env: DataFetchingEnvironment, value: String): String {
        return value.trim()
    }
}
