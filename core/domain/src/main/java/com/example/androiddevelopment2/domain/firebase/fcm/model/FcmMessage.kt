package com.example.androiddevelopment2.domain.firebase.fcm.model

data class FcmMessage(
    val category: FcmCategory,
    val title: String?,
    val message: String?,
    val screen: ScreenDestination?,
    val featureTarget: String?,
    val extraData: Map<String, String> = emptyMap()
)