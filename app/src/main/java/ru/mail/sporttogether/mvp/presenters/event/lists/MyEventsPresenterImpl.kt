package ru.mail.sporttogether.mvp.presenters.event.lists

import ru.mail.sporttogether.app.App
import ru.mail.sporttogether.mvp.views.event.IEventListView

/**
 * Created by bagrusss on 15.10.16
 */
class MyEventsPresenterImpl(view: IEventListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getMyEvents()
}