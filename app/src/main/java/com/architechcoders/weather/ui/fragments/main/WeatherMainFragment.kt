package com.architechcoders.weather.ui.fragments.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.architechcoders.weather.R
import com.architechcoders.weather.databinding.FragmentMainWeatherBinding
import com.architechcoders.weather.ui.fragments.utils.distinctCollect
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

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
        viewLifecycleOwner.distinctCollect(viewModel.uiState, { it.weatherList }) {
            binding.weathers = it
        }
        viewLifecycleOwner.distinctCollect(viewModel.uiState, { it.error }) {
            it?.also {
                Snackbar.make(
                    binding.recyclerWeather,
                    stateHolder.getError(it),
                    Snackbar.LENGTH_LONG
                ).show()
                viewModel.onErrorHandled()
            }
        }
        viewLifecycleOwner.distinctCollect(viewModel.uiState, { it.permissionGranted }) {
            binding.permissionGranted = it
        }
        viewLifecycleOwner.distinctCollect(viewModel.uiState, { it.loading }) {
            binding.loading = it
        }
        stateHolder.requestLocationPermission {
            viewModel.onLocationPermissionReady(it)
        }
    }
}