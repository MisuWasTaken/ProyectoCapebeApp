package com.example.proyectocapebeapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jornadas")
data class JornadaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fecha: String,
    val hora: String,
    val cupos: Int,
    val estado: String
)