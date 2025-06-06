package com.example.discoappdatastore.data

import android.content.Context

class AppContainer(context: Context) {
    private val discoDataStore: DiscoDataStore
    private val discoRepository: DiscoRepository

    init {
        discoDataStore = DiscoDataStore(context)
        discoRepository = DiscoRepositoryImpl(discoDataStore)
    }

    fun provideDiscoRepository(): DiscoRepository = discoRepository
}