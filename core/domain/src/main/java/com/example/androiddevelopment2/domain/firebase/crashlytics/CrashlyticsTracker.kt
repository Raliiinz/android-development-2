package com.example.androiddevelopment2.domain.firebase.crashlytics

interface CrashlyticsTracker {
    fun logEvent(eventName: String)
    fun logError(throwable: Throwable)
    fun setCustomKey(key: String, value: String)
    fun setCustomKey(key: String, value: Boolean)
    fun setCustomKey(key: String, value: Int)
}