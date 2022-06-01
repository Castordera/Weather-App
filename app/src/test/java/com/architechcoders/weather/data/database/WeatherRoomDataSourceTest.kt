package com.architechcoders.weather.data.database

import android.database.sqlite.SQLiteConstraintException
import com.architechcoders.data.datasource.WeatherLocalDataSource
import com.architechcoders.domain.Errors
import com.architechcoders.weather.CoroutineTestRule
import com.architechcoders.weather.domainWeather
import com.architechcoders.weather.localWeather
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
class WeatherRoomDataSourceTest {
    
    private lateinit var weatherDao: WeatherDao
    private lateinit var dataSource: WeatherLocalDataSource
    
    @get:Rule
    val coroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        weatherDao = mockk()
        dataSource = WeatherRoomDataSource(weatherDao)
    }

    @Test
    fun `Retrieve a list of weather from database`() = runTest {
        val weather = listOf(localWeather.copy(id = 2), localWeather.copy(id = 1))
        every { weatherDao.getAllWeathers() } returns flowOf(weather)
        dataSource.getWeatherList()
            .catch { fail("An error was not expected") }
            .collect {
                assertEquals(it.size, 2)
                assertEquals(it[0].id, 2)
                assertEquals(it[1].id, 1)
            }
        verify {
            weatherDao.getAllWeathers()
        }
    }

    @Test
    fun `Attempt to store weather in database`() = runTest {
        coEvery { weatherDao.insertWeather(any()) } just runs
        val response = dataSource.storeWeather(domainWeather.copy(id = 1))
        assertNull(response)
    }

    @Test
    fun `Attempt to store weather in database with error while storing`() = runTest {
        coEvery { weatherDao.insertWeather(any()) } throws SQLiteConstraintException()
        val response = dataSource.storeWeather(domainWeather.copy(id = 1))
        assert(response is Errors.Database.SaveFailed)
    }

    @Test
    fun `Attempt to get a weather by city id`() = runTest {
        every { weatherDao.getWeatherCityById(any()) } returns flowOf(localWeather.copy(123456))
        dataSource.getWeatherByCityId(123456)
            .collect {
                assertEquals(it.id, 123456)
            }
        verify {
            weatherDao.getWeatherCityById(123456)
        }
    }

    @Test
    fun `Attempt to update the weather by given weather`() = runTest {
        coEvery { weatherDao.updateWeather(any()) } just runs andThenThrows Exception("Error")
        val responseSuccess = dataSource.updateWeather(domainWeather.copy(id = 1))
        assertNull(responseSuccess)
        val responseError = dataSource.updateWeather(domainWeather.copy(id = 123456))
        assert(responseError is Errors.Unknown)
        coVerify {
            weatherDao.updateWeather(localWeather.copy(id = 1))
            weatherDao.updateWeather(localWeather.copy(id = 123456))
        }
    }

    @Test
    fun `Attempt to delete a weather by city id`() = runTest {
        coEvery { weatherDao.deleteWeather(any()) } just runs andThenThrows Exception("Error")
        val responseSuccess = dataSource.deleteWeatherById(123)
        assertNull(responseSuccess)
        val responseError = dataSource.deleteWeatherById(987)
        assert(responseError is Errors.Unknown)
        coVerify {
            weatherDao.deleteWeather(123)
            weatherDao.deleteWeather(987)
        }
    }

    @Test
    fun `Attempt to store current weather`() = runTest {
        coEvery { weatherDao.insertLocalWeather(any()) } just runs andThenThrows Exception("Error")
        val responseSuccess = dataSource.storeCurrentWeather(domainWeather.copy(id = 1))
        assertNull(responseSuccess)
        val responseError = dataSource.storeCurrentWeather(domainWeather.copy(id = 3))
        assert(responseError is Errors.Unknown)
        coVerify {
            weatherDao.insertLocalWeather(localWeather.copy(id = 1))
            weatherDao.insertLocalWeather(localWeather.copy(id = 3))
        }
    }
}