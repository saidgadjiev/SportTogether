package ru.mail.sporttogether.mvp.views.event

import ru.mail.sporttogether.mvp.views.IView
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 18.01.17
 */
interface IEventListView : IView {
    fun swapEvents(events: MutableList<Event>)

    fun showCatAnimation()

    fun hideCatAnimation()
}