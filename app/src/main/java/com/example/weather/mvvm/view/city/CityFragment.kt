package com.example.weather.mvvm.view.city

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.mvvm.model.CityLocation
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_home.*

class CityFragment : Fragment(R.layout.fragment_city) {

    private var cityLocation: CityLocation? = null

    companion object {
        private const val LOCATION_OBJ = "location"

        fun getInstance(cityLocation: CityLocation): CityFragment {
            val cityFragment = CityFragment()
            val bundle = Bundle()
            bundle.putSerializable(LOCATION_OBJ, cityLocation)
            cityFragment.arguments = bundle
            return cityFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {

        cityLocation = arguments?.getSerializable(LOCATION_OBJ) as CityLocation?

        cityLocation?.let { location ->
            val celsius = location.temp - 273.15
            txtTemp?.text = "${celsius.toInt()} \u2103"
            txtWeatherName?.setText(location.weatherName?:"")
            txtHumidity?.setText("Humidity ${location.humidity}%")
            txtWindSpeed?.setText("Wind ${location.windSpeed} km/hr")
        }
    }


}