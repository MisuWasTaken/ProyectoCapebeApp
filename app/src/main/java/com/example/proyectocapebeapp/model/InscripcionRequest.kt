package com.example.proyectocapebeapp.model

data class InscripcionRequest(
    val usuarioId: Long,
    val vueloId: Long,
    val nombre: String,
    val email: String,
    val consentimientoAceptado: Boolean
)