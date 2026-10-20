package com.example.proyectocapebeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "inscripciones")
data class InscripcionEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val jornadaId: Int
)