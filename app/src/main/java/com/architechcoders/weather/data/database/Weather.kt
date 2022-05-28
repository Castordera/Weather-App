package com.architechcoders.weather.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.architechcoders.domain.Weather as DomainWeather

@Entity
data class Weather(
    @PrimaryKey val id: Int,
    val cityName: String,
    val description: String,
    val temperature: Float,
    val icon: String,
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

fun Weather.toDomainModel(): DomainWeather = DomainWeather(
    id,
    cityName,
    description,
    icon,
    temperature,
    feelsLikeTemperature,
    maxTemperature,
    minTemperature,
    humidity,
    pressure,
    sunrise,
    sunset,
    windSpeed,
    timezone,
    timeRequested,
    isCurrentLocation
)

fun DomainWeather.toEntity() = Weather(
    id,
    cityName,
    description,
    temperature,
    icon,
    feelsLikeTemperature,
    maxTemperature,
    minTemperature,
    humidity,
    pressure,
    sunrise,
    sunset,
    windSpeed,
    timezone,
    timeRequested,
    isCurrentLocation
)