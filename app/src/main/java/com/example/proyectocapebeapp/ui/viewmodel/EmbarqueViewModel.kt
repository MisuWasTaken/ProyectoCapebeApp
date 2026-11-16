
package com.example.proyectocapebeapp.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.api.RetrofitEmbarque
import com.example.proyectocapebeapp.data.api.RetrofitInscripcion
import com.example.proyectocapebeapp.data.api.RetrofitVuelo
import com.example.proyectocapebeapp.model.EmbarqueRequest
import com.example.proyectocapebeapp.model.EmbarqueResponse
import com.example.proyectocapebeapp.model.InscripcionResponse
import com.example.proyectocapebeapp.model.VueloResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import okhttp3.MediaType
import okhttp3.RequestBody

class EmbarqueViewModel : ViewModel() {

    var vuelos by mutableStateOf<List<VueloResponse>>(emptyList())
        private set

    var vueloSeleccionado by mutableStateOf<VueloResponse?>(null)
        private set

    var pasajeros by mutableStateOf<List<InscripcionResponse>>(emptyList())
        private set

    var embarques by mutableStateOf<List<EmbarqueResponse>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    var registrando by mutableStateOf(false)
        private set

    fun cargarVuelos() {
        viewModelScope.launch {
            cargando = true
            mensaje = null

            try {
                vuelos = RetrofitVuelo.api.obtenerTodos()
            } catch (e: Exception) {
                mensaje = "No se pudieron cargar los vuelos."
            } finally {
                cargando = false
            }
        }
    }

    fun seleccionarVuelo(vuelo: VueloResponse) {
        vueloSeleccionado = vuelo
        pasajeros = emptyList()
        embarques = emptyList()

        viewModelScope.launch {
            cargando = true
            mensaje = null

            try {
                pasajeros = RetrofitInscripcion.api.obtenerPorVuelo(vuelo.id)
                embarques = RetrofitEmbarque.api.obtenerPorVuelo(vuelo.id)
            } catch (e: Exception) {
                mensaje = "No se pudieron cargar los pasajeros o vuelos."
            } finally {
                cargando = false
            }
        }
    }

    fun registrarEmbarque(pasajero: InscripcionResponse) {
        val vuelo = vueloSeleccionado ?: return

        if (registrando || embarques.any { it.inscripcionId == pasajero.id }) {
            return
        }

        viewModelScope.launch {
            registrando = true
            mensaje = null

            try {
                val request = EmbarqueRequest(
                    vueloId = vuelo.id,
                    inscripcionId = pasajero.id
                )

                val json = Gson().toJson(request)
                val data = RequestBody.create(
                    MediaType.parse("application/json"),
                    json
                )

                val embarque = RetrofitEmbarque.api.registrar(data)

                embarques = embarques + embarque
                mensaje = "Control iniciado correctamente."

            } catch (e: HttpException) {
                mensaje = when (e.code()) {
                    400 -> "La inscripcion no corresponde al vuelo."
                    409 -> "Este pasajero ya tiene un vuelo registrado."
                    503 -> "El servicio de inscripciones no esta disponible."
                    else -> "Error al registrar vuelo: ${e.code()}"
                }
            } catch (e: Exception) {
                mensaje = "No se pudo conectar con el servicio de operaciones."
            } finally {
                registrando = false
            }
        }
    }

    fun cambiarEstado(embarque: EmbarqueResponse, estado: String) {
        if (registrando || estado !in listOf("EMBARCADO", "NO_EMBARCADO")) {
            return
        }

        viewModelScope.launch {
            registrando = true
            mensaje = null

            try {
                val actualizado = RetrofitEmbarque.api.cambiarEstado(
                    embarque.id,
                    mapOf("estado" to estado)
                )

                embarques = embarques.map {
                    if (it.id == actualizado.id) actualizado else it
                }

                mensaje = "Participacion actualizada correctamente."

            } catch (e: HttpException) {
                mensaje = when (e.code()) {
                    400 -> "El estado seleccionado no es valido."
                    404 -> "No se encontro el registro del pasajero."
                    else -> "Error al cambiar el estado: ${e.code()}"
                }
            } catch (e: Exception) {
                mensaje = "No se pudo conectar con el servicio de operaciones."
            } finally {
                registrando = false
            }
        }
    }

    fun volverAVuelos() {
        vueloSeleccionado = null
        pasajeros = emptyList()
        embarques = emptyList()
        mensaje = null
    }
}
