package ru.mail.sporttogether.activities.presenter

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import ru.mail.sporttogether.activities.view.DrawerView
import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.managers.HeaderManagerImpl
import ru.mail.sporttogether.net.api.AuthorizationAPI
import ru.mail.sporttogether.net.models.Event
import javax.inject.Inject

/**
 * Created by bagrusss on 15.10.16.
 *
 */
class DrawerActivityPresenterImpl(view: DrawerView) : DrawerActivityPresenter {
    private var view: DrawerView? = view

    @Inject lateinit var api: AuthorizationAPI
    @Inject lateinit var context: Context
    @Inject lateinit var headerManager: HeaderManagerImpl
    @Inject lateinit var eventsManager: EventsManager
    @Inject lateinit var socialNetworkManager: SocialNetworkManager
//    private var eventsSubscribion: Subscription? = null

    override fun clickSignOut() {
    }


    override fun showEventOnMap(bundle: Bundle) {
        Log.d(TAG, "show event on map in drawer presenter")
        val id  = bundle.getLong("id")
        val lat = bundle.getDouble("latitude")
        val lng = bundle.getDouble("longtitude")
        val eventFound = eventsManager.getEvents().find { e -> e.id == id }
        if (eventFound != null) {
            Handler().postDelayed({
                Log.d(TAG, "show found event " + eventFound.toString())
                eventsManager.showEvent(eventFound)
            }, 1000)
        } else {
            val event = Event(id = id, lat = lat, lng = lng)
            Handler().postDelayed({
                Log.d(TAG, event.toString())
                eventsManager.showEventPosition(event)
            }, 1000)
        }

    }

    override fun onCreate(args: Bundle?) {
        super.onCreate(args)
//        eventsSubscribion = eventsManager.getObservable()
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { newState ->
//                    when (newState.type) {
//                        EventsManager.UpdateType.NEW_LIST -> {
//
//                        }
//                        else -> Log.e(TAG, "observe with not right type")
//                    }
//                }
    }

    override fun onDestroy() {
        view = null
//        eventsSubscribion = null
    }

    override fun logout() {
        for (network in socialNetworkManager.initializedSocialNetworks) {
            if (network.isConnected) {
                network.logout()
                view?.startLoginActivity()
                break
            }
        }
    }

    override fun getProfileName(): String {
        return socialNetworkManager.activeUser.name
    }

    override fun getAvatar(): String {
        return socialNetworkManager.activeUser.avatar
    }

    init {
        App.injector.usePresenterComponent().inject(this)
    }

    companion object {
        val TAG = "#MY " + DrawerActivityPresenterImpl::class.java.simpleName.substring(0, 18)
    }
}