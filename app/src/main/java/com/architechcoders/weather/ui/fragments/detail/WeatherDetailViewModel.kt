package com.architechcoders.weather.ui.fragments.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import com.architechcoders.usecase.DeleteWeatherUseCase
import com.architechcoders.usecase.GetWeatherByIdUseCase
import com.architechcoders.usecase.UpdateCityWeatherUseCase
import com.architechcoders.weather.data.utils.toError
import com.architechcoders.weather.di.WeatherId
import com.architechcoders.weather.ui.fragments.utils.shouldUpdate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    @WeatherId private val weatherId: Int,
    getWeatherByIdUseCase: GetWeatherByIdUseCase,
    private val updateCityWeatherUseCase: UpdateCityWeatherUseCase,
    private val deleteWeatherUseCase: DeleteWeatherUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    data class UiState(
        val loading: Boolean = false,
        val weather: Weather? = null,
        val error: Errors? = null,
        val deleteCompleted: Boolean = false
    )

    init {
        viewModelScope.launch {
            getWeatherByIdUseCase(weatherId)
                .catch { error -> _uiState.update { it.copy(error = error.toError()) } }
                .onEach { shouldRefreshWeatherData(it) }
                .collect { weather ->
                    _uiState.update { it.copy(weather = weather) }
                }
        }
    }

    fun onUpdateCityWeather() {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val response = updateCityWeatherUseCase(weatherId)
            _uiState.update { it.copy(error = response, loading = false) }
        }
    }

    private fun shouldRefreshWeatherData(weather: Weather) {
        if (weather.shouldUpdate()) {
            onUpdateCityWeather()
        }
    }

    fun onDeleteCityWeather() {
        viewModelScope.launch {
            val response = deleteWeatherUseCase(weatherId)
            _uiState.update { it.copy(deleteCompleted = response == null, error = response) }
        }
    }
}