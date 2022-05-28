package com.architechcoders.weather.data.server

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("weather")
    suspend fun getWeatherForLocation(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): RemoteResponse

    @GET("weather")
    suspend fun getWeatherForCity(
        @Query("q") cityName: String,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): RemoteResponse

    @GET("weather")
    suspend fun getWeatherByCityId(
        @Query("id") cityId: Int,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): RemoteResponse
}