package com.example.proyectocapebeapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.database.AppDatabase
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity
import com.example.proyectocapebeapp.model.JornadaModel
import kotlinx.coroutines.launch

class JornadaViewModel : ViewModel() {

    var uiState by mutableStateOf(JornadaModel())
        private set
    var jornadas by mutableStateOf<List<JornadaEntity>>(emptyList())
        private set

    var cuposDisponibles by mutableStateOf<Map<Int, Int>>(emptyMap())
        private set
    fun cargarJornadas(context: Context) {

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            jornadas = db.jornadaDao().obtenerJornadas()
            val cupos = mutableMapOf<Int, Int>()
            for (jornada in jornadas) {
                val inscritos =
                    db.inscripcionDao().contarInscritos(jornada.id)
                val disponibles =
                    jornada.cupos - inscritos
                if (disponibles < 0) {
                    cupos[jornada.id] = 0
                } else {
                    cupos[jornada.id] = disponibles
                }
            }
            cuposDisponibles = cupos
        }
    }
    fun onFechaChanged(fecha: String) {
        uiState = uiState.copy(
            fecha = fecha,
            errorMessage = null
        )
    }

    fun onHoraChanged(hora: String) {
        uiState = uiState.copy(
            hora = hora,
            errorMessage = null
        )
    }

    fun onCuposChanged(cupos: String) {
        uiState = uiState.copy(
            cupos = cupos,
            errorMessage = null
        )
    }

    fun onCrearJornadaClick(
        context: Context,
        onSuccess: () -> Unit
    ) {

        if (
            uiState.fecha.isBlank() ||
            uiState.hora.isBlank() ||
            uiState.cupos.isBlank()
        ) {
            uiState = uiState.copy(
                errorMessage = "Los campos no pueden estar vacios."
            )
            return
        }

        val cuposNumero = uiState.cupos.toIntOrNull()

        if (cuposNumero == null || cuposNumero <= 0) {
            uiState = uiState.copy(
                errorMessage = "Los cupos deben ser un numero mayor a 0."
            )
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val jornada = JornadaEntity(
                fecha = uiState.fecha,
                hora = uiState.hora,
                cupos = cuposNumero,
                estado = "Disponible"
            )
            db.jornadaDao().crearJornada(jornada)
            onSuccess()
        }
    }
}