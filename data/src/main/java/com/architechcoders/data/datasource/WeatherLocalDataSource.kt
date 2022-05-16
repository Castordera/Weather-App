package com.architechcoders.data.datasource

import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherLocalDataSource {
    fun getWeatherList(): Flow<List<Weather>>
    fun getWeatherByCityId(cityId: Int): Flow<Weather>
    suspend fun storeWeather(weather: Weather): Errors?
    suspend fun updateWeather(weather: Weather): Errors?
    suspend fun deleteWeatherById(cityId: Int): Errors?
    suspend fun storeCurrentWeather(weather: Weather): Errors?
}