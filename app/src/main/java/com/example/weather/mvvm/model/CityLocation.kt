package com.example.weather.mvvm.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "CityLocation")
data class CityLocation (
    @PrimaryKey(autoGenerate = true) var _id:Int,
    @ColumnInfo(name = "city") var cityName: String?,
    @ColumnInfo(name = "lat") var lat: Double,
    @ColumnInfo(name = "lng") var lng: Double,
    @ColumnInfo(name = "weatherName") var weatherName:String?,
    @ColumnInfo(name = "humidity") var humidity: Int,
    @ColumnInfo(name = "pressure") var pressure: Int,
    @ColumnInfo(name = "temp") var temp: Double,
    @ColumnInfo(name = "windSpeed") var windSpeed: Double
): Serializable