package com.example.proyectocapebeapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.api.RetrofitVuelo
import com.example.proyectocapebeapp.data.database.AppDatabase
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity
import com.example.proyectocapebeapp.model.JornadaModel
import com.example.proyectocapebeapp.model.VueloRequest
import com.example.proyectocapebeapp.model.VueloResponse
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JornadaViewModel : ViewModel() {

    var uiState by mutableStateOf(JornadaModel())
        private set
    var jornadas by mutableStateOf<List<JornadaEntity>>(emptyList())
        private set

    var cuposDisponibles by mutableStateOf<Map<Int, Int>>(emptyMap())
        private set

    var vuelosBackend by mutableStateOf<List<VueloResponse>>(emptyList())
        private set

    var errorVuelos by mutableStateOf<String?>(null)
        private set

    var origen by mutableStateOf("")
        private set

    var destino by mutableStateOf("")
        private set

    fun cargarVuelosBackend() {
        viewModelScope.launch {
            try {
                vuelosBackend = RetrofitVuelo.api.obtenerTodos()
                errorVuelos = null
            } catch (e: Exception) {
                errorVuelos = "No se pudieron cargar los vuelos."
            }
        }
    }

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

    fun onOrigenChanged(valor: String) {
        origen = valor
        uiState = uiState.copy(errorMessage = null)
    }

    fun onDestinoChanged(valor: String) {
        destino = valor
        uiState = uiState.copy(errorMessage = null)
    }

    fun esVueloFuturo(fecha: String, hora: String): Boolean {
        return try {
            val formato = SimpleDateFormat(
                "d/M/yyyy H:mm",
                Locale.getDefault()
            )
            formato.isLenient = false
            val fechaHora = formato.parse("$fecha $hora")
            fechaHora != null && fechaHora.after(Date())
        } catch (e: Exception) {
            false
        }
    }

    fun onCrearJornadaClick(onSuccess: () -> Unit) {

        if (
            uiState.fecha.isBlank() ||
            uiState.hora.isBlank() ||
            uiState.cupos.isBlank() ||
            origen.isBlank() ||
            destino.isBlank()
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

        if (!esVueloFuturo(uiState.fecha, uiState.hora)) {
            uiState = uiState.copy(
                errorMessage = "La fecha y hora del vuelo deben ser posteriores a la actual."
            )
            return
        }

        viewModelScope.launch {
            try {
                val fechaPartes = uiState.fecha.split("/")
                val horaPartes = uiState.hora.split(":")
                val fecha = "${fechaPartes[2]}-${fechaPartes[1].padStart(2, '0')}-${fechaPartes[0].padStart(2, '0')}"
                val hora = "${horaPartes[0].padStart(2, '0')}:${horaPartes[1].padStart(2, '0')}:00"

                val request = VueloRequest(fecha, hora, cuposNumero, origen.trim(), destino.trim())
                RetrofitVuelo.api.crear(request)

                uiState = JornadaModel()
                origen = ""
                destino = ""
                onSuccess()
            } catch (e: Exception) {
                uiState = uiState.copy(
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }
}