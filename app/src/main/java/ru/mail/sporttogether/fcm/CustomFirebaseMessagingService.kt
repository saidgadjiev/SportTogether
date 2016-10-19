package ru.mail.sporttogether.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by said on 17.10.16.
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FMService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.messageId)
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.notification)
        Log.d(TAG, "FCM Data Message: " + remoteMessage.data)
    }
}