
package com.example.proyectocapebeapp.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectocapebeapp.data.api.RetrofitSupabaseAuth
import com.example.proyectocapebeapp.data.api.SesionSupabase
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream

class FotoIdentificacionViewModel : ViewModel() {

    var fotoBitmap by mutableStateOf<Bitmap?>(null)
        private set

    var cargando by mutableStateOf(false)
        private set

    fun cargarFoto(
        context: Context,
        usuarioId: Int
    ) {
        val token = SesionSupabase.accessToken ?: return
        val id = SesionSupabase.usuarioId ?: return

        fotoBitmap = null

        viewModelScope.launch {
            cargando = true

            try {
                val respuesta = RetrofitSupabaseAuth.api.obtenerFoto(
                    autorizacion = "Bearer $token",
                    archivo = "$id.jpg"
                )

                if (respuesta.isSuccessful) {
                    val datos = respuesta.body()?.use {
                        it.bytes()
                    }

                    if (datos != null) {
                        fotoBitmap = BitmapFactory.decodeByteArray(
                            datos, 0, datos.size
                        )
                    }
                } else {
                    val detalle =
                        respuesta.errorBody()?.string().orEmpty()

                    if (respuesta.code() != 404 &&
                        !detalle.contains("Object not found", true)
                    ) {
                        Toast.makeText(
                            context,
                            "Error al cargar foto: ${respuesta.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "No se pudo cargar la foto.",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                cargando = false
            }
        }
    }

    fun guardarFoto(
        context: Context,
        usuarioId: Int,
        bitmap: Bitmap,
        onSuccess: () -> Unit
    ) {
        val token = SesionSupabase.accessToken ?: return
        val id = SesionSupabase.usuarioId ?: return

        if (cargando) return

        viewModelScope.launch {
            cargando = true

            try {
                val archivo = ByteArrayOutputStream()

                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    archivo
                )

                val foto = RequestBody.create(
                    MediaType.parse("image/jpeg"),
                    archivo.toByteArray()
                )

                val respuesta = RetrofitSupabaseAuth.api.subirFoto(
                    autorizacion = "Bearer $token",
                    archivo = "$id.jpg",
                    foto = foto
                )

                if (respuesta.isSuccessful) {
                    respuesta.body()?.close()
                    fotoBitmap = bitmap
                    onSuccess()
                } else {
                    respuesta.errorBody()?.close()

                    Toast.makeText(
                        context,
                        "Error al guardar foto: ${respuesta.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "No se pudo guardar la foto.",
                    Toast.LENGTH_SHORT
                ).show()
            } finally {
                cargando = false
            }
        }
    }
}
