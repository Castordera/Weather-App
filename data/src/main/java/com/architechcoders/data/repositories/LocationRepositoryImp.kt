package com.architechcoders.data.repositories

import com.architechcoders.data.datasource.LocationDataSource
import com.architechcoders.domain.Location
import javax.inject.Inject

class LocationRepositoryImp @Inject constructor(
    private val locationDataSource: LocationDataSource
): LocationRepository {

    override suspend fun getLastKnownLocation(): Location? {
        return locationDataSource.getLastKnownLocation()
    }
}