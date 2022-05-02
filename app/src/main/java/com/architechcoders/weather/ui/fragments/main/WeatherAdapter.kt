package com.architechcoders.weather.ui.fragments.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.architechcoders.domain.Weather
import com.architechcoders.weather.R
import com.architechcoders.weather.databinding.ItemWeatherBinding
import com.architechcoders.weather.ui.fragments.utils.basicDiffUtil

class WeatherAdapter(
    private val onClickWeather: (Weather) -> Unit
): ListAdapter<Weather, WeatherAdapter.WeatherViewHolder>(basicDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weatherItem = getItem(position)
        with(holder) {
            bind(weatherItem)
            itemView.setOnClickListener { onClickWeather(weatherItem) }
        }
    }

    inner class WeatherViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemWeatherBinding.bind(view)

        fun bind(weather: Weather) {
            binding.weather = weather
        }
    }
}