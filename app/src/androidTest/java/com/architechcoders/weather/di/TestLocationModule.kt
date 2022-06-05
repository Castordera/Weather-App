package com.architechcoders.weather.di

import com.architechcoders.data.datasource.LocationDataSource
import com.architechcoders.domain.Location
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Inject

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocationModule::class]
)
abstract class TestLocationModule {
    @Binds
    abstract fun bindLocationDataSource(locationDataSource: FakeAndroidLocationDataSource): LocationDataSource
}

class FakeAndroidLocationDataSource @Inject constructor(): LocationDataSource {

    override suspend fun getLastKnownLocation(): Location {
        return Location(0.0,0.0)
    }
}