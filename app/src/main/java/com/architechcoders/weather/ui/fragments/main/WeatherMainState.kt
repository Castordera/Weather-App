package com.architechcoders.weather.ui.fragments.main

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.architechcoders.domain.Weather
import com.architechcoders.weather.ui.fragments.utils.NewLocationDialog

fun WeatherMainFragment.buildStateHolder(
    context: Context = requireContext(),
    navController: NavController = findNavController(),
    fragmentManager: FragmentManager = childFragmentManager
) = WeatherMainState(context, navController, fragmentManager)

class WeatherMainState(
    private val context: Context,
    private val navController: NavController,
    private val fragmentManager: FragmentManager
) {
    fun onWeatherClicked(weather: Weather) {

    }

    fun requestLocationPermission() {

    }

    fun showAlertNewLocation(onSuccess: (String) -> Unit) {
        NewLocationDialog(onSuccess).show(fragmentManager, NewLocationDialog.TAG)
    }
}