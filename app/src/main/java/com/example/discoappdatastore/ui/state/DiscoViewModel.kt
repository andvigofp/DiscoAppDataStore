package com.example.discoappdatastore.ui.state

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.discoappdatastore.DiscosApplication
import com.example.discoappdatastore.data.Disco
import com.example.discoappdatastore.data.startingDiscos
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class FiltroDisco {
    TITULO, AUTOR, NUM_CANCIONES, PUBLICACION
}

class DiscoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as DiscosApplication).appContainer.provideDiscoRepository()

    var showSuccessDialog = MutableStateFlow(false)
    var showErrorDialog = MutableStateFlow(false)
    var showExistsDialog = MutableStateFlow(false)
    var error = MutableStateFlow(false)

    private val _filtroActual = MutableStateFlow(FiltroDisco.TITULO)
    val filtroActual: StateFlow<FiltroDisco> = _filtroActual.asStateFlow()

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    //Para buscar por filtro
    val discos: StateFlow<List<Disco>> = combine(
        repository.getAll(),
        _filtroActual,
        _busqueda
    ) { listaDiscos, filtro, textoBusqueda ->
        val textoBusquedaLower = textoBusqueda.lowercase()
        Log.d("DiscoViewModel", "Filtro: $filtro, Busqueda: '$textoBusquedaLower', Discos antes: ${listaDiscos.size}")

        val listaFiltrada = when (filtro) {
            FiltroDisco.TITULO -> {
                if (textoBusquedaLower.isEmpty()) {
                    listaDiscos.sortedBy { it.titulo }
                } else {
                    listaDiscos.filter { 
                        it.titulo.lowercase().contains(textoBusquedaLower) 
                    }.sortedBy { it.titulo }
                }
            }
            FiltroDisco.AUTOR -> {
                if (textoBusquedaLower.isEmpty()) {
                    listaDiscos.sortedBy { it.autor }
                } else {
                    listaDiscos.filter { 
                        it.autor.lowercase().contains(textoBusquedaLower) 
                    }.sortedBy { it.autor }
                }
            }
            FiltroDisco.NUM_CANCIONES -> {
                val numBusqueda = textoBusquedaLower.toIntOrNull()
                Log.d("DiscoViewModel", "  NUM_CANCIONES -> numBusqueda: $numBusqueda")
                if (numBusqueda != null) {
                    listaDiscos.filter { it.numCanciones == numBusqueda }
                        .sortedBy { it.numCanciones }
                } else {
                    // Si el texto de búsqueda no es un número o está vacío, solo ordenar
                    listaDiscos.sortedBy { it.numCanciones }
                }
            }
            FiltroDisco.PUBLICACION -> {
                val anioBusqueda = textoBusquedaLower.toIntOrNull()
                Log.d("DiscoViewModel", "  PUBLICACION -> anioBusqueda: $anioBusqueda")
                if (anioBusqueda != null) {
                    val filteredList = listaDiscos.filter { it.publicacion == anioBusqueda }
                    Log.d("DiscoViewModel", "    PUBLICACION -> Discos filtrados por año ($anioBusqueda): ${filteredList.size}")
                    filteredList.sortedBy { it.publicacion }
                } else {
                    // Si el texto de búsqueda no es un número o está vacío, solo ordenar
                    Log.d("DiscoViewModel", "    PUBLICACION -> Texto no numérico o vacío, solo ordenando.")
                    listaDiscos.sortedBy { it.publicacion }
                }
            }
        }
         Log.d("DiscoViewModel", "Discos después de filtrar/ordenar: ${listaFiltrada.size}")
        listaFiltrada
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setFiltro(filtro: FiltroDisco) {
        _filtroActual.value = filtro
         Log.d("DiscoViewModel", "Filtro cambiado a: $filtro")
    }

    fun setBusqueda(texto: String) {
        _busqueda.value = texto
         Log.d("DiscoViewModel", "Texto de búsqueda cambiado a: '$texto'")
    }

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