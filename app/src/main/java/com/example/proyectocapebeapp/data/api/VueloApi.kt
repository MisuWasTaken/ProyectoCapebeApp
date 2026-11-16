package com.example.proyectocapebeapp.data.api

import com.example.proyectocapebeapp.model.VueloRequest
import com.example.proyectocapebeapp.model.VueloResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface VueloApi {

    @GET("api/v1/vuelos")
    suspend fun obtenerTodos(): List<VueloResponse>

    @POST("api/v1/vuelos")
    suspend fun crear(@Body request: VueloRequest): VueloResponse
}