package ru.mail.sporttogether.activities.view

import ru.mail.sporttogether.data.binding.EventDetailListener
import ru.mail.sporttogether.mvp.IView

/**
 * Created by bagrusss on 12.02.17
 */
interface EventDetailsView : IView, EventDetailListener {
    fun updateAddress(address: String)
}