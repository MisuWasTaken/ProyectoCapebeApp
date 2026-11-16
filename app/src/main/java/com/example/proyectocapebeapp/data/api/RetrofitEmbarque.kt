
package com.example.proyectocapebeapp.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitEmbarque {

    private const val BASE_URL = "http://10.0.2.2:8083/"
    val api: EmbarqueApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmbarqueApi::class.java)
    }
}
