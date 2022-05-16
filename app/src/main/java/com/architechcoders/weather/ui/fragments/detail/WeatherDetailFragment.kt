package com.architechcoders.weather.ui.fragments.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.architechcoders.weather.R
import com.architechcoders.weather.databinding.FragmentDetailWeatherBinding
import com.architechcoders.weather.ui.fragments.utils.launchOnLifecycle
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherDetailFragment: Fragment(R.layout.fragment_detail_weather) {

    private val viewModel: WeatherDetailViewModel by viewModels()
    private lateinit var stateHolder: DetailState

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //
        stateHolder = buildStateHolder()
        val binding = FragmentDetailWeatherBinding.bind(view)
        viewLifecycleOwner.launchOnLifecycle {
            viewModel.uiState.collect { uiState ->
                uiState.weather?.also {
                    binding.weather = it
                    if (!stateHolder.isMenuAlreadyAdded && !it.isCurrentLocation) {
                        stateHolder.isMenuAlreadyAdded = true
                        binding.movieDetailToolbar.inflateMenu(R.menu.detail_menu)
                        binding.movieDetailToolbar.setOnMenuItemClickListener(::onMenuItemClick)
                    }
                }
                if (uiState.deleteCompleted) {
                    stateHolder.navigateToPrevScreen()
                }
            }
        }
    }

    private fun onMenuItemClick(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.refresh -> {
                viewModel.onUpdateCityWeather()
                true
            }
            R.id.delete -> {
                viewModel.onDeleteCityWeather()
                true
            }
            else -> false
        }
    }
}