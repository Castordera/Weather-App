package com.architechcoders.weather.ui.fragments.detail

import app.cash.turbine.test
import com.architechcoders.usecase.DeleteWeatherUseCase
import com.architechcoders.usecase.GetWeatherByIdUseCase
import com.architechcoders.usecase.UpdateCityWeatherUseCase
import com.architechcoders.weather.CoroutineTestRule
import com.architechcoders.weather.createFakeRepository
import com.architechcoders.weather.data.database.Weather
import com.architechcoders.weather.data.server.RemoteResponse
import com.architechcoders.weather.localWeather
import com.architechcoders.weather.serverWeather
import com.architechcoders.weather.ui.fragments.detail.WeatherDetailViewModel.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.time.Instant

@ExperimentalCoroutinesApi
class DetailWeatherIntegrationTest {

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Test
    fun `data comes from db and not necessary to update`() = runTest {
        val localData = listOf(localWeather.copy(123456, cityName = "Demo", timeRequested = Instant.now().toEpochMilli()))
        val viewModel = setUpViewModel(localData, serverWeather)
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals("Demo", awaitItem().weather!!.cityName)
        }
    }

    @Test
    fun `data comes from db and an update of data is required`() = runTest {
        val localData = listOf(localWeather.copy(123456, cityName = "Demo"))
        val viewModel = setUpViewModel(localData, serverWeather)
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals("Demo", awaitItem().weather!!.cityName)
            assertTrue(awaitItem().loading)
            assertFalse(awaitItem().loading)
            assertEquals("Updated", awaitItem().weather!!.cityName)
        }
    }

    @Test
    fun `data is deleted`() = runTest {
        val localData = listOf(localWeather.copy(123456, cityName = "Demo", timeRequested = Instant.now().toEpochMilli()))
        val viewModel = setUpViewModel(localData, serverWeather)
        viewModel.onDeleteCityWeather()
        viewModel.uiState.test {
            assertEquals(UiState(), awaitItem())
            assertEquals("Demo", awaitItem().weather!!.cityName)
            assertTrue(awaitItem().deleteCompleted)
        }
    }

    private fun setUpViewModel(
        localData: List<Weather> = emptyList(),
        remoteData: RemoteResponse
    ): WeatherDetailViewModel {
        val fakeRepository = createFakeRepository(localData, remoteData)
        val getWeatherByIdUseCase = GetWeatherByIdUseCase(fakeRepository)
        val updateCityWeatherUseCase = UpdateCityWeatherUseCase(fakeRepository)
        val deleteWeatherUseCase = DeleteWeatherUseCase(fakeRepository)
        return WeatherDetailViewModel(
            123456,
            getWeatherByIdUseCase,
            updateCityWeatherUseCase,
            deleteWeatherUseCase
        )
    }
}