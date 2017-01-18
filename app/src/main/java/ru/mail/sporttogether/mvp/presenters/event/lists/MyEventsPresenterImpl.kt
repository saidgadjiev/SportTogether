package ru.mail.sporttogether.mvp.presenters.event.lists

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.auth.core.SocialNetworkManager
import ru.mail.sporttogether.mvp.views.event.IEventListView
import ru.mail.sporttogether.net.models.User

/**
 * Created by bagrusss on 15.10.16
 */
class MyEventsPresenterImpl(view: IEventListView?) : EventsListPresenter(view) {

    private var socialNetworkManager: SocialNetworkManager = SocialNetworkManager.instance
    private val user: User


    init {
        App.injector
                .usePresenterComponent()
                .inject(this)

        user = socialNetworkManager.activeUser
    }

    override fun getApiObservable() = eventsApi.getMyEvents()
}