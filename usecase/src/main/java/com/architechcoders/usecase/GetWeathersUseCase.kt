package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import javax.inject.Inject

class GetWeathersUseCase @Inject constructor(private val repository: WeatherRepository) {

    operator fun invoke() = repository.getListOfWeathers()
}