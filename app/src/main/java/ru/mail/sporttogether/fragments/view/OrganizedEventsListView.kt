package ru.mail.sporttogether.fragments.view

import ru.mail.sporttogether.fragments.view.EventListView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 22.01.17
 */
interface OrganizedEventsListView : EventListView {
    fun resultEvent(e: Event)
}