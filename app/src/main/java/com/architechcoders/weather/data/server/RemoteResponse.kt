package com.architechcoders.weather.data.server

import com.google.gson.annotations.SerializedName
import java.time.Instant
import java.util.*
import com.architechcoders.domain.Weather as DomainWeather

data class RemoteResponse(
    val id: Int,
    @SerializedName("name") val cityName: String,
    val timezone: Int,
    val weather: List<Weather>,
    val main: Main,
    val wind: Wind,
    val dt: Long,
    val sys: Sys
)

data class Weather(
    val description: String,
    val icon: String
)

data class Main(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("feels_like") val feelLike: Float,
    @SerializedName("temp_min") val temperatureMin: Float,
    @SerializedName("temp_max") val temperatureMax: Float,
    val humidity: Int,
    val pressure: Int
)

data class Wind(
    val speed: Float
)

data class Sys(
    val sunrise: Long,
    val sunset: Long
)

fun RemoteResponse.toDomain(isCurrent: Boolean = false) = DomainWeather(
    if (isCurrent) 0 else id,
    cityName,
    weather[0].description.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() },
    "https://openweathermap.org/img/wn/${weather[0].icon}.png",
    main.temperature,
    main.feelLike,
    main.temperatureMax,
    main.temperatureMin,
    main.humidity,
    main.pressure,
    sys.sunrise,
    sys.sunset,
    wind.speed,
    timezone,
    Instant.now().toEpochMilli(),
    isCurrent
)