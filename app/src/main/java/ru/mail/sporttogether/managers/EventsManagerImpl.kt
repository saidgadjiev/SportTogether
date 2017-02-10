package ru.mail.sporttogether.managers

import android.util.Log
import android.util.LongSparseArray
import ru.mail.sporttogether.net.models.Event
import rx.subjects.PublishSubject
import java.util.*

/**
 * Created by bagrusss on 20.10.16
 */
class EventsManagerImpl : EventsManager {
    private var needShowIdEvent: Long? = null

    private val eventsMap = LongSparseArray<Event>()

    private val eventsUpdate: PublishSubject<EventsManager.NewData<*>> = PublishSubject.create()

    override fun addEvent(e: Event) {
        eventsMap.put(e.id, e)
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.ADD, data = e))
    }


    override fun swapEvents(events: MutableList<Event>) {
        Log.d("EventsManager", "swap events")
        eventsMap.clear()
        events.forEach {
            eventsMap.put(it.id, it)
        }
        needShowIdEvent?.let { id ->
            val foundEvent = events.find { item -> item.id == id }
            needShowIdEvent = null
            foundEvent?.let { it ->
                showEvent(it)
            }
        }
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.NEW_LIST, data = events))
    }

    override fun updateEvent(event: Event) {
        eventsMap[event.id]?.let {
            eventsMap.put(event.id, event)
            eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.UPDATE, data = event))
        }
    }

    override fun resultEvent(event: Event) {
        eventsMap[event.id]?.let {
            it.isEnded = true
            eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.RESULTED, data = event))
        }
    }

    override fun deleteEvent(event: Event) {
        eventsMap.delete(event.id)
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.DELETED, data = event))
    }

    override fun showEvent(event: Event) {
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.NEED_SHOW, data = event))
    }

    override fun showEventWhenLoaded(idEvent: Long) {
        needShowIdEvent = idEvent
    }

    override fun showEventPosition(event: Event) {
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.NEED_SHOW_POSITION, data = event))
    }

    override fun getEvents(): ArrayList<Event> {
        val size = eventsMap.size()
        val list = ArrayList<Event>(size)
        (0..size - 1).mapTo(list) { eventsMap.valueAt(it) }
        return list
    }

    override fun angryEvent(event: Event) {
        eventsUpdate.onNext(EventsManager.NewData(type = EventsManager.UpdateType.ANGRY, data = event))
    }

    override fun getObservable() = eventsUpdate

}