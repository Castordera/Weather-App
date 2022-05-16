package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import javax.inject.Inject

class DeleteWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(cityId: Int) = repository.deleteWeather(cityId)
}