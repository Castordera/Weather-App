package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import com.architechcoders.domain.Weather
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetWeathersUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var getWeathersUseCase: GetWeathersUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        repository = mockk()
        getWeathersUseCase = GetWeathersUseCase(repository)
    }

    @Test
    fun `Get a list of weathers`() = runTest {
        val mockResponse = listOf(mockWeather().copy(id = 1), mockWeather().copy(id = 2))
        every { repository.getListOfWeathers() } returns flowOf(mockResponse)
        getWeathersUseCase().collect {
            assertEquals(mockResponse, it)
        }
        verify {
            repository.getListOfWeathers()
        }
    }

    private fun mockWeather(): Weather = Weather(
        0,
        "",
        "",
        "",
        0f,
        0f,
        0f,
        0f,
        0,
        0,
        0L,
        0L,
        0f,
        0,
        0L,
        false
    )
}