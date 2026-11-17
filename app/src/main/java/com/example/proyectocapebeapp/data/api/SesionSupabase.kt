package com.example.proyectocapebeapp.data.api

object SesionSupabase {
    var accessToken: String? = null
        private set

    var usuarioId: String? = null
        private set

    fun iniciar(token: String, id: String) {
        accessToken = token
        usuarioId = id
    }

    fun cerrar() {
        accessToken = null
        usuarioId = null
    }
}
