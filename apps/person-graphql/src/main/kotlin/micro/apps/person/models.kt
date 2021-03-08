package micro.apps.person

import javax.json.bind.annotation.JsonbTransient
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OrderColumn

enum class Action {
    Steps, Gym, Drive, Save, CheckUp
}

@Entity
class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbTransient
    var id: Int? = null

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String::class)
    @OrderColumn
    var lines: List<String>? = null
    var code: String? = null
}

class Age : Measurable {
    override var value: Long? = null
}

@Entity
class CreditCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbTransient
    var id: Int? = null
    var expiry: String? = null
    var number: String? = null
    var type: String? = null
}

enum class Gender {
    Male, Female
}

@Entity
class ImClient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbTransient
    var id: Int? = null
    var im: String? = null
    var identifier: String? = null
}

interface Measurable {
    val value: Long?
}

@Entity
class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbTransient
    var id: Int? = null
    var type: String? = null
    var number: String? = null
}

@Entity
class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbTransient
    var id: Int? = null
    var relationType: RelationType? = null
    var personURI: String? = null
}

enum class RelationType {
    Spouse, Husband, Wife, Parent, Father, Mother, StepFather, StepMother, LegalGuardian, Child, Son, Daughter, StepSon, StepDaughter, Sibling, Brother, Sister, Stepfamily, Grandparent, Grandfather, Grandmother, Grandson, Granddaughter, Uncle, Aunt, Cousin, Nephew, Niece, FamilyInLaw, FatherInLaw, MotherInLaw, BrotherInLaw, SisterInLaw
}

class Score : Measurable {
    var id: String? = null
    var name: ScoreType? = null
    override var value: Long? = null
}

enum class ScoreType {
    Driving, Fitness, Activity, Financial
}

@Entity
class SocialMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonbTransient
    var id: Int? = null
    var name: String? = null
    var username: String? = null
}

class Weight : Measurable {
    override var value: Long? = null
}
