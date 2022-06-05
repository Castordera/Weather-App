package com.architechcoders.weather.di

import android.app.Application
import androidx.room.Room
import com.architechcoders.weather.data.database.WeatherDatabase
import com.architechcoders.weather.data.server.WeatherService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.inMemoryDatabaseBuilder(
        app,
        WeatherDatabase::class.java,
    ).build()

    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase) = database.weatherDao()

    @Provides
    @Singleton
    @ApiUrl
    fun provideApiUrl(): String = "http://localhost:8080"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = HttpLoggingInterceptor().run {
        level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder().addInterceptor(this).build()
    }

    @Provides
    @Singleton
    fun provideWeatherService(@ApiUrl apiUrl: String, okHttpClient: OkHttpClient): WeatherService {
        val retrofit = Retrofit.Builder()
            .baseUrl(apiUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create()
    }

    @Provides
    @Singleton
    fun provideFuseLocationProvider(app: Application): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(app)
}