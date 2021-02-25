package com.example.weather.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weather.mvvm.model.CityLocation

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeatherDetails(cityLocation: CityLocation)

    @Delete
    suspend fun deleteWeatherDetails(cityLocation: CityLocation)

    @Query("SELECT * FROM CityLocation")
    fun getWeatherList(): LiveData<List<CityLocation>>
}