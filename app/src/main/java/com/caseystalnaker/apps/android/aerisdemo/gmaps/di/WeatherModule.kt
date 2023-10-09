package com.caseystalnaker.apps.android.aerisdemo.gmaps.di

import android.content.Context
import com.caseystalnaker.apps.android.aerisdemo.gmaps.data.preferenceStore.PrefStoreRepository
import com.caseystalnaker.apps.android.aerisdemo.gmaps.data.room.MyPlaceRepository
import com.caseystalnaker.apps.android.aerisdemo.gmaps.data.weather.WeatherRepository
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.viewmodel.WeatherViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object WeatherModule {

    @Provides
    fun provideWeatherRepository(@ApplicationContext context: Context): WeatherRepository =
        WeatherRepository(context)

    @Provides
    fun provideWeatherViewModel(
        @ApplicationContext context: Context,
        weatherRepository: WeatherRepository,
        myPlaceRepository: MyPlaceRepository,
        prefStoreRepository: PrefStoreRepository
    ): WeatherViewModel =
        WeatherViewModel(context, weatherRepository, myPlaceRepository, prefStoreRepository)
}