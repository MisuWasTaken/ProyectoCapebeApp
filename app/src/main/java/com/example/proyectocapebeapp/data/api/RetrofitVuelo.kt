
package com.example.proyectocapebeapp.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitVuelo {

    private const val BASE_URL = "https://capebe-vuelos-v8bi.onrender.com/"

    private val cliente = OkHttpClient.Builder()
        .addInterceptor { cadena ->
            val token = SesionSupabase.tokenVigente()
            val peticion = cadena.request().newBuilder()

            if (token != null) {
                peticion.header("Authorization", "Bearer $token")
            }

            cadena.proceed(peticion.build())
        }
        .build()

    val api: VueloApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(VueloApi::class.java)
    }
}
