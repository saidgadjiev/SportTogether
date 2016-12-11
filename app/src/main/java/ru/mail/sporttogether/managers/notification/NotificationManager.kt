package ru.mail.sporttogether.managers.notification

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import ru.mail.sporttogether.R
import ru.mail.sporttogether.services.OkIntentService
import ru.mail.sporttogether.services.RejectIntentService
import java.util.*

/**
 * Created by said on 20.10.16
 */
class NotificationManager {

    private val EVENT_HOUR = "To the hour of the event"

    private fun init(context: Context, msg: String, title: String): NotificationCompat.Builder {
        val builder = NotificationCompat.Builder(context)

        builder.setContentTitle(title)
        builder.setContentText(msg)
        builder.setSmallIcon(R.mipmap.ic_launcher)

        return builder
    }

    private fun addButtons(builder: NotificationCompat.Builder, context: Context, id: Int) {
        val acceptIntent = Intent(context, OkIntentService::class.java)
        val rejectIntent = Intent(context, RejectIntentService::class.java)
        val bundle = Bundle()

        bundle.putInt("notificationID", id)

        acceptIntent.putExtras(bundle)
        rejectIntent.putExtras(bundle)

        val acceptPenIntent = PendingIntent.getService(context, 0, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val rejectPenIntent = PendingIntent.getService(context, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        builder.addAction(R.drawable.ic_check, "Accept", null)
        builder.addAction(R.drawable.ic_remove, "Reject", null)
    }

    fun push(context: Context, notificationMessage: RemoteMessage.Notification) {
        val id = Random().nextInt(32)
        val msg = notificationMessage.body
        val builder = init(context, msg, EVENT_HOUR)

        builder.setOngoing(true)
        addButtons(builder, context, id)

        val notification = builder.build()
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

        manager.notify(id, notification)
    }

    fun cancel(context: Context, id: Int) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        manager.cancel(id)
    }
}