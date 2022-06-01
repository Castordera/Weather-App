package com.architechcoders.weather.di

import androidx.lifecycle.SavedStateHandle
import com.architechcoders.weather.ui.fragments.detail.WeatherDetailFragmentArgs
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class DetailViewModelModule {

    @Provides
    @ViewModelScoped
    @WeatherId
    fun provideWeatherId(savedStateHandle: SavedStateHandle) =
        WeatherDetailFragmentArgs.fromSavedStateHandle(savedStateHandle).weatherId
}