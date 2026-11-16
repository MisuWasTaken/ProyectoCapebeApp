package com.example.proyectocapebeapp.model

data class VueloResponse(
    val id: Long,
    val fecha: String,
    val hora: String,
    val cupos: Int,
    val estado: String,
    val origen: String,
    val destino: String
)