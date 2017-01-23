package ru.mail.sporttogether.activities.presenter

import ru.mail.sporttogether.mvp.IPresenter
import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 22.01.17
 */
interface EditEventActivityPresenter : IPresenter {

    fun updateEvent(e: Event)

    fun cancelEvent(e: Event)

}