package com.architechcoders.weather.di

import android.app.Application
import androidx.room.Room
import com.architechcoders.data.datasource.LocationDataSource
import com.architechcoders.data.datasource.WeatherLocalDataSource
import com.architechcoders.data.datasource.WeatherRemoteDataSource
import com.architechcoders.data.repositories.LocationRepository
import com.architechcoders.data.repositories.LocationRepositoryImp
import com.architechcoders.data.repositories.WeatherRepository
import com.architechcoders.data.repositories.WeatherRepositoryImp
import com.architechcoders.weather.data.AndroidLocationDataSource
import com.architechcoders.weather.data.database.WeatherDatabase
import com.architechcoders.weather.data.database.WeatherRoomDataSource
import com.architechcoders.weather.data.server.WeatherServerDataSource
import com.architechcoders.weather.data.server.WeatherService
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application) = Room.databaseBuilder(
        app,
        WeatherDatabase::class.java,
        "weather-db"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideWeatherDao(database: WeatherDatabase) = database.weatherDao()

    @Provides
    @Singleton
    fun provideWeatherService(): WeatherService {
        val okHttpClient = HttpLoggingInterceptor().run {
            level = HttpLoggingInterceptor.Level.BODY
            OkHttpClient.Builder().addInterceptor(this).build()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create()
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AppDataModule {

    @Binds
    abstract fun bindWeatherRepository(weatherRepositoryImp: WeatherRepositoryImp): WeatherRepository

    @Binds
    abstract fun bindLocationRepository(locationRepositoryImp: LocationRepositoryImp): LocationRepository

    @Binds
    abstract fun bindWeatherLocalDataSource(localDataSource: WeatherRoomDataSource): WeatherLocalDataSource

    @Binds
    abstract fun bindWeatherRemoteDataSource(remoteDataSource: WeatherServerDataSource): WeatherRemoteDataSource

    @Binds
    abstract fun bindLocationDataSource(locationDataSource: AndroidLocationDataSource): LocationDataSource
}
