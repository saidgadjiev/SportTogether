package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.fragments.adapter.views.EventListView

/**
 * Created by bagrusss on 19.01.17
 */
class EndedEventsPresenterImpl(view: EventListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getResultedEvents()

}