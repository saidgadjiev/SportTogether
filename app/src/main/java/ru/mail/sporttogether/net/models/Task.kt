package ru.mail.sporttogether.net.models

/**
 * Created by Ivan on 27.11.2016.
 */
data class Task(
        val id: Long?,
        val message: String,
        val userId: Long?,
        val eventId: Long?
) {
    override fun toString(): String {
        return message + " " + id
    }
}