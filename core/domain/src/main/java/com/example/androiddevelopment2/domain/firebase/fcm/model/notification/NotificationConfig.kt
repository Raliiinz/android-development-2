package com.example.androiddevelopment2.domain.firebase.fcm.model.notification

data class NotificationConfig(
    val channelId: String,
    val title: String?,
    val message: String?,
    val smallIcon: Int,
    val priority: Int
)