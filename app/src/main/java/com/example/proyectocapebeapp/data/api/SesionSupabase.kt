
package com.example.proyectocapebeapp.data.api

import android.os.SystemClock
import java.io.IOException

object SesionSupabase {
    private data class Sesion(
        val token: String,
        val usuarioId: String,
        val refreshToken: String,
        val vencimiento: Long
    )

    @Volatile
    private var sesion: Sesion? = null

    private val bloqueoSesion = Any()

    val accessToken: String?
        get() = sesion?.token

    val usuarioId: String?
        get() = sesion?.usuarioId

    fun iniciar(
        token: String,
        id: String,
        refreshToken: String,
        expiresIn: Long
    ) {
        synchronized(bloqueoSesion) {
            sesion = Sesion(
                token,
                id,
                refreshToken,
                SystemClock.elapsedRealtime() + expiresIn * 1000
            )
        }
    }

    @Synchronized
    fun tokenVigente(): String? {
        val actual = sesion ?: return null

        if (SystemClock.elapsedRealtime() < actual.vencimiento - 60000) {
            return actual.token
        }

        val respuesta = RetrofitSupabaseAuth.api.renovarSesion(
            mapOf("refresh_token" to actual.refreshToken)
        ).execute()

        if (!respuesta.isSuccessful) {
            respuesta.errorBody()?.close()
            throw IOException("No se pudo renovar la sesion.")
        }

        val datos = respuesta.body()
            ?: throw IOException("Respuesta de sesion vacia.")

        val nuevoToken = datos.accessToken
            ?: throw IOException("Token no valido.")

        val nuevoRefresh = datos.refreshToken
            ?: throw IOException("Refresh de token no valido.")

        synchronized(bloqueoSesion) {
            if (sesion !== actual) {
                throw IOException("La sesion ha cambiado.")
            }

            sesion = Sesion(
                nuevoToken,
                actual.usuarioId,
                nuevoRefresh,
                SystemClock.elapsedRealtime() + datos.expiresIn * 1000
            )
        }

        return nuevoToken
    }

    fun cerrar() {
        synchronized(bloqueoSesion) {
            sesion = null
        }
    }
}
