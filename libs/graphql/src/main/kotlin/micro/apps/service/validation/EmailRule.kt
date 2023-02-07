package micro.apps.service.validation

import graphql.GraphQLError
import graphql.Scalars
import graphql.schema.GraphQLInputType
import graphql.validation.constraints.AbstractDirectiveConstraint
import graphql.validation.constraints.Documentation
import graphql.validation.rules.ValidationEnvironment
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator

class EmailRule : AbstractDirectiveConstraint("Email") {
    override fun getDocumentation(): Documentation {
        return Documentation.newDocumentation()
            .messageTemplate(messageTemplate)
            .description("The String must be a well-formed email address.")
            .example("updateAccident( email : String @Email) : DriverDetails")
            .applicableTypeNames(Scalars.GraphQLString.name, Scalars.GraphQLID.name, "Lists")
            .directiveSDL(
                "directive @Email(message : String = \"%s\") " +
                    "on ARGUMENT_DEFINITION | INPUT_FIELD_DEFINITION",
                messageTemplate
            )
            .build()
    }

    public override fun appliesToType(inputType: GraphQLInputType): Boolean {
        return isStringOrIDOrList(inputType)
    }

    override fun runConstraint(validationEnvironment: ValidationEnvironment): List<GraphQLError> {
        val validatedValue = validationEnvironment.validatedValue
        return if (!EmailValidator().isValid(validatedValue.toString(), null)) {
            mkError(validationEnvironment)
        } else emptyList()
    }

    override fun appliesToListElements(): Boolean {
        return true
    }
}
