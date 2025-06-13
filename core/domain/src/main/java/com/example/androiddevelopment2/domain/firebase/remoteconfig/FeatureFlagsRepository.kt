package com.example.androiddevelopment2.domain.firebase.remoteconfig

import com.example.androiddevelopment2.domain.firebase.remoteconfig.featureflags.model.FeatureFlag

interface FeatureFlagsRepository {
    suspend fun fetchFeatureFlags()
    fun isFeatureEnabled(featureFlag: FeatureFlag): Boolean
}