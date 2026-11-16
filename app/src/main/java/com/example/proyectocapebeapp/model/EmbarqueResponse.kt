
package com.example.proyectocapebeapp.model

data class EmbarqueResponse(
    val id: Long,
    val vueloId: Long,
    val inscripcionId: Long,
    val estado: String,
    val fotoUrl: String?
)
