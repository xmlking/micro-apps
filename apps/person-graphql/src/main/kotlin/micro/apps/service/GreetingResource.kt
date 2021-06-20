package micro.apps.service

import org.eclipse.microprofile.graphql.Description
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class GreetingResource {
    @Query
    fun hello(): String {
        return "hello"
    }

    @Query
    @Description("find all products data")
    fun getProducts(): List<String?>? {
        // you can simulate backend calls here to return the data graphql will fire only the required fields query and can be bounded to the schema.
        // @Mutation used for mutating data (create,update and delete operations).
        return java.util.List.of<String>("test", "test1", "test2")
    }
}
