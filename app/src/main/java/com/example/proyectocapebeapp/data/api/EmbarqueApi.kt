
package com.example.proyectocapebeapp.data.api

import com.example.proyectocapebeapp.model.EmbarqueResponse
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface EmbarqueApi {

    @Multipart
    @POST("api/v1/embarques")
    suspend fun registrar(
        @Part("data") data: RequestBody
    ): EmbarqueResponse

    @GET("api/v1/embarques/vuelo/{vueloId}")
    suspend fun obtenerPorVuelo(
        @Path("vueloId") vueloId: Long
    ): List<EmbarqueResponse>

    @PATCH("api/v1/embarques/{id}/estado")
    suspend fun cambiarEstado(
        @Path("id") id: Long,
        @Body datos: Map<String, String>
    ): EmbarqueResponse
}
