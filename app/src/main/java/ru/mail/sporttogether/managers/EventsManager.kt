package ru.mail.sporttogether.managers

import ru.mail.sporttogether.net.models.Event
import rx.subjects.PublishSubject
import java.util.*

/**
 * Created by bagrusss on 20.10.16
 */
interface EventsManager {

    enum class UpdateType {
        ADD,
        UPDATE,
        NEW_LIST,
        RESULTED,
        DELETED,
        NEED_SHOW
    }

    data class NewData<out T>(
            val type: UpdateType,
            val data: T
    )


    fun addEvent(e: Event)

    fun updateEvent(event: Event)

    fun swapEvents(events: MutableList<Event>)

    fun resultEvent(event: Event)

    fun deleteEvent(event: Event)

    fun showEvent(event: Event)

    fun getEvents(): ArrayList<Event>

    fun getObservable(): PublishSubject<NewData<*>>


}