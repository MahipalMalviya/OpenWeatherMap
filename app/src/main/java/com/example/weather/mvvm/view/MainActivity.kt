package com.example.weather.mvvm.view

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.database.dao.WeatherDatabase
import com.example.weather.mvvm.model.CityLocation
import com.example.weather.mvvm.model.WeatherResponse
import com.example.weather.mvvm.view.city.CityFragment
import com.example.weather.mvvm.view.help.HelpFragment
import com.example.weather.mvvm.view.home.HomeFragment
import com.example.weather.mvvm.view.home.OnFragmentCallback
import com.example.weather.mvvm.viewModel.MainActivityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnFragmentCallback, OnMapReadyCallback {

    private var mMap:GoogleMap? = null
    private var markerList = ArrayList<LatLng>()
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var weatherDatabase: WeatherDatabase? = null

    private val PERMISSION_CODE = 101
    private var permsList = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActivityViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(MainActivityViewModel::class.java)
        weatherDatabase = WeatherDatabase.getInstance(this)

        launchFragment(HomeFragment.getInstance().attachCallback(this),false,HomeFragment::class.java.name)

        if (!hasPermissions(permsList)) {
            ActivityCompat.requestPermissions(this,permsList,PERMISSION_CODE)
        }

        mainActivityViewModel.getWeatherLiveData().observe(this,
            Observer<WeatherResponse> {
                it?.let { weatherResponse ->
                    GlobalScope.launch {
                        async(Dispatchers.IO) {
                            val cityLocation = getCityLocationObject(weatherResponse)
                            weatherDatabase?.weatherDao()?.addWeatherDetails(cityLocation)
                        }
                    }
                }
            })
    }

    private fun getCityLocationObject(weatherResponse: WeatherResponse): CityLocation {
        var weatherName:String? = null
        if (weatherResponse.weather?.size?:0 > 0) {
            weatherName =  weatherResponse.weather?.get(0)?.description
        }
        val cityLocation = CityLocation(
            _id = weatherResponse.id,
            cityName = weatherResponse.name,
            lat = weatherResponse.coord?.lat?:0.0,
            lng = weatherResponse.coord?.lon?:0.0,
            weatherName = weatherName,
            humidity = weatherResponse.main?.humidity?:0,
            pressure = weatherResponse.main?.pressure?:0,
            temp = weatherResponse.main?.temp?:0.0,
            windSpeed = weatherResponse.wind?.speed?:0.0
        )
        return cityLocation
    }

    override fun onLaunchCityFragment(cityLocation: CityLocation) {
        launchFragment(CityFragment.getInstance(cityLocation),true,CityFragment::class.java.name)
    }

    override fun onLaunchMapFragment() {
        val supportFragment = SupportMapFragment.newInstance()
        launchFragment(supportFragment,true,SupportMapFragment::class.java.name)
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.homeContainer) as SupportMapFragment?
        supportFragment?.getMapAsync(this)
    }

    override fun onLaunchHelpFragment() {

    }

    override fun onMapReady(map: GoogleMap?) {
        mMap = map
        mMap?.setOnMapClickListener { latLng ->
            latLng?.let {
                markerList.add(it)
                mMap?.clear()
                val cityName = getCityName(it)
                mMap?.addMarker(MarkerOptions().position(it).title(cityName))
                mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(it,12.0f))

                cityName?.let {
                    mainActivityViewModel.getCurrentWeatherMetadata(this, it)
                }
            }
        }
    }

    private fun getCityName(latLng:LatLng): String? {
        val geoCoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address>? = geoCoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses?.size?:0 > 0) {
            return addresses?.get(0)?.subAdminArea
        }
        return null
    }

    private fun launchFragment(fragment:Fragment, addToBackStack:Boolean, tagName:String) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(R.id.homeContainer,fragment,tagName)

        if (addToBackStack) {
            transaction.addToBackStack(tagName)
        }
        transaction.commit()
    }

    fun hasPermissions(permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.isNotEmpty()) {
                val isFineLocation = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val isCoarseLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED

                if (!isFineLocation || !isCoarseLocation) {
                    ActivityCompat.requestPermissions(this,permsList,PERMISSION_CODE)
                    return
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.helpMenu -> {
                launchFragment(HelpFragment.getInstance(),true,HelpFragment::class.java.name)
            }
        }
        return true
    }

}