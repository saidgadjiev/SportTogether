package ru.mail.sporttogether.mvp.views.event

import ru.mail.sporttogether.net.models.Event

/**
 * Created by Ivan on 19.10.2016.
 */
interface IListEventView {
    fun loadEvents(events: List<Event>)
}