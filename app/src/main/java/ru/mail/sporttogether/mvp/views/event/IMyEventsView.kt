package ru.mail.sporttogether.mvp.views.event

import ru.mail.sporttogether.mvp.views.IView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 10.11.16.
 *
 */
interface IMyEventsView : IView {

    fun addOrganizedEvents(events: MutableList<Event>)
    fun addEndedEvents(events: MutableList<Event>)
    fun addMyEvents(events: MutableList<Event>)
    fun clearEvents()
}