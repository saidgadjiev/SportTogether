package ru.mail.sporttogether.net.models

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class Event(
        val name: String,
        val categoryId: Long,
        val latitude: Double,
        val longtitude: Double,
        val id: Long?,
        val userId: Long
)