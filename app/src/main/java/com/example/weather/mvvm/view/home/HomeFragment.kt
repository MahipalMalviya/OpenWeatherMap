package com.example.weather.mvvm.view.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.mvvm.model.CityLocation
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment: Fragment(R.layout.fragment_home) {

    private lateinit var mCityLocationAdapter: CityLocationAdapter
    private var cityList = ArrayList<CityLocation>()

    private var onFragmentCallback: OnFragmentCallback? = null

    fun attachCallback(onFragmentCallback: OnFragmentCallback) {
        this.onFragmentCallback = onFragmentCallback
    }

    companion object {
        fun getInstance() : HomeFragment {
            val  homeFragment = HomeFragment()
            val bundle = Bundle()
            homeFragment.arguments = bundle
            return homeFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    private fun initViews() {
        rvCityLocation.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
        mCityLocationAdapter = CityLocationAdapter(cityList) { cityLocation ->
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
    }



}