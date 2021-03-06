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
        NEED_SHOW,
        NEED_SHOW_POSITION,
        ANGRY
    }

    data class NewData<out T>(
            val type: UpdateType,
            val data: T
    )


    fun isNeedJoin(): Boolean
    fun setNeedJoin(newVal: Boolean)


    fun addEvent(e: Event)

    fun updateEvent(event: Event)

    fun swapEvents(events: MutableList<Event>)

    fun resultEvent(event: Event)

    fun deleteEvent(event: Event)

    fun showEvent(event: Event)

    fun showEventWhenLoaded(idEvent: Long)

    fun angryEvent(event: Event)

    fun showEventPosition(event: Event)

    fun getEvents(): ArrayList<Event>

    fun getObservable(): PublishSubject<NewData<*>>

    fun getCreatingEvent(): Event?

    fun setCreatingEvent(e: Event)


}