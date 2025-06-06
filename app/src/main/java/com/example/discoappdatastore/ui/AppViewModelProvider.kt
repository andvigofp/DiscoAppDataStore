package com.example.discoappdatastore.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.discoappdatastore.DiscosApplication
import com.example.discoappdatastore.ui.state.DiscoViewModel
import kotlin.jvm.java

class DiscoViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiscoViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

fun CreationExtras.discosApp(): DiscosApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as DiscosApplication)