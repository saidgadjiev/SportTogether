package ru.mail.sporttogether.adapter.events

import ru.mail.sporttogether.net.models.Event

/**
 * Created by bagrusss on 10.11.16.
 *
 */
data class EventWrapper(
        val type: Int,
        val event: Event
)