package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import com.architechcoders.domain.Weather
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetWeatherByIdUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var getWeatherByIdUseCase: GetWeatherByIdUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        repository = mockk()
        getWeatherByIdUseCase = GetWeatherByIdUseCase(repository)
    }

    @Test
    fun `Get flow of weather by city Id`() = runTest {
        val weatherMock = mockk<Weather>()
        every { repository.getGetWeatherById(any()) } returns flowOf(weatherMock)
        getWeatherByIdUseCase(123).collect {
            assertEquals(it, weatherMock)
        }
        verify {
            repository.getGetWeatherById(123)
        }
    }
}