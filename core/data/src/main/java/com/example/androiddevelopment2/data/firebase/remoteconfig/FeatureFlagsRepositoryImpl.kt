package com.example.androiddevelopment2.data.firebase.remoteconfig

import com.example.androiddevelopment2.domain.firebase.remoteconfig.featureflags.model.FeatureFlag
import com.example.androiddevelopment2.domain.firebase.remoteconfig.FeatureFlagsRepository
import com.example.androiddevelopment2.domain.firebase.remoteconfig.FirebaseRemoteConfigDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeatureFlagsRepositoryImpl @Inject constructor(
    private val remoteConfigDataSource: FirebaseRemoteConfigDataSource
) : FeatureFlagsRepository {

    override suspend fun fetchFeatureFlags() {
        remoteConfigDataSource.fetchConfig()
    }

    override fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean {
        return remoteConfigDataSource.getBoolean(featureFlag.key)
    }
}