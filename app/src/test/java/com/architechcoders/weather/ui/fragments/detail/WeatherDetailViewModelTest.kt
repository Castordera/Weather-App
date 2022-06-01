package com.architechcoders.weather.ui.fragments.detail

import app.cash.turbine.test
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import com.architechcoders.usecase.DeleteWeatherUseCase
import com.architechcoders.usecase.GetWeatherByIdUseCase
import com.architechcoders.usecase.UpdateCityWeatherUseCase
import com.architechcoders.weather.CoroutineTestRule
import com.architechcoders.weather.domainWeather
import com.architechcoders.weather.ui.fragments.detail.WeatherDetailViewModel.UiState
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.Instant
import java.time.temporal.ChronoUnit

@ExperimentalCoroutinesApi
class WeatherDetailViewModelTest {

    private lateinit var getWeatherByIdUseCase: GetWeatherByIdUseCase
    private lateinit var updateCityWeatherUseCase: UpdateCityWeatherUseCase
    private lateinit var deleteWeatherUseCase: DeleteWeatherUseCase
    private lateinit var viewModel: WeatherDetailViewModel

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        getWeatherByIdUseCase = mockk()
        updateCityWeatherUseCase = mockk()
        deleteWeatherUseCase = mockk()
    }

    @Test
    fun `Init behavior to get weather by id with refresh option`() = runTest {
        val weather = domainWeather.copy(timeRequested = getTime(true))
        initSetUp(weather)
        coEvery { updateCityWeatherUseCase(any()) } returns null
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(weather = weather), awaitItem())
            assertEquals(UiState(loading = true, weather = weather), awaitItem())
            assertEquals(UiState(weather = weather), awaitItem())
        }
        verify(exactly = 1) {
            getWeatherByIdUseCase(1)
        }
        coVerify(exactly = 1) {
            updateCityWeatherUseCase(1)
        }
    }

    @Test
    fun `Init behavior to get weather by id without refresh`() = runTest {
        val weather = domainWeather.copy(timeRequested = getTime(false))
        initSetUp(weather)
        coEvery { updateCityWeatherUseCase(any()) } returns null
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals(UiState(weather = weather), awaitItem())
        }
        verify(exactly = 1) {
            getWeatherByIdUseCase(1)
        }
        coVerify(exactly = 0) {
            updateCityWeatherUseCase(1)
        }
    }

    @Test
    fun `Delete a weather`() = runTest {
        val weather = domainWeather.copy(timeRequested = getTime(false))
        val error = Errors.Unknown("Error")
        initSetUp(weather)
        coEvery { deleteWeatherUseCase(any()) } returns null andThen error
        viewModel.onDeleteCityWeather()
        viewModel.onDeleteCityWeather()
        viewModel.uiState.drop(2).test {
            assertEquals(UiState(weather = weather, deleteCompleted = true), awaitItem())
            assertEquals(UiState(weather = weather, error = error), awaitItem())
        }
        coVerify(exactly = 2) { deleteWeatherUseCase(1) }
    }


    private fun initSetUp(weather: Weather) {
        every {
            getWeatherByIdUseCase(any())
        } returns flowOf(weather)
        viewModel = WeatherDetailViewModel(
            1,
            getWeatherByIdUseCase,
            updateCityWeatherUseCase,
            deleteWeatherUseCase
        )
    }

    private fun getTime(
        inPast: Boolean,
        instant: Instant = Instant.now()
    ): Long {
        return if (!inPast) {
            instant.toEpochMilli()
        } else {
            instant.minus(30, ChronoUnit.MINUTES).toEpochMilli()
        }
    }
}