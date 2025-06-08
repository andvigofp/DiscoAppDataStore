package com.example.discoappdatastore.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.discoappdatastore.data.Disco
import com.example.discoappdatastore.ui.state.DiscoViewModel
import com.example.discoappdatastore.ui.state.FiltroDisco
import kotlin.collections.average
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.let
import kotlin.text.format

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: DiscoViewModel,
    onDiscoClick: (Int) -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (Int) -> Unit
) {
    val discos by viewModel.discos.collectAsStateWithLifecycle()
    val filtroActual by viewModel.filtroActual.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()

    // Estado para el diálogo de borrado
    var discoAEliminar by remember { mutableStateOf<Disco?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                ListaDiscosTopAppBar(
                    title = "DiscosApp Andres",
                    canNavigateBack = false
                )
                // Barra de búsqueda
                OutlinedTextField(
                    value = busqueda,
                    onValueChange = { viewModel.setBusqueda(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    placeholder = { Text("Buscar...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                    trailingIcon = {
                        if (busqueda.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setBusqueda("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda")
                            }
                        }
                    },
                    singleLine = true
                )
                // Selector de filtro
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FiltroDisco.values().forEach { filtro ->
                        FilterChip(
                            selected = filtroActual == filtro,
                            onClick = { viewModel.setFiltro(filtro) },
                            label = {
                                Text(
                                    when (filtro) {
                                        FiltroDisco.TITULO -> "Título"
                                        FiltroDisco.AUTOR -> "Autor"
                                        FiltroDisco.NUM_CANCIONES -> "Canciones"
                                        FiltroDisco.PUBLICACION -> "Año"
                                    }
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Añadir disco")
            }
        },
        bottomBar = {
            BottomAppBar {
                val media = if (discos.isNotEmpty()) discos.map { it.valoracion }.average() else 0.0
                Text(
                    if (discos.isNotEmpty())
                        "Valoración media: %.2f".format(media)
                    else
                        "Todavía no hay discos añadidos",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) { padding ->
        if (discos.isEmpty() && busqueda.isEmpty() && filtroActual == FiltroDisco.TITULO) {
            // Pantalla vacía solo si no hay búsqueda activa ni filtro diferente del por defecto
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(64.dp))
                    Text("No hay discos añadidos todavía")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.insertarDiscosPorDefecto() }) {
                        Text("Insertar discos de prueba")
                    }
                }
            }
        } else if (discos.isEmpty() && (busqueda.isNotEmpty() || filtroActual != FiltroDisco.TITULO)) {
             // Mensaje si no se encuentran resultados con el filtro/búsqueda actual
             Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontraron resultados para la búsqueda y filtro actuales.")
             }
        }
        else {
            // Lista de discos
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(discos) { disco ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clickable { onDiscoClick(disco.id) },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(disco.titulo, style = MaterialTheme.typography.titleMedium)
                                Text(disco.autor, style = MaterialTheme.typography.bodyMedium)
                                Text("Canciones: ${disco.numCanciones}", style = MaterialTheme.typography.bodySmall)
                                Text("Año: ${disco.publicacion}", style = MaterialTheme.typography.bodySmall)
                            }
                            // Botón de editar
                            IconButton(onClick = { onEditClick(disco.id) }) {
                                Icon(Icons.Filled.Edit, contentDescription = "Editar disco")
                            }
                            // Valoración en estrellas
                            Row {
                                repeat(disco.valoracion) {
                                    Text("⭐")
                                }
                            }
                            // Botón de borrar
                            IconButton(onClick = {
                                discoAEliminar = disco
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar disco")
                            }
                        }
                    }
                }
            }
        }

        // Diálogo de confirmación de borrado
        ConfirmDeleteDialog(
            show = showDialog,
            discoTitulo = discoAEliminar?.titulo ?: "",
            onConfirm = {
                discoAEliminar?.let { viewModel.deleteDisco(it) }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
}