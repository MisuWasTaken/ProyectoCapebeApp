package com.example.proyectocapebeapp.data.api

import com.example.proyectocapebeapp.model.SupabaseAuthRequest
import com.example.proyectocapebeapp.model.SupabaseAuthResponse
import com.example.proyectocapebeapp.model.SupabasePerfil
import com.example.proyectocapebeapp.model.SupabaseRegistroRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseAuthApi {

    @POST("auth/v1/token")
    suspend fun iniciarSesion(
        @Query("grant_type") grantType: String = "password",
        @Body datos: SupabaseAuthRequest
    ): SupabaseAuthResponse

    @POST("auth/v1/signup")
    suspend fun registrar(
        @Body datos: SupabaseRegistroRequest
    ): SupabaseAuthResponse

    @GET("rest/v1/perfiles")
    suspend fun obtenerPerfil(
        @Header("Authorization") autorizacion: String,
        @Query("id") usuarioId: String,
        @Query("select") columnas: String = "id,nombre,rol"
    ): List<SupabasePerfil>
}
