package com.example.proyectocapebeapp.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitSupabaseAuth {

    private const val BASE_URL = "https://arwwmnqyjidfiuwypgsc.supabase.co/"
    private const val API_KEY = "sb_publishable_ipVyMa4LIzG7MNwWVLSJgA_L3KVpskM"

    private val cliente = OkHttpClient.Builder()
        .addInterceptor { cadena ->
            val peticion = cadena.request().newBuilder()
                .addHeader("apikey", API_KEY)
                .build()
            cadena.proceed(peticion)
        }
        .build()

    val api: SupabaseAuthApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SupabaseAuthApi::class.java)
    }
}
