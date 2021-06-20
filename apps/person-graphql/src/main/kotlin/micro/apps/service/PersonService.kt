package micro.apps.service

import javax.enterprise.context.ApplicationScoped
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.transaction.Transactional

@ApplicationScoped
class PersonService {
    @PersistenceContext(name = "PersonDS")
    var em: EntityManager? = null
    fun getPerson(id: Long): Person {
        println("======= Getting person [$id] =======")
        return em!!.find(Person::class.java, id)
    }

    fun getPerson(person: Person): Person {
        println("======= Getting person [$person] =======")
        val thisPerson = em!!.find(Person::class.java, person)
        println("Error")
        return thisPerson
    }

    val people: List<Person>
        get() = em!!.createQuery("SELECT p FROM Person p", Person::class.java)
            .resultList as List<Person>

    fun getPeopleWithSurname(surname: String?): List<Person> {
        return em!!.createQuery("SELECT p FROM Person p WHERE p.surname=:surname", Person::class.java)
            .setParameter("surname", surname)
            .resultList as List<Person>
    }

    @Transactional
    fun updateOrCreate(person: Person): Person {
        return if (person.id == null) {
            em!!.persist(person)
            person
        } else {
            em!!.merge(person)
        }
    }

    @Transactional
    fun delete(id: Long?): Person? {
        val p = em!!.find(Person::class.java, id)
        if (p != null) {
            em!!.remove(p)
        }
        return p
    }
}
