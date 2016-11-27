package ru.mail.sporttogether.fcm

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import ru.mail.sporttogether.R
import ru.mail.sporttogether.net.models.NotificationMessage

/**
 * Created by said on 17.10.16.
 *
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FMService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val data: MutableMap<String, String>? = remoteMessage.data
        val body: String?
        try {
            body = remoteMessage.notification.body
        } catch (e: NullPointerException) {
            Log.e("#MY " + javaClass.simpleName, "no body in received notification")
            return
        }

        val gson = GsonBuilder().setPrettyPrinting().create()
        try {
            val notificationMessage: NotificationMessage = gson.fromJson(body, NotificationMessage::class.java)
            Log.d("#MY " + javaClass.simpleName, "" + notificationMessage.type)
            Log.d("#MY " + javaClass.simpleName, notificationMessage.title)
            Log.d("#MY " + javaClass.simpleName, notificationMessage.message)
            if (notificationMessage.isValid()) {
                val notification = NotificationCompat.Builder(this)
                        .setContentTitle(notificationMessage.title)
                        .setContentText(notificationMessage.message)
                        .setSmallIcon(R.drawable.ic_racing_flag)
                        .build()
                val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(10, notification)
            } else {
                Log.e("#MY " + javaClass.simpleName, "empty message or title in notification body")
                return
            }
        } catch (e: JsonSyntaxException) {
            Log.e("#MY " + javaClass.simpleName, "notification body is not json : " + body)
            return
        }

    }
}