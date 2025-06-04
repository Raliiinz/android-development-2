package com.example.androiddevelopment2.domain.firebase.remoteconfig.featureflags

import com.example.androiddevelopment2.domain.firebase.remoteconfig.featureflags.model.FeatureFlag

object FeatureFlags {
    val DETAILS_PAGE = FeatureFlag(
        key = "feature_details_enabled",
        defaultValue = false,
        description = "Controls access to details screen"
    )
}