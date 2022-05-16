package com.architechcoders.weather.ui.fragments.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import com.architechcoders.usecase.DeleteWeatherUseCase
import com.architechcoders.usecase.GetWeatherByIdUseCase
import com.architechcoders.usecase.UpdateCityWeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WeatherDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getWeatherByIdUseCase: GetWeatherByIdUseCase,
    private val updateCityWeatherUseCase: UpdateCityWeatherUseCase,
    private val deleteWeatherUseCase: DeleteWeatherUseCase
): ViewModel() {

    private val weatherId = WeatherDetailFragmentArgs.fromSavedStateHandle(savedStateHandle).weatherId
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
                .catch {  }
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
        val time = Instant.ofEpochSecond(weather.timeRequested)
        val currentTime = Instant.now()
        val dif = Duration.between(time, currentTime)
        if (TimeUnit.MINUTES.convert(dif.seconds, TimeUnit.SECONDS) >= MAX_MINUTES_TO_FORCE_UPDATE) {
            onUpdateCityWeather()
        }
    }

    fun onDeleteCityWeather() {
        viewModelScope.launch {
            val response = deleteWeatherUseCase(weatherId)
            _uiState.update { it.copy(deleteCompleted = response == null, error = response) }
        }
    }

    companion object {
        const val MAX_MINUTES_TO_FORCE_UPDATE = 30
    }
}