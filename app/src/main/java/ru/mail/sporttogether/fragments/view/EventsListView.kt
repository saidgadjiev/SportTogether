package ru.mail.sporttogether.fragments.view

import ru.mail.sporttogether.mvp.IView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
interface EventsListView : IView {
    fun swapEvents(events: MutableList<Event>)
    fun deleteEvent(e: Event)
}