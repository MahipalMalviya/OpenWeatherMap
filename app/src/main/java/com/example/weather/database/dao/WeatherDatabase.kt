package com.example.weather.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weather.mvvm.model.CityLocation

@Database(entities = [CityLocation::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao?

    companion object {
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            if(instance == null) {
                instance = Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java,
                    "weather_db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}