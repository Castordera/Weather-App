package com.architechcoders.weather.data

import android.annotation.SuppressLint
import android.location.Location
import com.architechcoders.data.datasource.LocationDataSource
import com.google.android.gms.location.FusedLocationProviderClient
import com.architechcoders.domain.Location as DomainLocation
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class AndroidLocationDataSource @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient
): LocationDataSource {

    @SuppressLint("MissingPermission")
    override suspend fun getLastKnownLocation(): DomainLocation? {
        return suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    continuation.resume(it.result?.toDomain())
                }
        }
    }

    private fun Location.toDomain(): DomainLocation {
        return DomainLocation(latitude, longitude)
    }
}