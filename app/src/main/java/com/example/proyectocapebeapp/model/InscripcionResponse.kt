
package com.example.proyectocapebeapp.model

data class InscripcionResponse(
    val id: Long,
    val usuarioId: Long,
    val vueloId: Long,
    val nombre: String,
    val email: String,
    val consentimientoAceptado: Boolean,
    val usuarioAuthId: String? = null
)
