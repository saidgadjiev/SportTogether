package ru.mail.sporttogether.fcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.NotificationCompat
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import ru.mail.sporttogether.R
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.net.models.NotificationMessage
import ru.mail.sporttogether.services.ShowEventIntentService
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
                .setSmallIcon(R.mipmap.ic_launcher)
        var currentNotificationId = (System.currentTimeMillis() / 1000 % 10000).toInt()

        when(type) {
            0 -> {
                currentNotificationId += CANCEL_KOEF * 10000
            }
            1 -> {
                if (event != null) {
                    currentNotificationId += RESULT_KOEF * 10000
                    resultNotificationLogic(currentNotificationId, notificationBuilder, event)
                }
            }
            2 -> {
                if (event != null) {
                    currentNotificationId += REMIND_KOEF * 10000
                    remindNotificationLogic(currentNotificationId, notificationBuilder, event)
                }
            }
        }
        Log.d(TAG, "getted notification, type is $type generated notification_id: $currentNotificationId")
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(currentNotificationId, notificationBuilder.build())
    }

    fun remindNotificationLogic(notifId: Int, builder: NotificationCompat.Builder, event: String) {
        val json = gson.fromJson(event, JsonObject::class.java)
        val id = json.get("id").asInt
        val unjoinIntent = Intent(this, UnjoinIntentService::class.java)
        val showingIntent = Intent(this, ShowEventIntentService::class.java)
        unjoinIntent.putExtra(ID_EVENT_KEY, id)
        unjoinIntent.putExtra(ID_NOTIFICATION_KEY, notifId)
        val bundle = bundleFromJSON(json)
        bundle.putLong("id", id.toLong())
        showingIntent.putExtra("data", bundle)
        showingIntent.putExtra(ID_NOTIFICATION_KEY, notifId)
        val unjoinPendingIntent: PendingIntent = PendingIntent.getService(
                this,
                notifId,
                unjoinIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        val showingPendingIntent: PendingIntent = PendingIntent.getService(
                this,
                notifId + 1,
                showingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.addAction(R.drawable.ic_people, "Я не приду", unjoinPendingIntent)
        builder.addAction(R.drawable.ic_map, "Подробнее", showingPendingIntent)
        Log.d(TAG, "added pending intent of id event $id, notification id $notifId")
    }

    fun resultNotificationLogic(notifId: Int, builder: NotificationCompat.Builder, event: String) {
        val json: JsonObject = gson.fromJson(event, JsonObject::class.java)
        val id = json.get("id").asInt

        //костыль, от сервера в поле заголовка отсутствует имя
        val jsonName: JsonElement? = json.get("name")
        if (jsonName != null) {
            builder.setContentTitle(builder.mContentTitle.toString() + " " + jsonName.asString)
        }

        val showingIntent = Intent(this, ShowEventIntentService::class.java)
        val bundle = bundleFromJSON(json)
        bundle.putLong("id", id.toLong())
        showingIntent.putExtra("data", bundle)
        showingIntent.putExtra(ID_NOTIFICATION_KEY, notifId)
        val showingPendingIntent: PendingIntent = PendingIntent.getService(
                this,
                notifId + 1,
                showingIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        )
        builder.addAction(R.drawable.ic_map, "Подробнее", showingPendingIntent)
        Log.d(TAG, "added pending intent of id event $id, notification id $notifId")
    }

    fun bundleFromJSON(json: JsonObject): Bundle {
        val bundle = Bundle()
        val jsonLatitude: JsonElement? = json.get("latitude")
        val jsonLongtitude: JsonElement? = json.get("longtitude")
        if (jsonLatitude != null) {
            bundle.putDouble("latitude", jsonLatitude.asDouble)
        }
        if (jsonLongtitude != null) {
            bundle.putDouble("longtitude", jsonLongtitude.asDouble)
        }
        return  bundle
    }

    companion object {
        val REMIND_KOEF = 1
        val RESULT_KOEF = 2
        val CANCEL_KOEF = 3
        val ID_EVENT_KEY = "idEvent"
        val ID_NOTIFICATION_KEY = "idNotification"
    }
}