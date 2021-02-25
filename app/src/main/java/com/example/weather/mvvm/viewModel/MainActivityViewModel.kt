package com.example.weather.mvvm.viewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.mvvm.model.WeatherResponse
import com.example.weather.networks.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val weatherLiveData = MutableLiveData<WeatherResponse>()

    fun getWeatherLiveData(): LiveData<WeatherResponse> {
        return weatherLiveData
    }

    fun getCurrentWeatherMetadata(context: Context, cityName: String) {
        val apiService = RetrofitInstance.createAPI(context)
        val apiCall = apiService?.getCityCurrentWeather(cityName, RetrofitInstance.WEATHER_API_KEY)
        apiCall?.enqueue(object :Callback<WeatherResponse> {
            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful && response.code() == 200 && response.body() != null) {
                    weatherLiveData.postValue(response.body())
                }
            }

        })
    }
}