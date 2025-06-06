package com.example.discoappdatastore.ui.screens

import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.discoappdatastore.ui.state.DiscoViewModel
import kotlin.collections.find
import kotlin.let
import kotlin.text.repeat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleScreen(
    viewModel: DiscoViewModel,
    discoId: Int,
    onBack: () -> Unit
) {
    val discos by viewModel.discos.collectAsState(initial = emptyList())
    val disco = discos.find { it.id == discoId }

    Scaffold(
        topBar = {
            ListaDiscosTopAppBar(
                title = "Detalle",
                canNavigateBack = true,
                navigateUp = onBack
            )
        }
    ) { padding ->
        disco?.let {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Título: ${it.titulo}")
                Text("Autor: ${it.autor}")
                Text("Número de canciones: ${it.numCanciones}")
                Text("Año de publicación: ${it.publicacion}")
                Text("Valoración: ${"⭐".repeat(it.valoracion)}")
            }
        } ?: Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text("Disco no encontrado")
        }
    }
}
