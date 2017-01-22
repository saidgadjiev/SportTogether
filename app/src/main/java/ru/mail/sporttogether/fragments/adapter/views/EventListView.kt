package ru.mail.sporttogether.fragments.adapter.views

import ru.mail.sporttogether.mvp.IView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
interface EventListView : IView {
    fun swapEvents(events: MutableList<Event>)
}