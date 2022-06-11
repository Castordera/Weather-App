package com.architechcoders.weather

import com.architechcoders.data.repositories.LocationRepository
import com.architechcoders.data.repositories.WeatherRepository
import com.architechcoders.data.repositories.WeatherRepositoryImp
import com.architechcoders.domain.Location
import com.architechcoders.weather.data.database.WeatherDao
import com.architechcoders.weather.data.database.WeatherRoomDataSource
import com.architechcoders.weather.data.server.RemoteResponse
import com.architechcoders.weather.data.server.WeatherServerDataSource
import com.architechcoders.weather.data.server.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import com.architechcoders.weather.data.database.Weather as LocalWeather

fun createFakeRepository(
    localData: List<LocalWeather>,
    remoteData: RemoteResponse
): WeatherRepository {
    val localDataSource = WeatherRoomDataSource(FakeWeatherDao(localData))
    val remoteDataSource = WeatherServerDataSource(FakeWeatherService(remoteData))
    val locationRepository = FakeLocationRepository()
    return WeatherRepositoryImp(
        localDataSource,
        remoteDataSource,
        locationRepository
    )
}

class FakeWeatherDao(localData: List<LocalWeather>): WeatherDao {

    private val weathers = MutableStateFlow(localData)
    private val singleWeather = MutableStateFlow(localData[0])

    override fun getAllWeathers(): Flow<List<LocalWeather>> {
        return weathers
    }

    override fun getWeatherCityById(id: Int): Flow<LocalWeather> {
        return singleWeather
    }

    override suspend fun insertWeather(weather: LocalWeather) {
        weathers.value += weather
    }

    override suspend fun insertLocalWeather(weather: LocalWeather) {
        weathers.value += weather.copy(cityName = "Local")
    }

    override suspend fun updateWeather(weather: LocalWeather) {
        singleWeather.value = weather.copy(id = 123456, cityName = "Updated")
    }

    override suspend fun deleteWeather(id: Int) {
        return
    }
}

class FakeWeatherService(
    private val remoteData: RemoteResponse
): WeatherService {
    override suspend fun getWeatherForLocation(
        lat: Double,
        lng: Double,
        units: String,
        apiKey: String
    ): RemoteResponse {
        return remoteData
    }

    override suspend fun getWeatherForCity(
        cityName: String,
        units: String,
        apiKey: String
    ): RemoteResponse {
        return remoteData.copy(cityName = cityName)
    }

    override suspend fun getWeatherByCityId(
        cityId: Int,
        units: String,
        apiKey: String
    ): RemoteResponse {
        return remoteData.copy(id = cityId)
    }
}

class FakeLocationRepository: LocationRepository {
    override suspend fun getLastKnownLocation(): Location {
        return Location(0.0, 0.0)
    }
}