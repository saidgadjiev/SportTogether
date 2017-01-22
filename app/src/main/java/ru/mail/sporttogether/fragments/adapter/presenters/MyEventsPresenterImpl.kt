package ru.mail.sporttogether.fragments.adapter.presenters

import ru.mail.sporttogether.fragments.adapter.views.EventListView
import ru.mail.sporttogether.fragments.presenter.AbstractEventsListPresenter

/**
 * Created by bagrusss on 15.10.16
 */
class MyEventsPresenterImpl(view: EventListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getMyEvents()
}