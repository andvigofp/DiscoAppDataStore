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
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Delete
import com.example.discoappdatastore.data.Disco
import com.example.discoappdatastore.ui.state.DiscoViewModel
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
    onAddClick: () -> Unit
) {
    val discos by viewModel.discos.collectAsState(initial = emptyList())

    // Estado para el diálogo de borrado
    var discoAEliminar by remember { mutableStateOf<Disco?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            ListaDiscosTopAppBar(
                title = "DiscosApp Andres", // Cambia por tu nombre real
                canNavigateBack = false
            )
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
        if (discos.isEmpty()) {
            // Pantalla vacía
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
        } else {
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
                            .clickable { onDiscoClick(disco.id) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(disco.titulo, style = MaterialTheme.typography.titleMedium)
                                Text(disco.autor, style = MaterialTheme.typography.bodyMedium)
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