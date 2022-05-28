package com.architechcoders.weather.data.database

import com.architechcoders.data.datasource.WeatherLocalDataSource
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import com.architechcoders.weather.data.utils.call
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRoomDataSource @Inject constructor(
    private val weatherDao: WeatherDao
): WeatherLocalDataSource {

    override fun getWeatherList(): Flow<List<Weather>> {
        return weatherDao.getAllWeathers().map { list -> list.map { it.toDomainModel() } }
    }

    override suspend fun storeWeather(weather: Weather): Errors? {
        return call { weatherDao.insertWeather(weather.toEntity()) }.fold(
            ifLeft = { it },
            ifRight = { null }
        )
    }

    override fun getWeatherByCityId(cityId: Int): Flow<Weather> {
        return weatherDao.getWeatherCityById(cityId).map { it.toDomainModel() }
    }

    override suspend fun updateWeather(weather: Weather): Errors? {
        return call { weatherDao.updateWeather(weather.toEntity()) }.fold(
            ifLeft = { it },
            ifRight = { null }
        )
    }

    override suspend fun deleteWeatherById(cityId: Int): Errors? {
        return call { weatherDao.deleteWeather(cityId) }.fold(
            ifLeft = { it },
            ifRight = { null }
        )
    }

    override suspend fun storeCurrentWeather(weather: Weather): Errors? {
        return call { weatherDao.insertLocalWeather(weather.toEntity()) }.fold(
            ifLeft = { it },
            ifRight = { null }
        )
    }
}