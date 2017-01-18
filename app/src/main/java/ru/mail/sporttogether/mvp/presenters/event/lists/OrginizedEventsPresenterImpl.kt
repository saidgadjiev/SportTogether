package ru.mail.sporttogether.mvp.presenters.event.lists

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.views.event.IEventListView

/**
 * Created by bagrusss on 18.01.17
 */
class OrginizedEventsPresenterImpl(view: IEventListView?) : EventsListPresenter(view) {

    init {
        App.injector
                .usePresenterComponent()
                .inject(this)
    }

    override fun getApiObservable() = eventsApi.getOrginizedEvents()

}