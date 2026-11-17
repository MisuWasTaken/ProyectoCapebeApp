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
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.Call

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

    @Headers("x-upsert: true")
    @POST("storage/v1/object/identificaciones-capebe/{archivo}")
    suspend fun subirFoto(
        @Header("Authorization") autorizacion: String,
        @Path("archivo") archivo: String,
        @Body foto: RequestBody
    ): Response<ResponseBody>

    @GET("storage/v1/object/authenticated/identificaciones-capebe/{archivo}")
    suspend fun obtenerFoto(
        @Header("Authorization") autorizacion: String,
        @Path("archivo") archivo: String
    ): Response<ResponseBody>

    @POST("auth/v1/token?grant_type=refresh_token")
    fun renovarSesion(
        @Body datos: Map<String, String>
    ): Call<SupabaseAuthResponse>

}
