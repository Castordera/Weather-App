package com.architechcoders.weather.ui.fragments.main

import app.cash.turbine.test
import com.architechcoders.domain.Errors
import com.architechcoders.usecase.AddNewWeatherUseCase
import com.architechcoders.usecase.GetCurrentLocationWeatherUseCase
import com.architechcoders.usecase.GetWeathersUseCase
import com.architechcoders.weather.CoroutineTestRule
import com.architechcoders.weather.domainList
import com.architechcoders.weather.ui.fragments.main.WeatherMainViewModel.UiState
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherMainViewModelTest {

    private lateinit var getWeatherUseCase: GetWeathersUseCase
    private lateinit var addNewWeatherUseCase: AddNewWeatherUseCase
    private lateinit var getCurrentLocationWeatherUseCase: GetCurrentLocationWeatherUseCase
    private lateinit var viewModel: WeatherMainViewModel

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        getWeatherUseCase = mockk()
        addNewWeatherUseCase = mockk()
        getCurrentLocationWeatherUseCase = mockk()
        every { getWeatherUseCase() } returns flowOf(domainList)
        viewModel = WeatherMainViewModel(
            getWeatherUseCase,
            addNewWeatherUseCase,
            getCurrentLocationWeatherUseCase
        )
    }

    @Test
    fun `Init configuration performed`() = runTest {
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(loading = true), awaitItem())
            assertEquals(UiState(weatherList = domainList), awaitItem())
        }
        verify(exactly = 1) {
            getWeatherUseCase()
        }
    }

    @Test
    fun `Add a new weather location by city name`()  = runTest {
        coEvery { addNewWeatherUseCase(any()) } returns null
        viewModel.onAddNewWeatherLocation("city name")
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(loading = true), awaitItem())
            assertEquals(UiState(weatherList = domainList), awaitItem())
            assertEquals(UiState(loading = true, weatherList = domainList), awaitItem())
            assertEquals(UiState(weatherList = domainList), awaitItem())
        }
        coVerify { addNewWeatherUseCase("city name") }
    }

    @Test
    fun `Add a new weather location and return an error`()  = runTest {
        val error = Errors.Unknown("Error")
        coEvery { addNewWeatherUseCase(any()) } returns error
        viewModel.onAddNewWeatherLocation("city name")
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(loading = true), awaitItem())
            assertEquals(UiState(weatherList = domainList), awaitItem())
            assertEquals(UiState(loading = true, weatherList = domainList), awaitItem())
            assertEquals(UiState(weatherList = domainList, error = error), awaitItem())
        }
        coVerify { addNewWeatherUseCase("city name") }
    }

    @Test
    fun `Location permission ready`() = runTest {
        coEvery { getCurrentLocationWeatherUseCase() } returns null
        viewModel.onLocationPermissionReady(false)
        viewModel.onLocationPermissionReady(true)
        viewModel.uiState.drop(3).test {
            assertEquals(UiState(weatherList = domainList, permissionGranted = false), awaitItem())
            assertEquals(UiState(weatherList = domainList), awaitItem())
        }
        coVerify(exactly = 1) {
            getCurrentLocationWeatherUseCase()
        }
    }

    @Test
    fun `Test error handling`() = runTest {
        val error = Errors.Unknown("Error")
        coEvery { getCurrentLocationWeatherUseCase() } returns error
        viewModel.onLocationPermissionReady(true)
        viewModel.uiState.drop(3).test {
            assertEquals(UiState(weatherList = domainList, permissionGranted = true, error = error), awaitItem())
            viewModel.onErrorHandled()
            assertEquals(UiState(weatherList = domainList, permissionGranted = true), awaitItem())
        }
    }
}