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
import com.example.proyectocapebeapp.model.PasajeroInscritoModel
import com.example.proyectocapebeapp.notification.NotificacionHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class InscripcionViewModel : ViewModel() {

    var misVuelos by mutableStateOf<List<JornadaEntity>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    var vuelosInscritos by mutableStateOf<List<Int>>(emptyList())
        private set

    var pasajerosInscritos by
    mutableStateOf<List<PasajeroInscritoModel>>(emptyList())
        private set


    fun inscribirseEnVuelo(
        context: Context,
        usuarioId: Int,
        jornadaId: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val inscripcionExistente =
                db.inscripcionDao()
                    .obtenerInscripcion(usuarioId, jornadaId)
            if (inscripcionExistente != null) {
                mensaje = "Ya estas inscrito en este vuelo."
                return@launch
            }
            val vuelo =
                db.jornadaDao().obtenerJornadaPorId(jornadaId)
            if (vuelo == null) {
                mensaje = "El vuelo ya no esta disponible."
                return@launch
            }
            val inscritos =
                db.inscripcionDao().contarInscritos(jornadaId)
            if (inscritos >= vuelo.cupos) {
                mensaje = "Este vuelo ya no tiene cupos disponibles."
                return@launch
            }
            val inscripcion = InscripcionEntity(
                usuarioId = usuarioId,
                jornadaId = jornadaId,
                consentimientoAceptado = true
            )
            db.inscripcionDao().crearInscripcion(inscripcion)
            val inscripcionGuardada =
                db.inscripcionDao().obtenerInscripcion(
                    usuarioId,
                    jornadaId
                )
            if (inscripcionGuardada != null) {
                NotificacionHelper.programarRecordatorio(
                    context = context,
                    inscripcionId = inscripcionGuardada.id,
                    fecha = vuelo.fecha,
                    hora = vuelo.hora
                )
            }
            vuelosInscritos = vuelosInscritos + jornadaId
            mensaje = "Te has inscrito correctamente a este vuelo."
            onSuccess()
        }
    }

    fun cargarVuelosInscritos(
        context: Context,
        usuarioId: Int
    ) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val inscripciones =
                db.inscripcionDao()
                    .obtenerInscripcionesUsuario(usuarioId)
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
                db.inscripcionDao()
                    .obtenerInscripcionesUsuario(usuarioId)
            val vuelos = mutableListOf<JornadaEntity>()
            for (inscripcion in inscripciones) {
                val vuelo =
                    db.jornadaDao()
                        .obtenerJornadaPorId(inscripcion.jornadaId)
                if (vuelo != null) {
                    vuelos.add(vuelo)
                }
            }
            misVuelos = vuelos
        }
    }
    fun cargarPasajerosInscritos(
        context: Context,
        jornadaId: Int
    ) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val inscripciones =
                db.inscripcionDao()
                    .obtenerInscripcionesJornada(jornadaId)
            val pasajeros =
                mutableListOf<PasajeroInscritoModel>()
            for (inscripcion in inscripciones) {
                val usuario =
                    db.usuarioDao()
                        .getUserById(inscripcion.usuarioId)
                if (usuario != null) {
                    val pasajero = PasajeroInscritoModel(
                        inscripcionId = inscripcion.id,
                        usuarioId = usuario.id,
                        nombre = usuario.name,
                        email = usuario.email,
                        jornadaId = jornadaId,
                        fotoPath = usuario.fotoPath,
                        consentimientoAceptado = inscripcion.consentimientoAceptado
                    )
                    pasajeros.add(pasajero)
                }
            }
            pasajerosInscritos = pasajeros
        }
    }


    fun esFechaPosterior(
        fechaActual: String,
        nuevaFecha: String
    ): Boolean {
        return try {
            val formato =
                SimpleDateFormat(
                    "d/M/yyyy",
                    Locale.getDefault()
                )
            formato.isLenient = false
            val actual = formato.parse(fechaActual)
            val nueva = formato.parse(nuevaFecha)
            actual != null &&
                    nueva != null &&
                    nueva.after(actual)
        } catch (e: Exception) {
            false
        }
    }
    fun cambiarVuelo(
        context: Context,
        inscripcionId: Int,
        usuarioId: Int,
        nuevaJornadaId: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val nuevoVuelo =
                db.jornadaDao()
                    .obtenerJornadaPorId(nuevaJornadaId)
            if (nuevoVuelo == null) {
                mensaje = "El vuelo seleccionado ya no existe."
                return@launch
            }
            val inscritos =
                db.inscripcionDao()
                    .contarInscritos(nuevaJornadaId)
            if (inscritos >= nuevoVuelo.cupos) {
                mensaje = "El vuelo seleccionado ya no tiene cupos."
                return@launch
            }
            val inscripcionExistente =
                db.inscripcionDao()
                    .obtenerInscripcion(
                        usuarioId,
                        nuevaJornadaId
                    )
            NotificacionHelper.programarRecordatorio(
                context = context,
                inscripcionId = inscripcionId,
                fecha = nuevoVuelo.fecha,
                hora = nuevoVuelo.hora
            )
            if (inscripcionExistente != null) {
                mensaje = "El pasajero ya esta inscrito en ese vuelo."
                return@launch
            }
            db.inscripcionDao().cambiarVuelo(
                inscripcionId = inscripcionId,
                nuevaJornadaId = nuevaJornadaId
            )
            mensaje = "Vuelo actualizado correctamente."
            onSuccess()
        }
    }
}