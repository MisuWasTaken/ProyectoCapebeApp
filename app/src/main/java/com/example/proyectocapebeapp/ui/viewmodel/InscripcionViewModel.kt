
package com.example.proyectocapebeapp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.api.RetrofitInscripcion
import com.example.proyectocapebeapp.data.api.RetrofitVuelo
import com.example.proyectocapebeapp.data.database.AppDatabase
import com.example.proyectocapebeapp.model.InscripcionRequest
import com.example.proyectocapebeapp.model.PasajeroInscritoModel
import com.example.proyectocapebeapp.model.VueloResponse
import com.example.proyectocapebeapp.notification.NotificacionHelper
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.proyectocapebeapp.data.api.RetrofitSupabaseAuth
import com.example.proyectocapebeapp.data.api.SesionSupabase
import kotlinx.coroutines.CancellationException

class InscripcionViewModel : ViewModel() {

    var misVuelos by mutableStateOf<List<VueloResponse>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    var vuelosInscritos by mutableStateOf<List<Long>>(emptyList())
        private set

    var pasajerosInscritos by mutableStateOf<List<PasajeroInscritoModel>>(emptyList())
        private set

    var cuposBackend by mutableStateOf<Map<Long, Int>>(emptyMap())
        private set

    fun inscribirseEnVuelo(
        context: Context,
        usuarioId: Int,
        jornadaId: Long,
        fecha: String,
        hora: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            mensaje = null
            try {
                val db = AppDatabase.getDatabase(context)
                val usuario = db.usuarioDao().getUserById(usuarioId)

                if (usuario == null) {
                    mensaje = "Usuario no encontrado."
                    return@launch
                }

                val request = InscripcionRequest(
                    usuarioId = usuarioId.toLong(),
                    vueloId = jornadaId,
                    nombre = usuario.name,
                    email = usuario.email,
                    consentimientoAceptado = true
                )

                val inscripcion = RetrofitInscripcion.api.crear(request)

                vuelosInscritos = vuelosInscritos + jornadaId
                onSuccess()

                try {
                    NotificacionHelper.programarRecordatorio(
                        context = context,
                        inscripcionId = inscripcion.id.toInt(),
                        fecha = fecha,
                        hora = hora
                    )
                } catch (e: Exception) {
                    mensaje = "Inscripcion guardada, pero fallo el recordatorio."
                }

            } catch (e: HttpException) {
                mensaje = when (e.code()) {
                    409 -> "Ya estas inscrito o el vuelo no tiene cupos."
                    400 -> "No se pudo realizar la inscripcion. Revisa los datos."
                    503 -> "El servicio de vuelos no esta disponible."
                    else -> "Error al inscribirse: ${e.code()}"
                }
            } catch (e: Exception) {
                mensaje = "No se pudo conectar con el servicio de inscripciones."
            }
        }
    }

    fun cargarVuelosInscritos(context: Context, usuarioId: Int) {
        viewModelScope.launch {
            try {
                val db = AppDatabase.getDatabase(context)
                val usuario = db.usuarioDao().getUserById(usuarioId)

                if (usuario == null) {
                    mensaje = "Usuario no encontrado."
                    return@launch
                }

                val inscripciones = RetrofitInscripcion.api.obtenerPorEmail(usuario.email)
                vuelosInscritos = inscripciones.map { it.vueloId }
                mensaje = null

            } catch (e: Exception) {
                mensaje = "No se pudieron consultar tus inscripciones."
            }
        }
    }

    fun cargarMisVuelos(context: Context, usuarioId: Int) {
        viewModelScope.launch {
            try {
                val db = AppDatabase.getDatabase(context)
                val usuario = db.usuarioDao().getUserById(usuarioId)

                if (usuario == null) {
                    mensaje = "Usuario no encontrado."
                    return@launch
                }

                val inscripciones = RetrofitInscripcion.api.obtenerPorEmail(usuario.email)
                val ids = inscripciones.map { it.vueloId }

                misVuelos = RetrofitVuelo.api.obtenerTodos()
                    .filter { it.id in ids }

                mensaje = null

            } catch (e: Exception) {
                mensaje = "No se pudieron cargar tus vuelos."
            }
        }
    }

    fun cargarCuposBackend(vuelos: List<VueloResponse>) {
        viewModelScope.launch {
            try {
                val cupos = mutableMapOf<Long, Int>()

                for (vuelo in vuelos) {
                    val inscritos = RetrofitInscripcion.api.contarPorVuelo(vuelo.id)
                    cupos[vuelo.id] = (vuelo.cupos - inscritos).coerceAtLeast(0)
                }

                cuposBackend = cupos

            } catch (e: Exception) {
                mensaje = "No se pudieron consultar los cupos disponibles."
            }
        }
    }
    fun cargarPasajerosInscritos(context: Context, jornadaId: Long) {
        viewModelScope.launch {
            pasajerosInscritos = emptyList()
            try {
                val inscripciones =
                    RetrofitInscripcion.api.obtenerPorVuelo(jornadaId)
                val pasajeros = mutableListOf<PasajeroInscritoModel>()
                for (inscripcion in inscripciones) {
                    pasajeros.add(
                        PasajeroInscritoModel(
                            inscripcionId = inscripcion.id.toInt(),
                            usuarioId = inscripcion.usuarioId.toInt(),
                            nombre = inscripcion.nombre,
                            email = inscripcion.email,
                            jornadaId = inscripcion.vueloId.toInt(),
                            consentimientoAceptado = inscripcion.consentimientoAceptado,
                            usuarioAuthId = inscripcion.usuarioAuthId
                        )
                    )
                }
                pasajerosInscritos = pasajeros
                mensaje = null
            } catch (e: Exception) {
                mensaje = "No se pudieron cargar los pasajeros inscritos."
            }
        }
    }

    suspend fun obtenerFotoPasajero(usuarioAuthId: String?): Bitmap? {
        val token = SesionSupabase.accessToken ?: return null
        if (usuarioAuthId.isNullOrBlank()) return null
        return try {
            val respuesta = RetrofitSupabaseAuth.api.obtenerFoto(
                autorizacion = "Bearer $token",
                archivo = "$usuarioAuthId.jpg"
            )
            if (respuesta.isSuccessful) {
                val datos = respuesta.body()?.use {
                    it.bytes()
                }
                if (datos != null) {
                    BitmapFactory.decodeByteArray(datos, 0, datos.size)
                } else {
                    null
                }
            } else {
                respuesta.errorBody()?.close()
                null
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            null
        }
    }

    fun esFechaPosterior(
        fechaActual: String,
        horaActual: String,
        nuevaFecha: String,
        nuevaHora: String
    ): Boolean {
        return try {
            val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formato.isLenient = false

            val actual = formato.parse("$fechaActual $horaActual")
            val nueva = formato.parse("$nuevaFecha $nuevaHora")

            actual != null && nueva != null && nueva.after(actual)
        } catch (e: Exception) {
            false
        }
    }

    fun cambiarVuelo(
        context: Context,
        inscripcionId: Int,
        nuevaJornadaId: Long,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            mensaje = null
            try {
                RetrofitInscripcion.api.cambiarVuelo(
                    inscripcionId.toLong(),
                    mapOf("nuevoVueloId" to nuevaJornadaId)
                )

                mensaje = "Vuelo actualizado correctamente."
                onSuccess()

                try {
                    val nuevoVuelo = RetrofitVuelo.api.obtenerTodos()
                        .firstOrNull { it.id == nuevaJornadaId }

                    if (nuevoVuelo != null) {
                        val partes = nuevoVuelo.fecha.split("-")
                        val fecha = "${partes[2].toInt()}/${partes[1].toInt()}/${partes[0]}"

                        NotificacionHelper.programarRecordatorio(
                            context = context,
                            inscripcionId = inscripcionId,
                            fecha = fecha,
                            hora = nuevoVuelo.hora.take(5)
                        )
                    }
                } catch (e: Exception) {
                    mensaje = "Vuelo actualizado, pero fallo el recordatorio."
                }

            } catch (e: HttpException) {
                mensaje = when (e.code()) {
                    404 -> "La inscripcion no existe."
                    409 -> "El vuelo no esta disponible, no tiene cupos o no es posterior."
                    503 -> "El servicio de vuelos no esta disponible."
                    else -> "Error al cambiar vuelo: ${e.code()}"
                }
            } catch (e: Exception) {
                mensaje = "No se pudo conectar con el servicio de inscripciones."
            }
        }
    }
}
