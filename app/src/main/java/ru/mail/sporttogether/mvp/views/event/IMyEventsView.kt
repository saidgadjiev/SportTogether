package ru.mail.sporttogether.mvp.views.event

import ru.mail.sporttogether.mvp.views.IView
import ru.mail.sporttogether.net.responses.EventsResponse

/**
 * Created by bagrusss on 10.11.16
 */
interface IMyEventsView : IView {
    fun openEditActivity(id: Long)
    fun updateEvents(events: EventsResponse)
}