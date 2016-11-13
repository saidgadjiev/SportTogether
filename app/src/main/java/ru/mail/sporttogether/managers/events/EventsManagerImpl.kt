package ru.mail.sporttogether.managers.events

import android.util.LongSparseArray
import ru.mail.sporttogether.net.models.Event
import rx.subjects.BehaviorSubject
import java.util.*

/**
 * Created by bagrusss on 20.10.16.
 *
 */
class EventsManagerImpl : EventsManager {

    private val eventsMap = LongSparseArray<Event>()

    val eventsUpdate: BehaviorSubject<EventsManager.NewData<*>> = BehaviorSubject.create()

    override fun addEvent(e: Event) {
        eventsMap.put(e.id, e)
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.ADD, data = e))
    }


    override fun swapEvents(events: MutableList<Event>) {
        eventsMap.clear()
        events.forEach {
            eventsMap.put(it.id, it)
        }
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.NEW_LIST, data = events))
    }

    override fun updateEvent(event: Event) {
        eventsMap[event.id]?.let {
            eventsMap.put(event.id, event)
        }
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.UPDATE, data = event))
    }

    override fun getEvents(): ArrayList<Event> {
        val size = eventsMap.size()
        val list = ArrayList<Event>(size)
        (0..size - 1).mapTo(list) { eventsMap.valueAt(it) }
        return list
    }

    override fun getObservable() = eventsUpdate

}