package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.fragments.adapter.views.EventListView

/**
 * Created by bagrusss on 18.01.17
 */
class OrganizedEventsPresenterImpl(view: EventListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getOrganizedEvents()

}