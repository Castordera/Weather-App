package com.architechcoders.weather.ui.fragments.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherMainViewModel @Inject constructor(

): ViewModel() {

    //
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val weather = Weather("Varsovia", "Soleado", 37.0f, 20f, 15f, 14f, true)
            val weather2 = Weather("Varsovia", "Soleado", 37.0f, 20f, 15f, 14f, false)
            _uiState.update { it.copy(weatherList = listOf(weather, weather2)) }
        }
    }

    fun onAddNewWeatherLocation(string: String) {

    }

    fun onLocationPermissionReady() {

    }

    data class UiState(
        val loading: Boolean = false,
        val weatherList: List<Weather>? = null,
        val error: Errors? = null
    )
}