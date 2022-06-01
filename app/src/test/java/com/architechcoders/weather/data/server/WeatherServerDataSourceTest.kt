package com.architechcoders.weather.data.server

import com.architechcoders.data.datasource.WeatherRemoteDataSource
import com.architechcoders.domain.Location
import com.architechcoders.weather.CoroutineTestRule
import com.architechcoders.weather.serverWeather
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
class WeatherServerDataSourceTest {

    private lateinit var weatherService: WeatherService
    private lateinit var remoteDataSource: WeatherRemoteDataSource

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        weatherService = mockk()
        remoteDataSource = WeatherServerDataSource(weatherService)
    }

    @Test
    fun `Request Weather for an specific city to server`() = runTest {
        val cityName = "Demo city"
        val mockResponse = serverWeather.copy(123456, cityName)
        coEvery { weatherService.getWeatherForCity(any(), any(), any()) } returns mockResponse
        val response = remoteDataSource.requestWeatherForCity(cityName)
        response.fold(
            ifLeft = { fail("Error not expected") },
            ifRight = {
                assertEquals(it.id, 123456)
                assertEquals(it.cityName, cityName)
                assertFalse(it.isCurrentLocation)
            }
        )
    }

    @Test
    fun `Request Weather for an specific city id to server`() = runTest {
        val cityName = "Demo city"
        val mockResponse = serverWeather.copy(123456789, cityName)
        coEvery { weatherService.getWeatherByCityId(any(), any(), any()) } returns mockResponse
        val response = remoteDataSource.requestWeatherByCityId(123456789)
        response.fold(
            ifLeft = { fail("Error not expected") },
            ifRight = {
                assertEquals(it.id, 123456789)
                assertEquals(it.cityName, cityName)
                assertFalse(it.isCurrentLocation)
            }
        )
    }

    @Test
    fun `Request Weather for an specific location`() = runTest {
        val cityName = "Demo city"
        val mockResponse = serverWeather.copy(123456789, cityName)
        coEvery { weatherService.getWeatherForLocation(any(), any(), any(), any()) } returns mockResponse
        val response = remoteDataSource.requestWeatherForLocation(Location(1.0, 2.0))
        response.fold(
            ifLeft = { fail("Error not expected") },
            ifRight = {
                assertEquals(it.id, 0)
                assertEquals(it.cityName, cityName)
                assertTrue(it.isCurrentLocation)
            }
        )
        coVerify {
            weatherService.getWeatherForLocation(1.0, 2.0, any(), any())
        }
    }
}