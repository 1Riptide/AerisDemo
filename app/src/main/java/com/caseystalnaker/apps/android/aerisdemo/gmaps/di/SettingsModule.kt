package com.caseystalnaker.apps.android.aerisdemo.gmaps.di

import android.content.Context
import com.caseystalnaker.apps.android.aerisdemo.gmaps.data.preferenceStore.PrefStoreRepository
import com.caseystalnaker.apps.android.aerisdemo.gmaps.view.viewmodel.PrefViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ActivityComponent::class)
object SettingsModule {

    @Provides
    fun providePreferenceStoreRepository(@ApplicationContext context: Context): PrefStoreRepository =
        PrefStoreRepository(context)

    @Provides
    fun providePrefViewModel(prefStoreRepository: PrefStoreRepository): PrefViewModel =
        PrefViewModel(prefStoreRepository)
}
