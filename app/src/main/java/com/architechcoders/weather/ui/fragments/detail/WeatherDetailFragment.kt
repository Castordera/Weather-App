package com.architechcoders.weather.ui.fragments.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.architechcoders.weather.R
import com.architechcoders.weather.databinding.FragmentDetailWeatherBinding

class WeatherDetailFragment: Fragment(R.layout.fragment_detail_weather) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        val binding = FragmentDetailWeatherBinding.bind(view)
    }
}