package com.example.discoappdatastore.ui.screens

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDeleteDialog(
    show: Boolean,
    discoTitulo: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Confirmación de borrado") },
            text = { Text("¿Estás seguro de borrar el álbum $discoTitulo?") },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("BORRAR")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("CANCELAR")
                }
            }
        )
    }
}