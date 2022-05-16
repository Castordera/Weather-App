package com.architechcoders.data.repositories

import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getListOfWeathers(): Flow<List<Weather>>
    fun getGetWeatherById(cityId: Int): Flow<Weather>
    suspend fun updateWeatherCity(cityId: Int): Errors?
    suspend fun addNewWeatherLocation(city: String): Errors?
    suspend fun deleteWeather(cityId: Int): Errors?
    suspend fun getLocationWeather(): Errors?
}