package ru.mail.sporttogether.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.messaging.FirebaseMessaging
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.data.FcmTokenManager
import javax.inject.Inject

/**
 * Created by said on 17.10.16.
 */
class CustomFirebaseInstanceIdService: FirebaseInstanceIdService {

    private val TAG = "FMIIdService"
    private val FRIENDLY_ENGAGE_TOPIC = "friendly_engage"
    @Inject lateinit var manager: FcmTokenManager

    constructor() {
        App.injector.useSharedComponent().inject(this)
    }

    override fun onTokenRefresh() {
        val token = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "FCM Token: $token token")

        manager.saveToken(applicationContext, token)
        FirebaseMessaging.getInstance().subscribeToTopic(FRIENDLY_ENGAGE_TOPIC)
    }
}