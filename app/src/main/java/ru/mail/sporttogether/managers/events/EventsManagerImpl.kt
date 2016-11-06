package ru.mail.sporttogether.managers.events

import android.util.LongSparseArray
import ru.mail.sporttogether.net.models.Event
import rx.Observable
import java.util.*

/**
 * Created by bagrusss on 20.10.16.
 *
 */
class EventsManagerImpl : EventsManager {

    private val eventsMap = LongSparseArray<Event>()

    override fun addEvent(e: Event) {
        eventsMap.put(e.id, e)
    }


    override fun swapEvents(events: MutableList<Event>) {
        eventsMap.clear()
        events.forEach {
            eventsMap.put(it.id, it)
        }
    }

    override fun updateEvent(event: Event) {
        eventsMap[event.id]?.let {
            eventsMap.put(event.id, event)
        }
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