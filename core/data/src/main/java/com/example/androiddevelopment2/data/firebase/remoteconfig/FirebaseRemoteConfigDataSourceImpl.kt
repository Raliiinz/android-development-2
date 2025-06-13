package com.example.androiddevelopment2.data.firebase.remoteconfig

import com.example.androiddevelopment2.domain.firebase.remoteconfig.featureflags.FeatureFlags
import com.example.androiddevelopment2.domain.firebase.remoteconfig.FirebaseRemoteConfigDataSource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.tasks.await

import javax.inject.Inject

class FirebaseRemoteConfigDataSourceImpl @Inject constructor(
    private val remoteConfig: FirebaseRemoteConfig
) : FirebaseRemoteConfigDataSource {

    override suspend fun fetchConfig() {
        remoteConfig.fetchAndActivate().await()
    }

    override fun getBoolean(key: String): Boolean {
        return remoteConfig.getBoolean(key)
    }


    companion object {
        fun setupDefaults(remoteConfig: FirebaseRemoteConfig) {
            val configDefaults = mapOf(
                FeatureFlags.DETAILS_PAGE.key to FeatureFlags.DETAILS_PAGE.defaultValue
            )
            remoteConfig.setDefaultsAsync(configDefaults)
        }
    }
}