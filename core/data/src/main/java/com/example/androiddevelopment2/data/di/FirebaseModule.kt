package com.example.androiddevelopment2.data.di

import com.example.androiddevelopment2.data.firebase.remoteconfig.FirebaseRemoteConfigDataSourceImpl
import com.example.androiddevelopment2.network.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    private const val MIN_FETCH_INTERVAL_DEBUG = 60L
    private const val MIN_FETCH_INTERVAL_RELEASE = 3600L
    private const val FETCH_TIMEOUT = 10L

    @Provides
    @Singleton
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig {
        val remoteConfig = Firebase.remoteConfig
        FirebaseRemoteConfigDataSourceImpl.setupDefaults(remoteConfig)

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) MIN_FETCH_INTERVAL_DEBUG else MIN_FETCH_INTERVAL_RELEASE
            fetchTimeoutInSeconds = FETCH_TIMEOUT
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        return remoteConfig
    }
}