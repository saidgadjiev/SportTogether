package ru.mail.sporttogether.mvp.presenters.event

import ru.mail.sporttogether.data.binding.event.ItemClickListener
import ru.mail.sporttogether.mvp.presenters.IPresenter

/**
 * Created by bagrusss on 15.10.16.
 *
 */
interface MyEventsPresenter : IPresenter, ItemClickListener {
    fun getMyEvents()
}