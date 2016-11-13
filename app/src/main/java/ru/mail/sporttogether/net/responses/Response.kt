package ru.mail.sporttogether.net.responses

import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.models.Event
import java.util.*

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class Response<out T>(
        val code: Int,
        val message: String,
        val data: T
)

class EventsResponse : ArrayList<Event>()

class CategoriesResponse : ArrayList<Category>()