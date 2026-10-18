package com.example.proyectocapebeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyectocapebeapp.data.local.entity.InscripcionEntity

@Dao
interface InscripcionDao {

    @Insert
    suspend fun crearInscripcion(inscripcion: InscripcionEntity)

    @Query(
        "SELECT * FROM inscripciones " +
                "WHERE usuarioId = :usuarioId AND jornadaId = :jornadaId LIMIT 1"
    )
    suspend fun obtenerInscripcion(
        usuarioId: Int,
        jornadaId: Int
    ): InscripcionEntity?

    @Query("SELECT * FROM inscripciones WHERE usuarioId = :usuarioId")
    suspend fun obtenerInscripcionesUsuario(
        usuarioId: Int
    ): List<InscripcionEntity>

    @Query("SELECT * FROM inscripciones WHERE jornadaId = :jornadaId")
    suspend fun obtenerInscripcionesJornada(
        jornadaId: Int
    ): List<InscripcionEntity>
}