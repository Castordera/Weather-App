package com.architechcoders.usecase

import com.architechcoders.data.repositories.WeatherRepository
import com.architechcoders.domain.Errors
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UpdateCityWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var updateCityWeatherUseCase: UpdateCityWeatherUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        repository = mockk()
        updateCityWeatherUseCase = UpdateCityWeatherUseCase(repository)
    }

    @Test
    fun `Update weather by city id`() = runTest {
        coEvery { repository.updateWeatherCity(any()) } returns null
        val response = updateCityWeatherUseCase(123)
        assertNull(response)
        coVerify {
            repository.updateWeatherCity(123)
        }
    }

    @Test
    fun `Update weather by city id with an error`() = runTest {
        val mockResponse = Errors.Unknown("Error")
        coEvery { repository.updateWeatherCity(any()) } returns mockResponse
        val response = updateCityWeatherUseCase(123)
        assertEquals(mockResponse, response)
        coVerify {
            repository.updateWeatherCity(123)
        }
    }
}