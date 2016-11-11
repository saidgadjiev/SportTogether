package ru.mail.sporttogether.data.binding.event

import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 11.11.16.
 *
 */
interface ItemClickListener {
    fun onEventClicked(e: Event)
}