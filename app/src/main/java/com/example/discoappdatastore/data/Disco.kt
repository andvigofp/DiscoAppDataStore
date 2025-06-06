package com.example.discoappdatastore.data

import kotlinx.serialization.Serializable

@Serializable
data class Disco(
    val id: Int = 0,
    val titulo: String,
    val autor: String,
    val numCanciones: Int,
    val publicacion: Int,
    var valoracion: Int,
)