package com.example.weather.networks

import com.example.weather.mvvm.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("weather")
    fun getCityCurrentWeather(@Query("q") cityName:String,@Query("appid") appId:String): Call<WeatherResponse>?
}