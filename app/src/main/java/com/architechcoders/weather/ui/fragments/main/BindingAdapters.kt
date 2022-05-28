package com.architechcoders.weather.ui.fragments.main

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.architechcoders.domain.Weather
import com.architechcoders.weather.R

@BindingAdapter("items")
fun RecyclerView.setItems(items: List<Weather>?) {
    items?.also {
        (adapter as? WeatherAdapter)?.submitList(items)
    }
}

@BindingAdapter("temperature")
fun TextView.setTemperature(temperature: Float) {
    val temp = String.format("%.0f", temperature)
    text = context.getString(R.string.temperature_celsius_annotation, temp)
}

@BindingAdapter("bgDayTime")
fun View.setBackgroundDayTime(isDayTime: Boolean) {
    val color = if (isDayTime) R.color.day_color else R.color.night_color
    setBackgroundColor(ContextCompat.getColor(this.context, color))
}