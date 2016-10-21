package ru.mail.sporttogether.fcm

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by said on 17.10.16.
 */
class CustomFirebaseInstanceIdService: FirebaseInstanceIdService() {

    private val TAG = "FMIIdService"
    private val FRIENDLY_ENGAGE_TOPIC = "friendly_engage"


    override fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "FCM Token: $token token")

        FirebaseMessaging.getInstance().subscribeToTopic(FRIENDLY_ENGAGE_TOPIC)
    }
}