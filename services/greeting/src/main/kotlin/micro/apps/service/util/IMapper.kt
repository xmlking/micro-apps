package micro.apps.service.util

interface IMapper<D, E> {
    fun toEntity(dto: D): E
    fun toDTO(entity: E): D
//    fun toEntities(dtoList: List<D>?): List<E>?
//    fun toDTOs(entityList: List<E>?): List<D>?
}

fun <D, E> IMapper<D, E>.toEntities(dtoList: List<D>): List<E> {
    val result = mutableListOf<E>()
    for (dto in dtoList) {
        result.add(toEntity(dto))
    }
    return result
}

fun <D, E> IMapper<D, E>.toDTOs(entityList: List<E>): List<D> {
    val result = mutableListOf<D>()
    for (entity in entityList) {
        result.add(toDTO(entity))
    }
    return result
}
