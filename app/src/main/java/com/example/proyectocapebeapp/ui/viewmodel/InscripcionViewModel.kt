package com.example.proyectocapebeapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.database.AppDatabase
import com.example.proyectocapebeapp.data.local.entity.InscripcionEntity
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity
import kotlinx.coroutines.launch

class InscripcionViewModel : ViewModel() {

    var misVuelos by mutableStateOf<List<JornadaEntity>>(emptyList())
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set

    var vuelosInscritos by mutableStateOf<List<Int>>(emptyList())
        private set
    fun inscribirseEnVuelo(
        context: Context,
        usuarioId: Int,
        jornadaId: Int
    ) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val inscripcionExistente =
                db.inscripcionDao().obtenerInscripcion(usuarioId, jornadaId)
            if (inscripcionExistente != null) {
                vuelosInscritos = vuelosInscritos + jornadaId
                mensaje = "Ya estas inscrito en este vuelo."
                return@launch
            }
            val inscripcion = InscripcionEntity(
                usuarioId = usuarioId,
                jornadaId = jornadaId
            )
            db.inscripcionDao().crearInscripcion(inscripcion)
            mensaje = "Inscripcion realizada con exito."
        }
    }

    fun cargarVuelosInscritos(
        context: Context,
        usuarioId: Int
    ) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val inscripciones =
                db.inscripcionDao().obtenerInscripcionesUsuario(usuarioId)
            val vuelos = mutableListOf<Int>()
            for (inscripcion in inscripciones) {
                vuelos.add(inscripcion.jornadaId)
            }
            vuelosInscritos = vuelos
        }
    }

    fun cargarMisVuelos(
        context: Context,
        usuarioId: Int
    ) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val inscripciones =
                db.inscripcionDao().obtenerInscripcionesUsuario(usuarioId)
            val vuelos = mutableListOf<JornadaEntity>()
            for (inscripcion in inscripciones) {
                val vuelo =
                    db.jornadaDao().obtenerJornadaPorId(inscripcion.jornadaId)
                if (vuelo != null) {
                    vuelos.add(vuelo)
                }
            }
            misVuelos = vuelos
        }
    }
}