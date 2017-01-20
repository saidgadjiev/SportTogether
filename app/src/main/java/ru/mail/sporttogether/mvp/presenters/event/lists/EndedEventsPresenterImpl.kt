package ru.mail.sporttogether.mvp.presenters.event.lists

import ru.mail.sporttogether.mvp.views.event.IEventListView

/**
 * Created by bagrusss on 19.01.17
 */
class EndedEventsPresenterImpl(view: IEventListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getResultedEvents()

}