package com.architechcoders.domain

data class Weather(
    val cityName: String,
    val description: String,
    val temperature: Float,
    val feelsLikeTemperature: Float,
    val maxTemperature: Float,
    val minTemperature: Float,
    val isCurrentLocation: Boolean
)
