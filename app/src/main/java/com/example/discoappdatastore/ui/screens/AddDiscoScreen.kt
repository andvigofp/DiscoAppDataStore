package com.example.discoappdatastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.discoappdatastore.data.Disco
import com.example.discoappdatastore.ui.state.DiscoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDiscoScreen(
    viewModel: DiscoViewModel,
    onBack: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var numCanciones by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var valoracion by remember { mutableStateOf(0) }

    val datosValidos = titulo.isNotBlank() &&
            autor.isNotBlank() &&
            numCanciones.toIntOrNull() in 1..99 &&
            anio.toIntOrNull() in 1000..2030 &&
            valoracion in 1..5

    Scaffold(
        topBar = {
            ListaDiscosTopAppBar(
                title = "Añadir disco",
                canNavigateBack = true,
                navigateUp = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Título
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Título",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.weight(2f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Autor
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Autor",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    modifier = Modifier.weight(2f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Número de canciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Nº canciones",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedTextField(
                    value = numCanciones,
                    onValueChange = { numCanciones = it.filter { c -> c.isDigit() } },
                    label = { Text("Número") },
                    modifier = Modifier.weight(2f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Año de publicación
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Año",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge
                )
                OutlinedTextField(
                    value = anio,
                    onValueChange = { anio = it.filter { c -> c.isDigit() } },
                    label = { Text("Año") },
                    modifier = Modifier.weight(2f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Valoración
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Valoración",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row {
                    for (i in 1..5) {
                        IconButton(
                            onClick = { valoracion = i },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Estrella $i",
                                modifier = Modifier.size(36.dp),
                                tint = if (valoracion >= i)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón de añadir
            Button(
                onClick = {
                    viewModel.addDisco(
                        Disco(
                            titulo = titulo,
                            autor = autor,
                            numCanciones = numCanciones.toInt(),
                            publicacion = anio.toInt(),
                            valoracion = valoracion
                        )
                    )
                    onBack()
                },
                enabled = datosValidos,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("AÑADIR DISCO", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}