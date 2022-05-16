package com.architechcoders.weather.ui.fragments.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.architechcoders.weather.R
import com.architechcoders.weather.databinding.FragmentMainWeatherBinding
import com.architechcoders.weather.ui.fragments.utils.launchOnLifecycle
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

@AndroidEntryPoint
class WeatherMainFragment: Fragment(R.layout.fragment_main_weather) {

    private val viewModel: WeatherMainViewModel by viewModels()
    private lateinit var stateHolder: WeatherMainState
    private val adapter = WeatherAdapter { stateHolder.onWeatherClicked(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        stateHolder = buildStateHolder()
        val binding = FragmentMainWeatherBinding.bind(view)
        binding.toolbar.inflateMenu(R.menu.main_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            stateHolder.onMenuItemClicked(menuItem) {
                viewModel.onAddNewWeatherLocation(it)
            }
        }
        binding.recyclerWeather.adapter = adapter
        viewLifecycleOwner.launchOnLifecycle {
            viewModel.uiState.map { it.weatherList }.distinctUntilChanged().collect {
                binding.weathers = it
            }
        }
        viewLifecycleOwner.launchOnLifecycle {
            viewModel.uiState.map { it.error }.distinctUntilChanged().collect {
                it?.also {
                    Snackbar.make(
                        binding.recyclerWeather,
                        stateHolder.getError(it),
                        Snackbar.LENGTH_LONG
                    ).show()
                    viewModel.onErrorHandled()
                }
            }
        }
        stateHolder.requestLocationPermission {
            viewModel.onLocationPermissionReady(it)
        }
    }
}