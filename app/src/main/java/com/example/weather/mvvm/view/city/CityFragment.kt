package com.example.weather.mvvm.view.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.mvvm.model.CityLocation
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() {

        cityLocation = arguments?.getSerializable(LOCATION_OBJ) as CityLocation?
    }


}