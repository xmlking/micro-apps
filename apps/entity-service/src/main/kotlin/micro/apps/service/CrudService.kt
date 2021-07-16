package micro.apps.service

import kotlinx.coroutines.flow.Flow

interface CrudService<T, D> {
    suspend fun get(id: String): T
    suspend fun getAll(): Flow<T>
    suspend fun update(id: String, dto: D): T
    suspend fun update(obg: T): T
    suspend fun create(obg: D): T
    suspend fun delete(id: String)
}
