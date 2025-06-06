package com.example.discoappdatastore.ui.state

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.discoappdatastore.DiscosApplication
import com.example.discoappdatastore.data.Disco
import com.example.discoappdatastore.data.startingDiscos
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DiscoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as DiscosApplication).appContainer.provideDiscoRepository()

    var showSuccessDialog = MutableStateFlow(false)
    var showErrorDialog = MutableStateFlow(false)
    var showExistsDialog = MutableStateFlow(false)
    var error = MutableStateFlow(false)

    val discos: Flow<List<Disco>> = repository.getAll()

    fun addDisco(disco: Disco) {
        viewModelScope.launch {
            repository.insert(disco)
        }
    }

    fun deleteDisco(disco: Disco) {
        viewModelScope.launch {
            repository.delete(disco)
        }
    }

    fun updateDisco(disco: Disco) {
        viewModelScope.launch {
            repository.update(disco)
        }
    }

    fun setShowSuccessDialog(show: Boolean) {
        showSuccessDialog.value = show
    }

    fun setShowErrorDialog(show: Boolean) {
        showErrorDialog.value = show
    }

    fun setShowExistsDialog(show: Boolean) {
        showExistsDialog.value = show
    }

    fun setError(show: Boolean) {
        error.value = show
    }

    fun insertarDiscosPorDefecto() {
        viewModelScope.launch {
            startingDiscos.forEach { disco ->
                repository.insert(disco)
            }
        }
    }
}