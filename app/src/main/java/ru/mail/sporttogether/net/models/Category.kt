package ru.mail.sporttogether.net.models

/**
 * Created by bagrusss on 29.09.16.
 *
 */
data class Category(
        val id: Long?,
        val name: String
) {
    override fun toString(): String {
        return name
    }
}