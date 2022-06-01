package com.architechcoders.weather

import com.architechcoders.weather.data.server.*
import com.architechcoders.domain.Weather as WeatherDomain
import com.architechcoders.weather.data.database.Weather as WeatherEntity

val localWeather = WeatherEntity(
    id = 0,
    cityName = "",
    description = "",
    temperature = 0f,
    icon = "",
    feelsLikeTemperature = 0f,
    maxTemperature = 0f,
    minTemperature = 0f,
    humidity = 0,
    pressure = 0,
    sunrise = 0L,
    sunset = 0L,
    windSpeed = 0f,
    timezone = 0,
    timeRequested = 0L,
    isCurrentLocation = false
)

val domainWeather = WeatherDomain(
    id = 0,
    cityName = "",
    description = "",
    temperature = 0f,
    icon = "",
    feelsLikeTemperature = 0f,
    maxTemperature = 0f,
    minTemperature = 0f,
    humidity = 0,
    pressure = 0,
    sunrise = 0L,
    sunset = 0L,
    windSpeed = 0f,
    timezone = 0,
    timeRequested = 0L,
    isCurrentLocation = false
)

val domainList = listOf(
    domainWeather.copy(id = 1),
    domainWeather.copy(id = 2)
)

val serverWeather = RemoteResponse(
    id = 0,
    cityName = "",
    timezone = 0,
    weather = listOf(
        Weather(
            description = "Description",
            icon = ""
        )
    ),
    main = Main(
        temperature = 0f,
        feelLike = 0f,
        temperatureMin = 0f,
        temperatureMax = 0f,
        humidity = 0,
        pressure = 0
    ),
    wind = Wind (
        speed = 0f
    ),
    dt = 0L,
    sys = Sys(
        sunrise = 0L,
        sunset = 0L
    )
)