package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import com.architechcoders.domain.Errors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetCurrentLocationWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var getCurrentLocationWeatherUseCase: GetCurrentLocationWeatherUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        repository = mockk()
        getCurrentLocationWeatherUseCase = GetCurrentLocationWeatherUseCase(repository)
    }

    @Test
    fun `Call repository to get current location weather`() = runTest {
        coEvery { repository.getLocationWeather() } returns null
        val response = getCurrentLocationWeatherUseCase()
        assertNull(response)
        coVerify {
            repository.getLocationWeather()
        }
    }

    @Test
    fun `Call repository to get current location weather with an error`() = runTest {
        val mockResponse = Errors.Unknown("Error")
        coEvery { repository.getLocationWeather() } returns mockResponse
        val response = getCurrentLocationWeatherUseCase()
        assertEquals(response, mockResponse)
        coVerify {
            repository.getLocationWeather()
        }
    }
}