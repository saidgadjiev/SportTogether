package ru.mail.sporttogether.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonObject
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.net.models.NotificationMessage
import ru.mail.sporttogether.services.UnjoinIntentService
import javax.inject.Inject

/**
 * Created by said on 17.10.16.
 *
 */
class CustomFirebaseMessagingService : FirebaseMessagingService() {

    @Inject lateinit var gson: Gson


    override fun onCreate() {
        super.onCreate()
        App.injector.useServiceComponent().inject(this)
    }

    private val TAG = "#MY " + javaClass.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        var type: Int? = remoteMessage.data["type"]?.toInt()
        if (type == null) type = 0
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]
        if (title == null || message == null) {
            Log.e(TAG, "title or message is null")
            return
        }
        Log.d(TAG, "new version $type")
        val event = remoteMessage.data["object"]
        val notificationBuilder = NotificationCompat.Builder(this)
        val notificationMessage: NotificationMessage = NotificationMessage(
                type,
                title,
                message
        )
        if (!notificationMessage.isValid()) {
            Log.e("#MY " + javaClass.simpleName, "notification is not valid : " + notificationMessage)
            return
        }
        notificationBuilder
                .setContentTitle(notificationMessage.title)
                .setContentText(notificationMessage.message)
                .setSmallIcon(R.drawable.ic_racing_flag)
        var currentNotificationId = 0
        if (event != null) {
            val json = gson.fromJson(event, JsonObject::class.java)
            val id = json.get("id").asInt
            when(type) {
                0 -> Log.d(TAG, "type is 0")
                1 -> Log.d(TAG, "type is 1")
                2 -> {
                    Log.d(TAG, "type is 2")
                    currentNotificationId = UNJOIN_ID
                    val unjoinIntent = Intent(this, UnjoinIntentService::class.java)
                    unjoinIntent.putExtra(ID_EVENT_KEY, id)
                    val unjoinPendingIntent: PendingIntent = PendingIntent.getService(this, 0, unjoinIntent, PendingIntent.FLAG_UPDATE_CURRENT)
                    notificationBuilder.addAction(R.drawable.ic_people, "Я не приду", unjoinPendingIntent)
                    Log.d(TAG, "added pending intent of id event $id")
                }
            }
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(currentNotificationId, notificationBuilder.build())
    }

    companion object {
        val UNJOIN_ID = 10
        val ID_EVENT_KEY = "idEvent"
    }
}