package com.example.weather.mvvm.model

import java.io.Serializable

data class CityLocation(
    var cityName: String,
    var lat: Double,
    var lng: Double
): Serializable