package com.example.androiddevelopment2.data.firebase.crashlytics

import com.example.androiddevelopment2.domain.firebase.crashlytics.CrashlyticsTracker
import com.google.firebase.Firebase
import com.google.firebase.crashlytics.crashlytics
import javax.inject.Inject

class FirebaseCrashlyticsTracker @Inject constructor() : CrashlyticsTracker {
    private val crashlytics = Firebase.crashlytics

    override fun logEvent(eventName: String) {
        crashlytics.log(eventName)
    }

    override fun logError(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    override fun setCustomKey(key: String, value: String) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Boolean) {
        crashlytics.setCustomKey(key, value)
    }

    override fun setCustomKey(key: String, value: Int) {
        crashlytics.setCustomKey(key, value)
    }
}