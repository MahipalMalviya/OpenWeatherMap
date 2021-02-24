package com.example.weather.mvvm.view

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.mvvm.model.CityLocation
import com.example.weather.mvvm.view.city.CityFragment
import com.example.weather.mvvm.view.home.HomeFragment
import com.example.weather.mvvm.view.home.OnFragmentCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnFragmentCallback, OnMapReadyCallback {

    private var mMap:GoogleMap? = null
    private var markerList = ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        launchFragment(HomeFragment.getInstance(),false,HomeFragment::class.java.name)
    }

    private fun launchFragment(fragment:Fragment, addToBackStack:Boolean, tagName:String) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.homeContainer,fragment,tagName)

        if (addToBackStack) {
            transaction.addToBackStack(tagName)
        }
        transaction.commit()
    }


    override fun onLaunchCityFragment(cityLocation: CityLocation) {
        launchFragment(CityFragment.getInstance(cityLocation),true,CityFragment::class.java.name)
    }

    override fun onLaunchMapFragment() {
        launchFragment(SupportMapFragment.newInstance(),true,SupportMapFragment::class.java.name)
        val mapFragment = supportFragmentManager
            .findFragmentByTag(SupportMapFragment::class.java.name) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onLaunchHelpFragment() {

    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map
        mMap?.setOnMapClickListener {
            markerList.add(it)
            mMap?.clear()
            val cityName = getCityName(it)
            mMap?.addMarker(MarkerOptions().position(it).title(cityName))
        }
    }

    private fun getCityName(latLng:LatLng): String {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        return addresses[0].getAddressLine(0)
//        val stateName = addresses[0].getAddressLine(1)
//        val countryName = addresses[0].getAddressLine(2)
    }
}