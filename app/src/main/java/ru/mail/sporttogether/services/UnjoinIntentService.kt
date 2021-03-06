package ru.mail.sporttogether.services

//import ru.mail.sporttogether.managers.events.EventsManager
//import ru.mail.sporttogether.managers.headers.HeaderManagerImpl
//import ru.mail.sporttogether.mvp.presenters.auth.LoginPresenterImpl
//import ru.mail.sporttogether.net.responses.Response
import android.app.IntentService
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import ru.mail.sporttogether.activities.presenter.LoginActivityPresenterImpl
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.fcm.CustomFirebaseMessagingService.Companion.ID_EVENT_KEY
import ru.mail.sporttogether.fcm.CustomFirebaseMessagingService.Companion.ID_NOTIFICATION_KEY
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.managers.HeaderManagerImpl
import ru.mail.sporttogether.net.Response
import ru.mail.sporttogether.net.api.EventsAPI
import ru.mail.sporttogether.net.models.Event
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject


/**
 * Created by said on 21.10.16.
 */
class UnjoinIntentService : IntentService(NAME) {

    @Inject lateinit var eventsApi: EventsAPI
    @Inject lateinit var headerManager: HeaderManagerImpl
    @Inject lateinit var eventsManager: EventsManager

    override fun onCreate() {
        super.onCreate()
        App.injector.useServiceComponent().inject(this)
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "in unjoin intent service")
        if (intent != null) {
            val id = intent.getIntExtra(ID_EVENT_KEY, 0)
            val notificationId = intent.getIntExtra(ID_NOTIFICATION_KEY, 0)
            Log.d(ShowEventIntentService.TAG, "notification id $notificationId")
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.cancel(notificationId)

            val sharedPreferences = getSharedPreferences(LoginActivityPresenterImpl.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val token = sharedPreferences.getString(LoginActivityPresenterImpl.TOKEN_KEY, "")
            val clientId = sharedPreferences.getString(LoginActivityPresenterImpl.CLIENT_ID_KEY, "")
            headerManager.token = token
            headerManager.clientId = clientId

            eventsApi.unjoinFromEvent(id.toLong())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(object : Subscriber<Response<Any>>() {
                        override fun onNext(t: Response<Any>) {
                            if (t.code == 0) {
                                Toast.makeText(this@UnjoinIntentService,"Вы покинули событие", Toast.LENGTH_SHORT).show()
                                val event = Event()
                                event.id = id.toLong()
                                eventsManager.deleteEvent(event)
                            } else {
                                Log.e(TAG, t.message)
                            }
                        }

                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {

                        }

                    })

        }
    }
    companion object {
        val NAME = "unjoin_intent_service"
        val TAG = "#MY " + UnjoinIntentService::class.java.simpleName
    }
}