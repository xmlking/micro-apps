package micro.apps.greeting.util

interface IMapper<D, E> {
    fun toEntity(dto: D): E
    fun toDto(entity: E): D
//    fun toEntity(dtoList: List<D>?): List<E>?
//    fun toDto(entityList: List<E>?): List<D>?
}

fun <D, E> IMapper<D, E>.toEntity(dtoList: List<D>): List<E> {
    val result = mutableListOf<E>()
    for (dto in dtoList) {
        result.add(toEntity(dto))
    }
    return result
}

fun <D, E> IMapper<D, E>.toDto(entityList: List<E>): List<D> {
    val result = mutableListOf<D>()
    for (entity in entityList) {
        result.add(toDto(entity))
    }
    return result
}
