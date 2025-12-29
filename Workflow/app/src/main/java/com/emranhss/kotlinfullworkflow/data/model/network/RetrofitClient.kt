package com.emranhss.kotlinfullworkflow.data.model.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    //for emulator
    //private const val BASE_URL = "http://10.0.2.2:8085/" // api

    //wifi ip address
    private const val BASE_URL = "http://192.168.0.192:8085/" // api

    //my api
    //private const val BASE_URL = "http://localhost:8085/" // api

    //eta diea run korle crush kore(jodio eta postman a proper kaj kore)
    //private const val BASE_URL = "http://localhost:8085/api/users/save" // api

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}