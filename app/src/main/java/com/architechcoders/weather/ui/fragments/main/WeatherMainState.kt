package com.architechcoders.weather.ui.fragments.main

import android.Manifest
import android.view.MenuItem
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Weather
import com.architechcoders.weather.R
import com.architechcoders.weather.ui.fragments.utils.NewLocationDialog
import com.architechcoders.weather.ui.fragments.utils.PermissionRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun WeatherMainFragment.buildStateHolder(
    navController: NavController = findNavController(),
    fragmentManager: FragmentManager = childFragmentManager,
    permissionRequester: PermissionRequester = PermissionRequester(
        this, Manifest.permission.ACCESS_COARSE_LOCATION
    ),
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope
) = WeatherMainState(navController, fragmentManager, permissionRequester, scope)

class WeatherMainState(
    private val navController: NavController,
    private val fragmentManager: FragmentManager,
    private val permissionRequester: PermissionRequester,
    private val scope: CoroutineScope
) {
    fun onWeatherClicked(weather: Weather) {
        val action = WeatherMainFragmentDirections.actionMainToDetail(weather.id)
        navController.navigate(action)
    }

    fun requestLocationPermission(onResponse: (Boolean) -> Unit) {
        scope.launch {
            val result = permissionRequester.launch()
            onResponse(result)
        }
    }

    fun onMenuItemClicked(
        menuItem: MenuItem,
        onAddLocationAction: (String) -> Unit
    ): Boolean {
        return when (menuItem.itemId) {
            R.id.add_location -> {
                NewLocationDialog(onAddLocationAction)
                    .show(fragmentManager, NewLocationDialog.TAG)
                true
            }
            else -> false
        }
    }

    fun getError(error: Errors): String = when(error) {
        is Errors.Database -> "Database error: ${error.message}"
        is Errors.Server -> "Server error: ${error.code}"
        is Errors.Unknown -> "Unknown error: ${error.message}"
    }
}