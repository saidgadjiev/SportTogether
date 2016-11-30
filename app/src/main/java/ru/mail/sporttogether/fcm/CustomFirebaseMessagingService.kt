package ru.mail.sporttogether.fcm

import android.app.NotificationManager
import android.content.Context
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import ru.mail.sporttogether.R
import ru.mail.sporttogether.net.models.NotificationMessage

/**
 * Created by said on 17.10.16.
 *
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FMService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("#MY " + javaClass.simpleName, "message received ")

        val body: String? = remoteMessage.notification.body
        val notificationMessage: NotificationMessage = NotificationMessage(
                remoteMessage.data.get("type")?.toInt()!!.or(0),
                remoteMessage.data.get("title").orEmpty(),
                remoteMessage.data.get("message").orEmpty()
        )
        if (notificationMessage.isValid()) {
            val notification = NotificationCompat.Builder(this)
                    .setContentTitle(notificationMessage.title)
                    .setContentText(notificationMessage.message)
                    .setSmallIcon(R.drawable.ic_racing_flag)
                    .build()
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(10, notification)
        } else {
            Log.e("#MY " + javaClass.simpleName, "notification is not valid : " + notificationMessage)

        }
//
//        val gson = GsonBuilder().setPrettyPrinting().create()
//        try {
//            Log.d("#MY " + javaClass.simpleName, "" + notificationMessage.type)
//            Log.d("#MY " + javaClass.simpleName, notificationMessage.title)
//            Log.d("#MY " + javaClass.simpleName, notificationMessage.message)
//
//            } else {
//                Log.e("#MY " + javaClass.simpleName, "empty message or title in notification body")
//
//            }
//        } catch (e: JsonSyntaxException) {
//            Log.e("#MY " + javaClass.simpleName, "notification body is not json : " + body)
//            return
//        }

    }
}