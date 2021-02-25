package com.example.weather.mvvm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Main (

	@SerializedName("temp") @Expose val temp : Double,
	@SerializedName("feels_like") @Expose val feels_like : Double,
	@SerializedName("temp_min") @Expose val temp_min : Double,
	@SerializedName("temp_max") @Expose val temp_max : Double,
	@SerializedName("pressure") @Expose val pressure : Int,
	@SerializedName("humidity") @Expose val humidity : Int
)