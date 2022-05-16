package com.architechcoders.weather.ui.fragments.detail

import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

fun WeatherDetailFragment.buildStateHolder(
    navController: NavController = findNavController(),
) = DetailState(navController)

class DetailState(
    private val navController: NavController
) {
    var isMenuAlreadyAdded: Boolean = false

    fun navigateToPrevScreen() {
        navController.popBackStack()
    }
}