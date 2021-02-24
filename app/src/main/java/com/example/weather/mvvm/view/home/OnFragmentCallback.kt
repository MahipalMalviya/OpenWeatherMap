package com.example.weather.mvvm.view.home

import com.example.weather.mvvm.model.CityLocation

interface OnFragmentCallback {
    fun onLaunchCityFragment(cityLocation: CityLocation)
    fun onLaunchMapFragment()
    fun onLaunchHelpFragment()
}