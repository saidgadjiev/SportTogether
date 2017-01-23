package ru.mail.sporttogether.fragments.presenter

import ru.mail.sporttogether.fragments.view.MyEventsListView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 15.10.16
 */
class MyEventsListPresenterImpl(private var view: MyEventsListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getMyEvents()

    override fun onEventChanges(newData: EventsManager.NewData<*>) {
        super.onEventChanges(newData)
        if (newData.type == EventsManager.UpdateType.ANGRY) {
            view?.angryEvent(newData.data as Event)
        }
    }

}