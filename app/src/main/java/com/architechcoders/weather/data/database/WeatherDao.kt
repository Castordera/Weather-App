package com.architechcoders.weather.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM Weather")
    fun getAllWeathers(): Flow<List<Weather>>

    @Query("SELECT * FROM Weather WHERE id = :id")
    fun getWeatherCityById(id: Int): Flow<Weather>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertWeather(weather: Weather)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocalWeather(weather: Weather)

    @Update
    suspend fun updateWeather(weather: Weather)

    @Query("DELETE FROM Weather WHERE id = :id")
    suspend fun deleteWeather(id: Int)
}