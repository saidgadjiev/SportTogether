package ru.mail.sporttogether.services

import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import ru.mail.sporttogether.activities.DrawerActivity
import ru.mail.sporttogether.activities.SplashActivity
import ru.mail.sporttogether.fcm.CustomFirebaseMessagingService


/**
 * Created by said on 21.10.16.
 */
class ShowEventIntentService : IntentService(NAME) {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        if (intent != null) {
            val notificationId = intent.getIntExtra(CustomFirebaseMessagingService.ID_NOTIFICATION_KEY, 0)
            Log.d(TAG, "notification id $notificationId")
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(notificationId)

            val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            this.sendBroadcast(it)

            val isCreated = DrawerActivity.isCreated
            Log.d(TAG, "drawer activity is created : $isCreated")
            val startingIntent: Intent
            if (isCreated) {
                startingIntent = Intent(this, DrawerActivity::class.java)
            } else {
                startingIntent = Intent(this, SplashActivity::class.java)
            }
            startingIntent.putExtra("data", intent.getBundleExtra("data"))
            startActivity(startingIntent)
        }
    }
    companion object {
        val NAME = "show_event_intent_service"
        val TAG = "#MY " + ShowEventIntentService::class.java.simpleName
    }
}