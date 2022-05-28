package com.architechcoders.data.repositories

import com.architechcoders.data.datasource.LocationDataSource
import com.architechcoders.domain.Location
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LocationRepositoryImpTest {

    private lateinit var locationDataSource: LocationDataSource
    private lateinit var locationRepository: LocationRepository

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        locationDataSource = mockk()
        locationRepository = LocationRepositoryImp(locationDataSource)
    }

    @Test
    fun `Get last known location from repository`() = runTest {
        val mockResponse = mockk<Location>()
        coEvery { locationDataSource.getLastKnownLocation() } returns mockResponse
        val response = locationRepository.getLastKnownLocation()
        assertEquals(response, mockResponse)
        coVerify {
            locationDataSource.getLastKnownLocation()
        }
    }

    @Test
    fun `Get last known location from repository with null as response`() = runTest {
        coEvery { locationDataSource.getLastKnownLocation() } returns null
        val response = locationRepository.getLastKnownLocation()
        assertNull(response)
        coVerify {
            locationDataSource.getLastKnownLocation()
        }
    }
}