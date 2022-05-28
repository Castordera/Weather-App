package com.architechcoders.domain

import java.time.Instant
import java.time.ZoneOffset

data class Weather(
    val id: Int,
    val cityName: String,
    val description: String,
    val icon: String,
    val temperature: Float,
    val feelsLikeTemperature: Float,
    val maxTemperature: Float,
    val minTemperature: Float,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Long,
    val sunset: Long,
    val windSpeed: Float,
    val timezone: Int,
    val timeRequested: Long,
    val isCurrentLocation: Boolean
)

val Weather.isDayTime: Boolean
get() {
    val zoneOffset = ZoneOffset.ofTotalSeconds(timezone)
    val reqTime = Instant.ofEpochMilli(timeRequested).atOffset(zoneOffset)
    val sunsetTime = Instant.ofEpochSecond(sunset).atOffset(zoneOffset)
    val sunriseTime = Instant.ofEpochSecond(sunrise).atOffset(zoneOffset)
    return reqTime.isBefore(sunsetTime) && reqTime.isAfter(sunriseTime)
}