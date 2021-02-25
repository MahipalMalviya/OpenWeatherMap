package com.example.weather.mvvm.view.home

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.database.dao.WeatherDatabase
import com.example.weather.mvvm.model.CityLocation
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var mCityLocationAdapter: CityLocationAdapter
    private var cityList = ArrayList<CityLocation>()

    private var onFragmentCallback: OnFragmentCallback? = null
    private var weatherDatabase: WeatherDatabase? = null

    fun attachCallback(onFragmentCallback: OnFragmentCallback): HomeFragment {
        this.onFragmentCallback = onFragmentCallback
        return this
    }

    companion object {
        fun getInstance() : HomeFragment {
            val  homeFragment = HomeFragment()
            val bundle = Bundle()
            homeFragment.arguments = bundle
            return homeFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {
        weatherDatabase = WeatherDatabase.getInstance(requireContext())

        rvCityLocation.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        mCityLocationAdapter = CityLocationAdapter(cityList,weatherDatabase) { cityLocation ->
            onFragmentCallback?.onLaunchCityFragment(cityLocation)
        }
        rvCityLocation.adapter = mCityLocationAdapter

        btnAddLocation.setOnClickListener {
            onFragmentCallback?.onLaunchMapFragment()
        }
    }

    override fun onStart() {
        super.onStart()

        // fetch from database bookmarked location
        GlobalScope.launch {
            observeWeatherDao()
        }
    }

    private suspend fun observeWeatherDao() = withContext(Dispatchers.Main) {
        weatherDatabase?.weatherDao()?.getWeatherList()?.observe(viewLifecycleOwner, Observer {
            if (cityList.isNotEmpty()) {
                cityList.clear()
            }
            if (it?.isEmpty() == true) {
                Toast.makeText(requireContext(),"Please add location",Toast.LENGTH_SHORT).show()
                return@Observer
            }
            it?.let { locationList ->
                cityList.addAll(locationList)
                mCityLocationAdapter.notifyDataSetChanged()
            }
        })
    }


}