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
class DeleteWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var deleteWeatherUseCase: DeleteWeatherUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        repository = mockk()
        deleteWeatherUseCase = DeleteWeatherUseCase(repository)
    }

    @Test
    fun `Call repository to delete a weather by city id`() = runTest {
        coEvery { repository.deleteWeather(any()) } returns null
        val response = deleteWeatherUseCase(12345678)
        assertNull(response)
        coVerify {
            repository.deleteWeather(12345678)
        }
    }

    @Test
    fun `Call repository to delete a weather by city id with an error as response`() = runTest {
        val mockResponse = Errors.Unknown("Not defined")
        coEvery { repository.deleteWeather(any()) } returns mockResponse
        val response = deleteWeatherUseCase(12345678)
        assertEquals(mockResponse, response)
        coVerify {
            repository.deleteWeather(12345678)
        }
    }
}