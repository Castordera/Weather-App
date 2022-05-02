package com.architechcoders.weather.ui.fragments.main

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.architechcoders.domain.Weather

@BindingAdapter("items")
fun RecyclerView.setItems(items: List<Weather>?) {
    items?.also {
        (adapter as? WeatherAdapter)?.submitList(items)
    }
}