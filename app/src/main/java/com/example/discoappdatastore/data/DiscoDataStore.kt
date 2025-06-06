package com.example.discoappdatastore.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "discos")

class DiscoDataStore(private val context: Context) {
    companion object {
        private val DISCOS_KEY = stringPreferencesKey("discos")
        private val json = Json { ignoreUnknownKeys = true }
    }

    val discos: Flow<List<Disco>> = context.dataStore.data.map { preferences ->
        val discosJson = preferences[DISCOS_KEY] ?: "[]"
        try {
            json.decodeFromString<List<Disco>>(discosJson)
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun saveDiscos(discos: List<Disco>) {
        context.dataStore.edit { preferences ->
            preferences[DISCOS_KEY] = json.encodeToString(discos)
        }
    }

    suspend fun addDisco(disco: Disco) {
        context.dataStore.edit { preferences ->
            val currentDiscos = try {
                json.decodeFromString<List<Disco>>(preferences[DISCOS_KEY] ?: "[]")
            } catch (e: Exception) {
                emptyList()
            }
            val newDiscos = currentDiscos + disco
            preferences[DISCOS_KEY] = json.encodeToString(newDiscos)
        }
    }

    suspend fun updateDisco(disco: Disco) {
        context.dataStore.edit { preferences ->
            val currentDiscos = try {
                json.decodeFromString<List<Disco>>(preferences[DISCOS_KEY] ?: "[]")
            } catch (e: Exception) {
                emptyList()
            }
            val newDiscos = currentDiscos.map { 
                if (it.id == disco.id) disco else it 
            }
            preferences[DISCOS_KEY] = json.encodeToString(newDiscos)
        }
    }

    suspend fun deleteDisco(disco: Disco) {
        context.dataStore.edit { preferences ->
            val currentDiscos = try {
                json.decodeFromString<List<Disco>>(preferences[DISCOS_KEY] ?: "[]")
            } catch (e: Exception) {
                emptyList()
            }
            val newDiscos = currentDiscos.filter { it.id != disco.id }
            preferences[DISCOS_KEY] = json.encodeToString(newDiscos)
        }
    }
} 