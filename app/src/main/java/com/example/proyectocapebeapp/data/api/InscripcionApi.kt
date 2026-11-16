
package com.example.proyectocapebeapp.data.api

import com.example.proyectocapebeapp.model.InscripcionRequest
import com.example.proyectocapebeapp.model.InscripcionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InscripcionApi {

    @POST("api/v1/inscripciones")
    suspend fun crear(
        @Body request: InscripcionRequest
    ): InscripcionResponse

    @GET("api/v1/inscripciones/usuario")
    suspend fun obtenerPorEmail(
        @Query("email") email: String
    ): List<InscripcionResponse>

    @GET("api/v1/inscripciones/vuelo/{vueloId}/cantidad")
    suspend fun contarPorVuelo(
        @Path("vueloId") vueloId: Long
    ): Int

    @GET("api/v1/inscripciones/vuelo/{vueloId}")
    suspend fun obtenerPorVuelo(
        @Path("vueloId") vueloId: Long
    ): List<InscripcionResponse>

    @PATCH("api/v1/inscripciones/{id}/vuelo")
    suspend fun cambiarVuelo(
        @Path("id") id: Long,
        @Body datos: Map<String, Long>
    ): InscripcionResponse
}
