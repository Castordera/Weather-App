package com.architechcoders.weather.ui.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import com.architechcoders.usecase.AddNewWeatherUseCase
import com.architechcoders.usecase.GetCurrentLocationWeatherUseCase
import com.architechcoders.usecase.GetWeathersUseCase
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
                .catch {  }
                .collect { list -> _uiState.update { it.copy(weatherList = list) } }
        }
    }

    fun onAddNewWeatherLocation(city: String) {
        viewModelScope.launch {
            val response = addNewWeatherUseCase(city)
            _uiState.update { it.copy(error = response) }
        }
    }

    fun onLocationPermissionReady(permissionGranted: Boolean) {
        viewModelScope.launch {
            if (permissionGranted) {
                val response = getCurrentLocationWeatherUseCase()
                _uiState.update { it.copy(error = response) }
//                println("DEMO, VM => $response")
            }
        }
    }

    fun onErrorHandled() {
        _uiState.update { it.copy(error = null) }
    }

    data class UiState(
        val loading: Boolean = false,
        val weatherList: List<Weather>? = null,
        val error: Errors? = null
    )
}