package com.architechcoders.data.repositories

import com.architechcoders.domain.Location

interface LocationRepository {
    suspend fun getLastKnownLocation(): Location?
}