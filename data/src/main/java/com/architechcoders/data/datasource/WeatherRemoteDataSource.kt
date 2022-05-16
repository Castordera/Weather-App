package com.architechcoders.data.datasource

import arrow.core.Either
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Location
import com.architechcoders.domain.Weather

interface WeatherRemoteDataSource {
    suspend fun requestWeatherForCity(city: String): Either<Errors, Weather>
    suspend fun requestWeatherByCityId(cityId: Int): Either<Errors, Weather>
    suspend fun requestWeatherForLocation(location: Location): Either<Errors, Weather>
}