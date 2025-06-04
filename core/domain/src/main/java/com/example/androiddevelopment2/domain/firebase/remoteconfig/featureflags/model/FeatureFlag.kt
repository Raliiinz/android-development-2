package com.example.androiddevelopment2.domain.firebase.remoteconfig.featureflags.model

data class FeatureFlag(
    val key: String,
    val defaultValue: Boolean,
    val description: String
)