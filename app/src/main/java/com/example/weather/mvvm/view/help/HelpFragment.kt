package com.example.weather.mvvm.view.help

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.mvvm.model.CityLocation
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.android.synthetic.main.fragment_home.*

class HelpFragment : Fragment(R.layout.fragment_help) {

    companion object {
        fun getInstance(): HelpFragment {
            val helpFragment = HelpFragment()
            val bundle = Bundle()
            helpFragment.arguments = bundle
            return helpFragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews() {

    }


}