package com.architechcoders.data.datasource

import com.architechcoders.domain.Location

interface LocationDataSource {
    suspend fun getLastKnownLocation(): Location?
}