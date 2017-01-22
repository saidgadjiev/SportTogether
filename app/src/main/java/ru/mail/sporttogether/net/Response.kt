package ru.mail.sporttogether.net

import ru.mail.sporttogether.net.models.Category
import ru.mail.sporttogether.net.models.Event
import ru.mail.sporttogether.net.models.Task
import java.util.*

/**
 * Created by bagrusss on 29.09.16
 */
data class Response<out T>(
        val code: Int,
        val message: String,
        val data: T
)

class EventsResponse : ArrayList<Event>()

class TasksResponse : ArrayList<Task>()

class CategoriesResponse : ArrayList<Category>()