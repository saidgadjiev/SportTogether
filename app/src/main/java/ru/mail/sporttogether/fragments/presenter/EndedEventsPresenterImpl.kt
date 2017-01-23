package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.fragments.view.EndedEventsListView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 19.01.17
 */
class EndedEventsPresenterImpl(private var view: EndedEventsListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getResultedEvents()

    override fun onEventChanges(newData: EventsManager.NewData<*>) {
        super.onEventChanges(newData)
        if (newData.type == EventsManager.UpdateType.RESULTED) {
            view?.resultEvent(newData.data as Event)
        }
    }

}