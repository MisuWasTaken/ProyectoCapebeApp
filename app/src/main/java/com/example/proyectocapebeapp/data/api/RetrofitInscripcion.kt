package com.example.proyectocapebeapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInscripcion {

    private const val BASE_URL = "http://10.0.2.2:8082/"

    val api: InscripcionApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InscripcionApi::class.java)
    }
}