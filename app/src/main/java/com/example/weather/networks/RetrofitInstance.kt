package com.example.weather.networks

import android.content.Context
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    const val WEATHER_API_KEY = "6c9f5d2121eb5ffb9cf8bec31a56b076"

    fun createAPI(context: Context): APIService? {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(APIService::class.java)
    }
}