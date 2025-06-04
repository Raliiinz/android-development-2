package com.example.androiddevelopment2

import android.util.Log
import com.example.androiddevelopment2.domain.firebase.fcm.model.FcmCategory
import com.example.androiddevelopment2.domain.firebase.fcm.model.FcmMessage
import com.example.androiddevelopment2.domain.firebase.fcm.model.ScreenDestination
import com.example.androiddevelopment2.domain.firebase.fcm.repository.FcmRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFcmService : FirebaseMessagingService() {
    @Inject
    lateinit var fcmRepository: FcmRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            handleDataMessage(remoteMessage)
        }
    }

    private fun handleDataMessage(remoteMessage: RemoteMessage) {
        val message = remoteMessage.toDomainModel()
        CoroutineScope(Dispatchers.IO).launch {
            fcmRepository.processMessage(message)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
//        println(token)
    }

    private fun RemoteMessage.toDomainModel(): FcmMessage {
        val screen = data["screen"]?.let { screenName ->
            ScreenDestination.fromString(screenName)
        }

        return FcmMessage(
            category = data["category"]?.let { FcmCategory.valueOf(it) }
                ?: FcmCategory.HIGH_PRIORITY_NOTIFICATION,
            title = data["title"],
            message = data["message"],
            screen = screen,
            featureTarget = data["feature_target"],
            extraData = data
        )
    }
}