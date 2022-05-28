package com.architechcoders.weather.ui.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import com.architechcoders.usecase.AddNewWeatherUseCase
import com.architechcoders.usecase.GetCurrentLocationWeatherUseCase
import com.architechcoders.usecase.GetWeathersUseCase
import com.architechcoders.weather.data.utils.toError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherMainViewModel @Inject constructor(
    getWeathersUseCase: GetWeathersUseCase,
    private val addNewWeatherUseCase: AddNewWeatherUseCase,
    private val getCurrentLocationWeatherUseCase: GetCurrentLocationWeatherUseCase
): ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getWeathersUseCase()
                .onStart { _uiState.update { it.copy(loading = true) } }
                .catch { error -> _uiState.update { it.copy(error = error.toError(), loading = false) } }
                .collect { list -> _uiState.update { it.copy(weatherList = list, loading = false) } }
        }
    }

    fun onAddNewWeatherLocation(city: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true) }
            val response = addNewWeatherUseCase(city)
            _uiState.update { it.copy(error = response, loading = false) }
        }
    }

    fun onLocationPermissionReady(permissionGranted: Boolean) {
        viewModelScope.launch {
            if (permissionGranted) {
                val response = getCurrentLocationWeatherUseCase()
                _uiState.update { it.copy(error = response, permissionGranted = true) }
            } else {
                _uiState.update { it.copy(permissionGranted = false) }
            }
        }
    }

    fun onErrorHandled() {
        _uiState.update { it.copy(error = null) }
    }

    data class UiState(
        val loading: Boolean = false,
        val weatherList: List<Weather>? = null,
        val error: Errors? = null,
        val permissionGranted: Boolean = true
    )
}