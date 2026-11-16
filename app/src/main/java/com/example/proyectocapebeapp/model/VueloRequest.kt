package com.example.proyectocapebeapp.model

data class VueloRequest(
    val fecha: String,
    val hora: String,
    val cupos: Int,
    val origen: String,
    val destino: String
)