package com.example.androiddevelopment2

import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {
    @Inject lateinit var lifecycleHandler: AppLifecycleHandler
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(lifecycleHandler)
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
    }
}
