package com.architechcoders.weather.ui.fragments.main

import app.cash.turbine.test
import com.architechcoders.usecase.AddNewWeatherUseCase
import com.architechcoders.usecase.GetCurrentLocationWeatherUseCase
import com.architechcoders.usecase.GetWeathersUseCase
import com.architechcoders.weather.CoroutineTestRule
import com.architechcoders.weather.createFakeRepository
import com.architechcoders.weather.data.server.RemoteResponse
import com.architechcoders.weather.localWeather
import com.architechcoders.weather.serverWeather
import com.architechcoders.weather.ui.fragments.main.WeatherMainViewModel.UiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.architechcoders.weather.data.database.Weather as LocalWeather

@ExperimentalCoroutinesApi
class WeatherMainIntegrationTest {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Test
    fun `data is loaded from db eventually asks for location weather`() = runTest {
        val localData = listOf(localWeather.copy(id = 1), localWeather.copy(id = 2), localWeather.copy(id = 3))
        val viewModel = setUpViewModel(localData, serverWeather)
        viewModel.onLocationPermissionReady(true)
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(loading = true), awaitItem())
            var weathers = awaitItem().weatherList!!
            assertEquals(1, weathers[0].id)
            assertEquals(2, weathers[1].id)
            assertEquals(3, weathers[2].id)
            weathers = awaitItem().weatherList!!
            assertEquals(0, weathers[3].id)
            assertEquals("Local", weathers[3].cityName)
        }
    }

    @Test
    fun `data in db is empty therefore no data is expected`() = runTest {
        val viewModel = setUpViewModel(emptyList(), serverWeather)
        viewModel.onLocationPermissionReady(true)
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(loading = true), awaitItem())
            assertEquals(UiState(weatherList = emptyList()), awaitItem())
            val weathers = awaitItem().weatherList!!
            assertEquals(0, weathers[0].id)
        }
    }

    @Test
    fun `testing insertion of data`() = runTest {
        val viewModel = setUpViewModel(emptyList(), serverWeather)
        viewModel.onAddNewWeatherLocation("Demo city")
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(loading = true), awaitItem())
            assertEquals(UiState(weatherList = emptyList()), awaitItem())
            assertEquals(UiState(loading = true, weatherList = emptyList()), awaitItem())
            assertEquals(UiState(weatherList = emptyList()), awaitItem())
            val weathers = awaitItem().weatherList!!
            assertEquals("Demo city", weathers[0].cityName)
        }
    }

    private fun setUpViewModel(
        localData: List<LocalWeather> = emptyList(),
        remoteData: RemoteResponse
    ): WeatherMainViewModel {
        val fakeRepository = createFakeRepository(localData, remoteData)
        val getWeathersUseCase = GetWeathersUseCase(fakeRepository)
        val addNewWeatherUseCase = AddNewWeatherUseCase(fakeRepository)
        val getCurrentLocationWeatherUseCase = GetCurrentLocationWeatherUseCase(fakeRepository)
        return WeatherMainViewModel(
            getWeathersUseCase,
            addNewWeatherUseCase,
            getCurrentLocationWeatherUseCase
        )
    }
}