package com.example.proyectocapebeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity

@Dao
interface JornadaDao {

    @Insert
    suspend fun crearJornada(jornada: JornadaEntity)

    @Query("SELECT * FROM jornadas")
    suspend fun obtenerJornadas(): List<JornadaEntity>

    @Query("SELECT * FROM jornadas WHERE id = :jornadaId LIMIT 1")
    suspend fun obtenerJornadaPorId(jornadaId: Int): JornadaEntity?
}
