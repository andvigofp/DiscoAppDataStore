package com.example.discoappdatastore.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface DiscoRepository {
    fun getAll(): Flow<List<Disco>>
    fun getDisco(id: Int): Flow<Disco?>
    suspend fun insert(disco: Disco)
    suspend fun update(disco: Disco)
    suspend fun delete(disco: Disco)
}

class DiscoRepositoryImpl(private val discoDataStore: DiscoDataStore) : DiscoRepository {
    override fun getAll(): Flow<List<Disco>> = discoDataStore.discos

    override fun getDisco(id: Int): Flow<Disco?> = discoDataStore.discos.map { discos ->
        discos.find { it.id == id }
    }

    override suspend fun insert(disco: Disco) = discoDataStore.addDisco(disco)
    override suspend fun update(disco: Disco) = discoDataStore.updateDisco(disco)
    override suspend fun delete(disco: Disco) = discoDataStore.deleteDisco(disco)
}
