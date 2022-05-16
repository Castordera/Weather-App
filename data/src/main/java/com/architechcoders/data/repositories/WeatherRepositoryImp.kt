package com.architechcoders.data.repositories

import com.architechcoders.data.datasource.WeatherLocalDataSource
import com.architechcoders.data.datasource.WeatherRemoteDataSource
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepositoryImp @Inject constructor(
    private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource,
    private val locationRepository: LocationRepository
): WeatherRepository {

    override fun getListOfWeathers(): Flow<List<Weather>> = localDataSource.getWeatherList()

    override suspend fun addNewWeatherLocation(city: String): Errors? {
        val weather = remoteDataSource.requestWeatherForCity(city)
        return weather.fold(
            ifLeft = { it },
            ifRight = { localDataSource.storeWeather(it) }
        )
    }

    override fun getGetWeatherById(cityId: Int): Flow<Weather> {
        return localDataSource.getWeatherByCityId(cityId)
    }

    override suspend fun updateWeatherCity(cityId: Int): Errors? {
        val weather = remoteDataSource.requestWeatherByCityId(cityId)
        return weather.fold(
            ifLeft = { it },
            ifRight = { localDataSource.updateWeather(it) }
        )
    }

    override suspend fun deleteWeather(cityId: Int): Errors? {
        return localDataSource.deleteWeatherById(cityId)
    }

    override suspend fun getLocationWeather(): Errors? {
        val location = locationRepository.getLastKnownLocation()
        if (location != null) {
            return remoteDataSource.requestWeatherForLocation(location).fold(
                ifLeft = { it },
                ifRight = { localDataSource.storeCurrentWeather(it) }
            )
        }
        return Errors.Unknown("Unable to get current location")
    }
}