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


        var type: Int? = remoteMessage.data.get("type")?.toInt()
        if (type == null) type = 0
        val title = remoteMessage.data.get("title")
        val message = remoteMessage.data.get("message")
        if (title == null || message == null) {
            Log.e("#MY " + javaClass.simpleName, "title or message is null")
            return
        }
        val notificationMessage2 = NotificationMessage(
                type,
                title,
                message
        )
        val notificationMessage1 = notificationMessage2
        val notificationMessage: NotificationMessage = notificationMessage1
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