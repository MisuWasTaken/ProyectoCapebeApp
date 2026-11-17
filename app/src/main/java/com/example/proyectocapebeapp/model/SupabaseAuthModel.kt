
package com.example.proyectocapebeapp.model

import com.google.gson.annotations.SerializedName

data class SupabaseAuthRequest(
    val email: String,
    val password: String
)

data class SupabaseRegistroRequest(
    val email: String,
    val password: String,
    val data: Map<String, String>
)

data class SupabaseAuthResponse(
    @SerializedName("access_token") val accessToken: String? = null,
    @SerializedName("refresh_token") val refreshToken: String? = null,
    @SerializedName("expires_in") val expiresIn: Long = 3600,
    val user: SupabaseUsuario? = null
)

data class SupabaseUsuario(
    val id: String,
    val email: String?
)

data class SupabasePerfil(
    val id: String,
    val nombre: String,
    val rol: String
)
