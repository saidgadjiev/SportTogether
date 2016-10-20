package ru.mail.sporttogether.managers.events

import ru.mail.sporttogether.net.models.Event
import rx.Observable
import java.util.*

/**
 * Created by bagrusss on 20.10.16.
 *
 */
class EventsManagerImpl : IEventsManager {

    override fun swapEvents(events: ArrayList<Event>) {

    }

    override fun updateEvent(event: Event) {

    }

    override fun getEvents(): ArrayList<Event> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getActualEvents(): ArrayList<Event> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getObservable(): Observable<ArrayList<Event>> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}