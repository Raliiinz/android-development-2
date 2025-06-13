package com.example.androiddevelopment2.domain.firebase.fcm.repository

import com.example.androiddevelopment2.domain.firebase.fcm.model.FcmMessage
import com.example.androiddevelopment2.domain.firebase.fcm.model.ScreenDestination

interface FcmRepository {
    suspend fun processMessage(message: FcmMessage)
    fun saveCategory2Payload(data: Map<String, String>)
    fun saveCategory3Payload(screen: ScreenDestination?, extras: Map<String, String>?)
    fun getCategory3Payload(): Pair<ScreenDestination?, Map<String, String>?>?
    fun clearCategory3Payload()
    fun shouldHandleNavigation(): Boolean
}