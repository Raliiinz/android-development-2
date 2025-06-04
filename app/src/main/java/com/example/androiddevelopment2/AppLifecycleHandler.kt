package com.example.androiddevelopment2

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle
import javax.inject.Inject
import javax.inject.Singleton

@SuppressLint("StaticFieldLeak")
@Singleton
class AppLifecycleHandler @Inject constructor() : Application.ActivityLifecycleCallbacks {
    var isAppInForeground = false
    var currentActivity: Activity? = null

    override fun onActivityResumed(activity: Activity) {
        isAppInForeground = true
        currentActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {
        isAppInForeground = false
        currentActivity = null
    }

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {}

    override fun onActivityStarted(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(
        activity: Activity,
        outState: Bundle
    ) {}

    override fun onActivityDestroyed(activity: Activity) {}
}