package com.example.proyectocapebeapp.model

data class PasajeroInscritoModel(
    val inscripcionId: Int,
    val usuarioId: Int,
    val nombre: String,
    val email: String,
    val jornadaId: Int,
    val fotoPath: String? = null,
    val consentimientoAceptado: Boolean
)