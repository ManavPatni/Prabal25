package com.devmnv.prabaladmin.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.devmnv.in/prabal/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
