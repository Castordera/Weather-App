package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import javax.inject.Inject

class GetCurrentLocationWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke() = repository.getLocationWeather()
}