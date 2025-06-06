package com.example.discoappdatastore

import android.app.Application
import com.example.discoappdatastore.data.AppContainer

class DiscosApplication: Application() {
    val appContainer: AppContainer by lazy { AppContainer(applicationContext) }

    override fun onCreate() {
        super.onCreate()
    }
}