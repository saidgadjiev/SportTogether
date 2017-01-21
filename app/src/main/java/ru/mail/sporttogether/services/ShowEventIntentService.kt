package ru.mail.sporttogether.services

import android.app.IntentService
import android.content.Intent
import android.util.Log
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
import ru.mail.sporttogether.net.api.EventsAPI
import javax.inject.Inject


/**
 * Created by said on 21.10.16.
 */
class ShowEventIntentService : IntentService(NAME) {

    @Inject lateinit var eventsApi: EventsAPI
    @Inject lateinit var headerManager: HeaderManagerImpl

    override fun onCreate() {
        super.onCreate()
        App.injector.useServiceComponent().inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "in show event intent service")
        if (intent != null) {
        }
    }
    companion object {
        val NAME = "show_event_intent_service"
        val TAG = "#MY " + ShowEventIntentService::class.java.simpleName
    }
}