package ru.mail.sporttogether.managers.events

import ru.mail.sporttogether.net.models.Event
import rx.Observable
import java.util.*

/**
 * Created by bagrusss on 20.10.16.
 *
 */
interface IEventsManager {
    fun swapEvents(events: ArrayList<Event>)

    fun updateEvent(event: Event)

    fun getEvents(): ArrayList<Event>

    fun getActualEvents(): ArrayList<Event>

    //used for subscribe on events changes
    fun getObservable(): Observable<ArrayList<Event>>
}