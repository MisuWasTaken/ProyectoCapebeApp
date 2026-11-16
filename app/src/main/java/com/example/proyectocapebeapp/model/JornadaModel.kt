package com.example.proyectocapebeapp.model

data class JornadaModel(
    val fecha: String = "",
    val hora: String = "",
    val cupos: String = "",
    val origen: String = "",
    val destino: String = "",
    val errorMessage: String? = null
)