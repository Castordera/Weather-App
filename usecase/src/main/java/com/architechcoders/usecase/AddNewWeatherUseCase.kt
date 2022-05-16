package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import com.architechcoders.domain.Errors
import javax.inject.Inject

class AddNewWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Errors? {
        return repository.addNewWeatherLocation(city)
    }
}