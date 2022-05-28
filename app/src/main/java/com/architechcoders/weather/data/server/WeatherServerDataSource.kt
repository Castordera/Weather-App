package com.architechcoders.weather.data.server

import arrow.core.Either
import com.architechcoders.data.datasource.WeatherRemoteDataSource
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Location
import com.architechcoders.domain.Weather
import com.architechcoders.weather.BuildConfig
import com.architechcoders.weather.data.utils.call
import javax.inject.Inject

class WeatherServerDataSource @Inject constructor(
    private val weatherService: WeatherService
): WeatherRemoteDataSource {

    override suspend fun requestWeatherForCity(city: String): Either<Errors, Weather> {
        return call {
            weatherService
                .getWeatherForCity(cityName = city, apiKey = BuildConfig.API_KEY)
                .toDomain()
        }
    }

    override suspend fun requestWeatherByCityId(cityId: Int): Either<Errors, Weather> {
        return call {
            weatherService
                .getWeatherByCityId(cityId = cityId, apiKey = BuildConfig.API_KEY)
                .toDomain()
        }
    }

    override suspend fun requestWeatherForLocation(location: Location): Either<Errors, Weather> {
        return call {
            weatherService
                .getWeatherForLocation(location.lat, location.lng, apiKey = BuildConfig.API_KEY)
                .toDomain(isCurrent = true)
        }
    }
}