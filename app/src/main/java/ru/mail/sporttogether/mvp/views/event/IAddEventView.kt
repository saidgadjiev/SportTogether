package ru.mail.sporttogether.mvp.views.event

import ru.mail.sporttogether.mvp.views.IView

/**
 * Created by bagrusss on 15.10.16.
 *
 */
interface IAddEventView : IView {
    fun onEventAdded(name: String)
}