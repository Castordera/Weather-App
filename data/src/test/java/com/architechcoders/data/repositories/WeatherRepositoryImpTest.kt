package com.architechcoders.data.repositories

import arrow.core.left
import arrow.core.right
import com.architechcoders.data.datasource.WeatherLocalDataSource
import com.architechcoders.data.datasource.WeatherRemoteDataSource
import com.architechcoders.domain.Errors
import com.architechcoders.domain.Location
import com.architechcoders.domain.Weather
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class WeatherRepositoryImpTest {

    private lateinit var localDataSource: WeatherLocalDataSource
    private lateinit var remoteDataSource: WeatherRemoteDataSource
    private lateinit var locationRepository: LocationRepository
    private lateinit var weatherRepository: WeatherRepository

    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        locationRepository = mockk()
        weatherRepository = WeatherRepositoryImp(
            localDataSource,
            remoteDataSource,
            locationRepository
        )
    }

    @Test
    fun `Get list of weathers calling local data source`() = runTest {
        val responseMock = listOf<Weather>(mockk(), mockk())
        every { localDataSource.getWeatherList() } returns flowOf(responseMock)
        weatherRepository.getListOfWeathers().collect {
            assertEquals(responseMock, it)
        }
        verify(exactly = 1) {
            localDataSource.getWeatherList()
        }
    }

    @Test
    fun `Add a new location by asking data to server and store it`() = runTest {
        val mockResponse = mockk<Weather>()
        coEvery { remoteDataSource.requestWeatherForCity(any()) } returns mockResponse.right()
        coEvery { localDataSource.storeWeather(any()) } returns null
        val response = weatherRepository.addNewWeatherLocation("City name")
        assertNull(response)
        coVerify(exactly = 1) {
            remoteDataSource.requestWeatherForCity("City name")
            localDataSource.storeWeather(mockResponse)
        }
    }

    @Test
    fun `Add a new location by asking data to server and store it with error in remote`() = runTest {
        val mockResponse = mockk<Errors>()
        coEvery { remoteDataSource.requestWeatherForCity(any()) } returns mockResponse.left()
        val response = weatherRepository.addNewWeatherLocation("City name")
        assertEquals(mockResponse, response)
        coVerify(exactly = 1) {
            remoteDataSource.requestWeatherForCity("City name")
        }
        coVerify(exactly = 0) {
            localDataSource.storeWeather(any())
        }
    }

    @Test
    fun `Add a new location by asking data to server and store it with error in local`() = runTest {
        val mockResponse = mockk<Weather>()
        val mockError = mockk<Errors>()
        coEvery { remoteDataSource.requestWeatherForCity(any()) } returns mockResponse.right()
        coEvery { localDataSource.storeWeather(any()) } returns mockError
        val response = weatherRepository.addNewWeatherLocation("City name")
        assertEquals(response, mockError)
        coVerify(exactly = 1) {
            remoteDataSource.requestWeatherForCity("City name")
            localDataSource.storeWeather(mockResponse)
        }
    }

    @Test
    fun `Retrieve weather by id`() = runTest {
        val mockResponse = mockk<Weather>()
        every { localDataSource.getWeatherByCityId(any()) } returns flowOf(mockResponse)
        weatherRepository.getGetWeatherById(123)
            .catch { fail("Success expected") }
            .collect {
                assertEquals(it, mockResponse)
            }
        verify {
            localDataSource.getWeatherByCityId(123)
        }
    }

    @Test
    fun `Update weather by id`() = runTest {
        val mockWeather = mockk<Weather>()
        coEvery { remoteDataSource.requestWeatherByCityId(any()) } returns mockWeather.right()
        coEvery { localDataSource.updateWeather(any()) } returns null
        val response = weatherRepository.updateWeatherCity(12345)
        assertNull(response)
        coVerify(exactly = 1) {
            remoteDataSource.requestWeatherByCityId(12345)
            localDataSource.updateWeather(mockWeather)
        }
    }

    @Test
    fun `Update weather by id with error in server`() = runTest {
        val mockError = mockk<Errors>()
        coEvery { remoteDataSource.requestWeatherByCityId(any()) } returns mockError.left()
        val response = weatherRepository.updateWeatherCity(12345)
        assertEquals(mockError, response)
        coVerify(exactly = 1) {
            remoteDataSource.requestWeatherByCityId(12345)
        }
        coVerify(exactly = 0) {
            localDataSource.updateWeather(any())
        }
    }

    @Test
    fun `Update weather by id with error in local storage`() = runTest {
        val mockWeather = mockk<Weather>()
        val mockError = Errors.Unknown("Error")
        coEvery { remoteDataSource.requestWeatherByCityId(any()) } returns mockWeather.right()
        coEvery { localDataSource.updateWeather(any()) } returns mockError
        val response = weatherRepository.updateWeatherCity(12345)
        assertEquals(mockError, response)
        coVerify(exactly = 1) {
            remoteDataSource.requestWeatherByCityId(12345)
            localDataSource.updateWeather(mockWeather)
        }
    }

    @Test
    fun `Attempt to delete a weather by id`() = runTest {
        val mockError = Errors.Unknown("Error")
        coEvery { localDataSource.deleteWeatherById(any()) } returns null andThen mockError
        val responseNull = weatherRepository.deleteWeather(1234)
        assertNull(responseNull)
        val response = weatherRepository.deleteWeather(1234)
        assertEquals(mockError, response)
        coVerify(exactly = 2) {
            localDataSource.deleteWeatherById(1234)
        }
    }

    @Test
    fun `Get weather of a current provided location`() = runTest {
        val mockLocation = mockk<Location>()
        val mockWeather = mockk<Weather>()
        coEvery { locationRepository.getLastKnownLocation() } returns mockLocation
        coEvery { remoteDataSource.requestWeatherForLocation(any()) } returns mockWeather.right()
        coEvery { localDataSource.storeCurrentWeather(any()) } returns null
        val response = weatherRepository.getLocationWeather()
        assertNull(response)
        coVerifyOrder {
            locationRepository.getLastKnownLocation()
            remoteDataSource.requestWeatherForLocation(mockLocation)
            localDataSource.storeCurrentWeather(mockWeather)
        }
    }

    @Test
    fun `Get weather of a current provided location with location as null`() = runTest {
        coEvery { locationRepository.getLastKnownLocation() } returns null
        val response = weatherRepository.getLocationWeather()
        assert(response is Errors.Unknown)
        coVerify {
            locationRepository.getLastKnownLocation()
        }
        coVerify(exactly = 0) {
            remoteDataSource.requestWeatherForLocation(any())
            localDataSource.storeCurrentWeather(any())
        }
    }

    @Test
    fun `Get weather of a current provided location with server error`() = runTest {
        val mockLocation = mockk<Location>()
        val mockWeather = mockk<Errors>()
        coEvery { locationRepository.getLastKnownLocation() } returns mockLocation
        coEvery { remoteDataSource.requestWeatherForLocation(any()) } returns mockWeather.left()
        val response = weatherRepository.getLocationWeather()
        assertEquals(mockWeather, response)
        coVerifyOrder {
            locationRepository.getLastKnownLocation()
            remoteDataSource.requestWeatherForLocation(mockLocation)
        }
        coVerify(exactly = 0) {
            localDataSource.storeCurrentWeather(any())
        }
    }

    @Test
    fun `Get weather of a current provided location with local storage error`() = runTest {
        val mockLocation = mockk<Location>()
        val mockWeather = mockk<Weather>()
        val mockError = mockk<Errors>()
        coEvery { locationRepository.getLastKnownLocation() } returns mockLocation
        coEvery { remoteDataSource.requestWeatherForLocation(any()) } returns mockWeather.right()
        coEvery { localDataSource.storeCurrentWeather(any()) } returns mockError
        val response = weatherRepository.getLocationWeather()
        assertEquals(response, mockError)
        coVerifyOrder {
            locationRepository.getLastKnownLocation()
            remoteDataSource.requestWeatherForLocation(mockLocation)
            localDataSource.storeCurrentWeather(mockWeather)
        }
    }
}