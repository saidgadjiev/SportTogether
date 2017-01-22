package ru.mail.sporttogether.fragments.presenter

import android.os.Bundle
import ru.mail.sporttogether.fragments.view.OrganizedEventsListView
import ru.mail.sporttogether.managers.EventsManager
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
class OrganizedEventsPresenterImpl(private var view: OrganizedEventsListView?) : AbstractEventsListPresenter(view) {

    override fun getApiObservable() = eventsApi.getOrganizedEvents()

    override fun onCreate(args: Bundle?) {
        eventsSubscription = eventsManager.getObservable().subscribe { data ->
            when (data.type) {
                EventsManager.UpdateType.RESULTED -> {
                    view?.deleteEvent(data.data as Event)
                }
                EventsManager.UpdateType.DELETED -> {
                    view?.deleteEvent(data.data as Event)
                }
            }
        }
    }

    override fun onDestroy() {
        view = null
        eventsSubscription?.unsubscribe()
        super.onDestroy()
    }

}