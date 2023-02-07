package micro.apps.proto.util

import com.google.protobuf.FieldMask
import com.google.protobuf.util.FieldMaskUtil
import micro.apps.proto.common.v1.Person

// TODO: https://netflixtechblog.com/practical-api-design-at-netflix-part-1-using-protobuf-fieldmask-35cfdc606518

object PersonFieldMasks {
    /**
     * Can be used in [GetPersonRequest] to query
     * production title and format
     */
    val FIRST_AND_LAST_NAME_FIELD_MASK: FieldMask = FieldMaskUtil.fromFieldNumbers(
        Person::class.java,
        Person.FIRST_NAME_FIELD_NUMBER,
        Person.LAST_NAME_FIELD_NUMBER
    )

    /**
     * Can be used in [GetPersonRequest] to query
     * production title and schedule
     */
    val PHONE_AND_EMAIL_FIELD_MASK: FieldMask = FieldMaskUtil.fromFieldNumbers(
        Person::class.java,
        Person.PHONE_FIELD_NUMBER,
        Person.EMAIL_FIELD_NUMBER
    )
}
