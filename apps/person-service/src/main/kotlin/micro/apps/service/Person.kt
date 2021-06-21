package micro.apps.service

import java.io.Serializable
import java.net.URL
import java.time.LocalDate
import java.util.LinkedList
import javax.json.bind.annotation.JsonbDateFormat
import javax.persistence.CascadeType
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Person : Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var locale: String? = null
    var title: String? = null

    @ElementCollection(fetch = FetchType.EAGER, targetClass = String::class)
    private var names: MutableList<String>? = null

    @ElementCollection(targetClass = String::class)
    private var nicknames: MutableList<String>? = null
    var surname: String? = null
    var username: String? = null
    var idNumber: String? = null

    @ElementCollection(targetClass = URL::class)
    private var coverphotos: MutableList<URL>? = null

    @ElementCollection(targetClass = URL::class)
    private var profilePictures: MutableList<URL>? = null
    var gender: Gender? = null

    @JsonbDateFormat("dd/MM/yyyy")
    var birthDate: LocalDate? = null
    var favColor: String? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private var addresses: MutableList<Address>? = null

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String::class)
    private var emailAddresses: MutableList<String>? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private var phoneNumbers: MutableList<Phone>? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private var imClients: MutableList<ImClient>? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private var socialMedias: MutableList<SocialMedia>? = null
    var website: URL? = null

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String::class)
    private var taglines: MutableList<String>? = null
    var biography: String? = null
    var organization: String? = null
    var occupation: String? = null

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String::class)
    private var interests: MutableList<String>? = null

    @ElementCollection(fetch = FetchType.LAZY, targetClass = String::class)
    private var skills: MutableList<String>? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private var relations: MutableList<Relation>? = null

    @JsonbDateFormat("dd/MM/yyyy")
    var joinDate: LocalDate? = null
    var maritalStatus: String? = null

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    private var creditCards: MutableList<CreditCard>? = null
    var userAgent: String? = null
    fun addName(name: String) {
        if (names == null) names = LinkedList()
        names!!.add(name)
    }

    fun addNickName(nickname: String) {
        if (nicknames == null) nicknames = LinkedList()
        nicknames!!.add(nickname)
    }

    fun addCoverPhoto(coverphoto: URL) {
        if (coverphotos == null) coverphotos = LinkedList()
        coverphotos!!.add(coverphoto)
    }

    fun addProfilePicture(profilePicture: URL) {
        if (profilePictures == null) profilePictures = LinkedList()
        profilePictures!!.add(profilePicture)
    }

    fun addAddress(address: Address) {
        if (addresses == null) addresses = LinkedList()
        addresses!!.add(address)
    }

    fun addEmailAddress(emailAddress: String) {
        if (emailAddresses == null) emailAddresses = LinkedList()
        emailAddresses!!.add(emailAddress)
    }

    fun addPhoneNumber(phoneNumber: Phone) {
        if (phoneNumbers == null) phoneNumbers = LinkedList()
        phoneNumbers!!.add(phoneNumber)
    }

    fun addImClient(imClient: ImClient) {
        if (imClients == null) imClients = LinkedList()
        imClients!!.add(imClient)
    }

    fun addSocialMedia(socialMedia: SocialMedia) {
        if (socialMedias == null) socialMedias = LinkedList()
        socialMedias!!.add(socialMedia)
    }

    fun addTagline(tagline: String) {
        if (taglines == null) taglines = LinkedList()
        taglines!!.add(tagline)
    }

    fun addInterest(interest: String) {
        if (interests == null) interests = LinkedList()
        interests!!.add(interest)
    }

    fun addSkill(skill: String) {
        if (skills == null) skills = LinkedList()
        skills!!.add(skill)
    }

    fun addRelationship(relation: Relation) {
        if (relations == null) relations = LinkedList()
        relations!!.add(relation)
    }

    fun addCreditCard(card: CreditCard) {
        if (creditCards == null) creditCards = LinkedList()
        creditCards!!.add(card)
    }

    fun getNames(): List<String>? {
        return names
    }

    fun setNames(names: MutableList<String>?) {
        this.names = names
    }

    fun getNicknames(): List<String>? {
        return nicknames
    }

    fun setNicknames(nicknames: MutableList<String>?) {
        this.nicknames = nicknames
    }

    fun getCoverphotos(): List<URL>? {
        return coverphotos
    }

    fun setCoverphotos(coverphotos: MutableList<URL>?) {
        this.coverphotos = coverphotos
    }

    fun getProfilePictures(): List<URL>? {
        return profilePictures
    }

    fun setProfilePictures(profilePictures: MutableList<URL>?) {
        this.profilePictures = profilePictures
    }

    fun getAddresses(): List<Address>? {
        return addresses
    }

    fun setAddresses(addresses: MutableList<Address>?) {
        this.addresses = addresses
    }

    fun getEmailAddresses(): List<String>? {
        return emailAddresses
    }

    fun setEmailAddresses(emailAddresses: MutableList<String>?) {
        this.emailAddresses = emailAddresses
    }

    fun getPhoneNumbers(): List<Phone>? {
        return phoneNumbers
    }

    fun setPhoneNumbers(phoneNumbers: MutableList<Phone>?) {
        this.phoneNumbers = phoneNumbers
    }

    fun getImClients(): List<ImClient>? {
        return imClients
    }

    fun setImClients(imClients: MutableList<ImClient>?) {
        this.imClients = imClients
    }

    fun getSocialMedias(): List<SocialMedia>? {
        return socialMedias
    }

    fun setSocialMedias(socialMedias: MutableList<SocialMedia>?) {
        this.socialMedias = socialMedias
    }

    fun getTaglines(): List<String>? {
        return taglines
    }

    fun setTaglines(taglines: MutableList<String>?) {
        this.taglines = taglines
    }

    fun getInterests(): List<String>? {
        return interests
    }

    fun setInterests(interests: MutableList<String>?) {
        this.interests = interests
    }

    fun getSkills(): List<String>? {
        return skills
    }

    fun setSkills(skills: MutableList<String>?) {
        this.skills = skills
    }

    fun getRelations(): List<Relation>? {
        return relations
    }

    fun setRelations(relations: MutableList<Relation>?) {
        this.relations = relations
    }

    fun getCreditCards(): List<CreditCard>? {
        return creditCards
    }

    fun setCreditCards(creditCards: MutableList<CreditCard>?) {
        this.creditCards = creditCards
    }
}
