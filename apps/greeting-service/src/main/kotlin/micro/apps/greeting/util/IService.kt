package micro.apps.greeting.util

sealed class Either<A, B> {
    class Left<A, B>(val left: A) : Either<A, B>()
    class Right<A, B>(val right: B) : Either<A, B>()
}

typealias Result<V> = Either<Exception, V>
typealias Failure<V> = Either.Left<Exception, V>
typealias Success<V> = Either.Right<Exception, V>

interface IService<D> {
    fun findById(id: String): Result<D>
    fun findAll(index: Int, size: Int): List<D>
    fun save(dto: D): Result<D>
    fun delete(dto: D): Exception?
}

abstract class AbstractService<D, E> : IService<D> {

    abstract val mapper: IMapper<D, E>
/*
    abstract val repository: PanacheMongoRepository<E>

    override fun save(dto: U): Either<U, Exception> {
        val word = mapper.toEntity(dto)
        return try {
            repository.persist(word)
            Either.left(mapper.toDto(word))
        } catch (e: Exception) {
            Either.right("There was an error while saving")
        }
    }

    override fun delete(dto: U): Exception? {
        val model = mapper.toEntity(dto)
        val original = repository.findByIdOptional(model.id)
        if (original.isEmpty) return NotFoundException("No word with the id ${model.id} was found")

        return try {
            repository.delete(model)
            null
        } catch (e: Exception) {
            BadRequestException("There was an error while deleting - please ask the Owner of the library")
        }
    }

    override fun findAll(index: Int, size: Int): List<U> {
        val page = Page.of(index, size)
        return repository.findAll()
            .page<T>(page)
            .list<T>()
            .map(mapper::convertModelToDTO)
    }

    override fun findById(id: String): Either<U, Exception> {
        val optional = repository
            .findByIdOptional(ObjectId(id))
        return if (optional.isPresent)
            Either.left(optional.get().let { mapper.toDto(it) })
        else
            Either.right("No Word with id $id was found")
    }
 */
}
