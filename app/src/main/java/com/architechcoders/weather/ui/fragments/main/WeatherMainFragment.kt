package com.architechcoders.weather.ui.fragments.main

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.architechcoders.weather.R
import com.architechcoders.weather.databinding.FragmentMainWeatherBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        binding.toolbar.setOnMenuItemClickListener { onMenuItemClicked(it) }
        binding.recyclerWeather.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    binding.weathers = it.weatherList
                }
            }
        }
        viewModel.onLocationPermissionReady()
    }

    private fun onMenuItemClicked(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.add_location -> {
                stateHolder.showAlertNewLocation {
                    viewModel.onAddNewWeatherLocation(it)
                }
                true
            }
            else -> false
        }
    }
}