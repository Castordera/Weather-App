package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import com.architechcoders.domain.Weather
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWeatherByIdUseCase @Inject constructor(
    private val repository: WeatherRepository
){
    operator fun invoke(cityId: Int): Flow<Weather> {
        return repository.getGetWeatherById(cityId)
    }
}