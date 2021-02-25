package com.example.weather.mvvm.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.database.dao.WeatherDatabase
import com.example.weather.mvvm.model.CityLocation
import kotlinx.android.synthetic.main.layout_city_location_items.view.*
import kotlinx.coroutines.*

class CityLocationAdapter(private val cityList:ArrayList<CityLocation>,
                          private var db:WeatherDatabase?,
    var onItemClicked: ((CityLocation) -> Unit)):
    RecyclerView.Adapter<CityLocationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        init {
            itemView.imgDelete.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    removeItem(adapterPosition)
                }
            }

            itemView.setOnClickListener {
                onItemClicked.invoke(cityList[adapterPosition])
            }
        }

        fun bindItem(cityLocation: CityLocation) {
            itemView.txtCityName.text = cityLocation.cityName
            itemView.txtLat.text = "Latitude : ${cityLocation.lat}"
            itemView.txtLng.text = "Longitude : ${cityLocation.lng}"
        }
    }

    private suspend fun removeItem(adapterPosition: Int) {
        GlobalScope.async {
            db?.weatherDao()?.deleteWeatherDetails(cityList[adapterPosition])
        }
        cityList.removeAt(adapterPosition)
        notifyItemRemoved(adapterPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_city_location_items,parent,false))

    override fun getItemCount(): Int = cityList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(cityList[position])
    }
}