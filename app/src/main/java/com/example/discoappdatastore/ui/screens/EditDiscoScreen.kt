package com.example.discoappdatastore.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.discoappdatastore.data.Disco
import com.example.discoappdatastore.ui.state.DiscoViewModel
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDiscoScreen(
    viewModel: DiscoViewModel,
    discoId: Int,
    onBack: () -> Unit
) {
    val discoState by viewModel.discos.collectAsStateWithLifecycle()
    val disco = discoState.find { it.id == discoId }

    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var numCanciones by remember { mutableStateOf("") }
    var anio by remember { mutableStateOf("") }
    var valoracion by remember { mutableStateOf(0) }

    // Cargar los datos del disco cuando esté disponible
    LaunchedEffect(disco) {
        if (disco != null) {
            titulo = disco.titulo
            autor = disco.autor
            numCanciones = disco.numCanciones.toString()
            anio = disco.publicacion.toString()
            valoracion = disco.valoracion
        }
    }

    // La validación debe basarse en los campos editables
     val datosValidos = autor.isNotBlank() &&
            numCanciones.toIntOrNull() in 1..99 &&
            anio.toIntOrNull() in 1000..2030 &&
            valoracion in 1..5

    Scaffold(
        topBar = {
            ListaDiscosTopAppBar(
                title = "Editar disco",
                canNavigateBack = true,
                navigateUp = onBack
            )
        }
    ) { padding ->
        if (disco != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // Título - No editable
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
                        onValueChange = { }, // No permitir cambios
                        label = { Text("Título") },
                        modifier = Modifier.weight(2f),
                        singleLine = true,
                        readOnly = true, // Hacerlo no editable
                        colors = OutlinedTextFieldDefaults.colors(
                             // Usar colores para estado deshabilitado/solo lectura
                            disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            disabledBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
                            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                             disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            disabledSupportingTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                             disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                         )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Autor - Editable
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

                // Número de canciones - Editable
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

                // Año de publicación - Editable
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

                // Valoración - Editable
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

                // Botón de guardar
                Button(
                    onClick = {
                        val discoActualizado = disco.copy(
                            titulo = disco.titulo, // Usar el título del objeto disco original
                            autor = autor,
                            numCanciones = numCanciones.toInt(),
                            publicacion = anio.toInt(),
                            valoracion = valoracion
                        )
                        viewModel.updateDisco(discoActualizado)
                        onBack()
                    },
                    enabled = datosValidos,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Icon(Icons.Filled.Save, contentDescription = "Guardar cambios")
                    Spacer(Modifier.width(8.dp))
                    Text("GUARDAR CAMBIOS", style = MaterialTheme.typography.titleMedium)
                }
            }
        } else {
             Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("Disco no encontrado")
            }
        }
    }
} 