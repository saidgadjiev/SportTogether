package ru.mail.sporttogether.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.notification.NotificationManager
import javax.inject.Inject

/**
 * Created by said on 17.10.16.
 */
class CustomFirebaseMessagingService : FirebaseMessagingService {
    private val TAG = "FMService"
    @Inject lateinit var manager: NotificationManager

    constructor() {
        App.injector.useSharedComponent().inject(this)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.messageId)
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.notification)
        Log.d(TAG, "FCM Data Message: " + remoteMessage.data)
        manager.push(applicationContext, remoteMessage.notification)
    }
}