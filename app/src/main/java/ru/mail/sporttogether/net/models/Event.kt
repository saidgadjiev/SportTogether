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
        val id: Long? = null,
        val userId: Long? = null,
        val maxPeople: Int,
        val description: String,
        val isEnded: Boolean,
        val date: Long
)