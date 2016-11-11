package ru.mail.sporttogether.fcm

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.mail.sporttogether.R

/**
 * Created by said on 17.10.16.
 *
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FMService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Handle data payload of FCM messages.
        Log.d(TAG, "FCM Message Id: " + remoteMessage.messageId)
        Log.d(TAG, "FCM Notification Message: " + remoteMessage.notification)
        Log.d(TAG, "FCM Data Message: " + remoteMessage.data)
        val notification = NotificationCompat.Builder(this)
                .setContentTitle("Событие")
                //.setContentText(remoteMessage.data.toString())
                .setContentText("Событие отменено")
                .setSmallIcon(R.drawable.ic_racing_flag)
                .build()
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(10, notification)
    }
}