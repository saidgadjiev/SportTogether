package ru.mail.sporttogether.fragments.view

import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 23.01.17
 */
interface EndedEventsListView : EventsListView {
    fun resultEvent(e: Event)
}