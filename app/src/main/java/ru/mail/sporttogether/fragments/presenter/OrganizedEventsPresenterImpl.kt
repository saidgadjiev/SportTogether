package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.fragments.view.OrganizedEventsListView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
class OrganizedEventsPresenterImpl(private var view: OrganizedEventsListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getOrganizedEvents()

    override fun onDestroy() {
        view = null
        eventsSubscription?.unsubscribe()
        super.onDestroy()
    }

    override fun onEventChanges(newData: EventsManager.NewData<*>) {
        super.onEventChanges(newData)
        if (newData.type == EventsManager.UpdateType.RESULTED) {
            view?.deleteEvent(newData.data as Event)
        }
    }

}