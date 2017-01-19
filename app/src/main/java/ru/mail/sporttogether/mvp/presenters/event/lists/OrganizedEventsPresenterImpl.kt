package ru.mail.sporttogether.mvp.presenters.event.lists

import ru.mail.sporttogether.mvp.views.event.IEventListView

/**
 * Created by bagrusss on 18.01.17
 */
class OrganizedEventsPresenterImpl(view: IEventListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getOrginizedEvents()

}