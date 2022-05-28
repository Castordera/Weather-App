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
class AddNewWeatherUseCaseTest {

    private lateinit var repository: WeatherRepository
    private lateinit var useCase: AddNewWeatherUseCase

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        repository = mockk()
        useCase = AddNewWeatherUseCase(repository)
    }

    @Test
    fun `Call to request a new weather based on a city name`() = runTest {
        coEvery { repository.addNewWeatherLocation(any()) } returns null
        val response = useCase("random-city")
        assertNull(response)
        coVerify {
           repository.addNewWeatherLocation("random-city")
        }
    }

    @Test
    fun `Call to request a new weather based on a city name but response is an error`() = runTest {
        val mockResponse = Errors.Database.SaveFailed
        coEvery { repository.addNewWeatherLocation(any()) } returns mockResponse
        val response = useCase("random-city")
        assertEquals(mockResponse, response)
        coVerify {
            repository.addNewWeatherLocation("random-city")
        }
    }
}