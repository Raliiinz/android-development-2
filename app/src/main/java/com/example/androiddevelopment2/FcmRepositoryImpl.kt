package com.example.androiddevelopment2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.androiddevelopment2.base.R
import com.example.androiddevelopment2.domain.firebase.fcm.model.FcmCategory
import com.example.androiddevelopment2.domain.firebase.fcm.model.FcmMessage
import com.example.androiddevelopment2.domain.firebase.fcm.model.notification.NotificationChannelConfig
import com.example.androiddevelopment2.domain.firebase.fcm.model.notification.NotificationConfig
import com.example.androiddevelopment2.domain.firebase.fcm.model.notification.NotificationConstants
import com.example.androiddevelopment2.domain.firebase.fcm.model.ScreenDestination
import com.example.androiddevelopment2.domain.firebase.fcm.repository.FcmRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit
import com.google.gson.Gson

@Singleton
class FcmRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val appLifecycleHandler: AppLifecycleHandler
) : FcmRepository {
    private val gson = Gson()

    private val prefs = context.getSharedPreferences(
        NotificationConstants.FCM_PREFS_NAME,
        Context.MODE_PRIVATE
    )

    private val highPriorityChannelConfig = NotificationChannelConfig(
        id = NotificationConstants.HIGH_PRIORITY_CHANNEL_ID,
        name = NotificationConstants.HIGH_PRIORITY_CHANNEL_NAME,
        importance = NotificationManager.IMPORTANCE_HIGH
    )

    override suspend fun processMessage(message: FcmMessage) {
        when (message.category) {
            FcmCategory.HIGH_PRIORITY_NOTIFICATION -> showHighPriorityNotification(message)
            FcmCategory.SILENT_UPDATE -> saveCategory2Payload(message.extraData)
            FcmCategory.FEATURE_NAVIGATION -> handleFeatureNavigation(message)
        }
    }

    private fun handleFeatureNavigation(message: FcmMessage) {
        if (!appLifecycleHandler.isAppInForeground) {
            return
        }

        message.screen?.let { screen ->
            saveCategory3Payload(screen, message.extraData)
            (appLifecycleHandler.currentActivity as? MainActivity)?.handleNavigationPayload()
        }
    }


    private fun showHighPriorityNotification(message: FcmMessage) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                highPriorityChannelConfig.id,
                highPriorityChannelConfig.name,
                highPriorityChannelConfig.importance
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationConfig = NotificationConfig(
            channelId = highPriorityChannelConfig.id,
            title = message.title,
            message = message.message,
            smallIcon = R.drawable.ic_fastfood,
            priority = NotificationCompat.PRIORITY_MAX
        )

        val notification = NotificationCompat.Builder(context, notificationConfig.channelId)
            .setContentTitle(notificationConfig.title)
            .setContentText(notificationConfig.message)
            .setSmallIcon(notificationConfig.smallIcon)
            .setPriority(notificationConfig.priority)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }

    override fun saveCategory2Payload(data: Map<String, String>) {
        prefs.edit {
            putString(NotificationConstants.CATEGORY2_DATA_KEY, data.toString())
        }
    }

    override fun saveCategory3Payload(screen: ScreenDestination?, extras: Map<String, String>?) {
        prefs.edit {
            apply {
                putString(
                    NotificationConstants.CATEGORY3_SCREEN_KEY,
                    screen?.let { screen::class.simpleName })
            }
        }
    }


    override fun getCategory3Payload(): Pair<ScreenDestination?, Map<String, String>?>? {
        val screenName = prefs.getString(NotificationConstants.CATEGORY3_SCREEN_KEY, null)
        val extrasJson = prefs.getString(NotificationConstants.CATEGORY3_EXTRA_KEY, null)

        return try {
            val screen = screenName?.let { name ->
                when (name) {
                    "Search" -> ScreenDestination.Search
                    "Graph" -> ScreenDestination.Graph
                    "Auth" -> ScreenDestination.Auth
                    "Register" -> ScreenDestination.Register
                    "RecipeDetails" -> ScreenDestination.RecipeDetails(-1)
                    else -> null
                }
            }
            val extras =
                extrasJson?.let { gson.fromJson(it, Map::class.java) as Map<String, String> }
            screen to extras
        } catch (e: Exception) {
            null
        }
    }

    override fun clearCategory3Payload() {
        prefs.edit {
            apply {
                remove(NotificationConstants.CATEGORY3_SCREEN_KEY)
            }
        }
    }

    override fun shouldHandleNavigation(): Boolean {
        return appLifecycleHandler.isAppInForeground
    }
}